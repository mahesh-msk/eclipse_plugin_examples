/*
 * FILE:            PropertiesFileAuditor.java
 *
 * SW-COMPONENT:    com.ancit.favorites
 *
 * DESCRIPTION:     -
 *
 * COPYRIGHT:       © 2015 - 2022 Robert Bosch GmbH
 *
 * The reproduction, distribution and utilization of this file as
 * well as the communication of its contents to others without express
 * authorization is prohibited. Offenders will be held liable for the
 * payment of damages. All rights reserved in the event of the grant
 * of a patent, utility model or design.
 */
package com.ancit.favorites.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import com.ancit.favorites.FavoritesPlugin;
import com.ancit.favorites.logging.FavoritesLog;

public class PropertiesFileAuditor extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = FavoritesPlugin.PLUGIN_ID + ".propertiesFileAuditor";

	private static final String MARKER_ID = FavoritesPlugin.PLUGIN_ID + ".auditmarker";
	public static final String KEY = "key";
	public static final String VIOLATION = "violation";
	public static final int MISSING_KEY_VIOLATION = 1;
	public static final int UNUSED_KEY_VIOLATION = 2;

	public PropertiesFileAuditor() {
	}

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (shouldAudit(kind)) {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					auditPluginManifest(monitor);
				}
			}, monitor);
		}
		return null;
	}

	private boolean shouldAudit(int kind) {
		if (kind == FULL_BUILD) {
			return true;
		}
		IResourceDelta delta = getDelta(getProject());
		if (delta == null) {
			return false;
		}
		IResourceDelta[] children = delta.getAffectedChildren();
		for (IResourceDelta child : children) {
			String fileName = child.getProjectRelativePath().lastSegment();
			if ("plugin.xml".equals(fileName) || "plugin.properties".equals(fileName)) {
				return true;
			}
		}
		return false;
	}

	private void auditPluginManifest(IProgressMonitor monitor) {
		monitor.beginTask("Audit plugin manifest", 4);

		if (!deleteAuditMarkers(getProject()) || checkCancel(monitor)) {
			return;
		}
		IProject proj = getProject();

		if (checkCancel(monitor)) {
			return;
		}
		Map pluginKeys = scanPlugin(getProject().getFile("plugin.xml"));
		monitor.worked(1);

		if (checkCancel(monitor)) {
			return;
		}
		Map propertyKeys = scanProperties(getProject().getFile("plugin.properties"));
		monitor.worked(1);
		if (checkCancel(monitor)) {
			return;
		}

		Iterator iter = pluginKeys.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			if (!propertyKeys.containsKey(entry.getKey())) {
				reportProblem("Missing property key", (Location) entry.getValue(), MISSING_KEY_VIOLATION, true);
			}
		}
		monitor.worked(1);

		if (checkCancel(monitor)) {
			return;
		}

		iter = propertyKeys.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			if (!pluginKeys.containsKey(entry.getKey())) {
				reportProblem("Unused property key", (Location) entry.getValue(), UNUSED_KEY_VIOLATION, false);
			}
		}
		monitor.done();
	}

	public static boolean deleteAuditMarkers(IProject project) {
		try {
			project.deleteMarkers(MARKER_ID, false, IResource.DEPTH_INFINITE);
			return true;
		} catch (CoreException e) {
			FavoritesLog.logError(e);
			return false;
		}
	}

	private boolean checkCancel(IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			// Discard build state if necessary.
			throw new OperationCanceledException();
		}

		if (isInterrupted()) {
			// Discard build state if necessary.
			return true;
		}
		return false;
	}

//		The auditPluginManifest() method delegates scanning the plugin.xml and plugin.properties to two separate scan methods.

	private Map scanPlugin(IFile file) {
		Map keys = new HashMap();
		String content = readFile(file);
		int start = 0;
		while (true) {
			start = content.indexOf("\"%", start);
			if (start < 0) {
				break;
			}
			int end = content.indexOf('"', start + 2);
			if (end < 0) {
				break;
			}
			Location loc = new Location();
			loc.file = file;
			loc.key = content.substring(start + 2, end);
			loc.charStart = start + 1;
			loc.charEnd = end;
			keys.put(loc.key, loc);
			start = end + 1;
		}
		return keys;
	}

	private Map scanProperties(IFile file) {
		Map keys = new HashMap();
		String content = readFile(file);
		int end = 0;
		while (true) {
			end = content.indexOf('=', end);
			if (end < 0) {
				break;
			}
			int start = end - 1;
			while (start >= 0) {
				char ch = content.charAt(start);
				if (ch == '\r' || ch == '\n') {
					break;
				}
				start--;
			}
			start++;
			String found = content.substring(start, end).trim();
			if (found.length() == 0 || found.charAt(0) == '#' || found.indexOf('=') != -1) {
				continue;
			}
			Location loc = new Location();
			loc.file = file;
			loc.key = found;
			loc.charStart = start;
			loc.charEnd = end;
			keys.put(loc.key, loc);
			end++;
		}
		return keys;
	}

//		The following two scan methods read the file content into memory using the readFile() method.

	private String readFile(IFile file) {
		if (!file.exists()) {
			return "";
		}
		InputStream stream = null;
		try {
			stream = file.getContents();
			Reader reader = new BufferedReader(new InputStreamReader(stream));
			StringBuffer result = new StringBuffer(2048);
			char[] buf = new char[2048];
			while (true) {
				int count = reader.read(buf);
				if (count < 0) {
					break;
				}
				result.append(buf, 0, count);
			}
			return result.toString();
		} catch (Exception e) {
			FavoritesLog.logError(e);
			return "";
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				FavoritesLog.logError(e);
				return "";
			}
		}
	}

//		The reportProblem() method appends a message to standard output. In subsequent sections, this method will be enhanced to generate markers instead (see Section 14.2.2, Creating and deleting markers, on page 515).

	private void reportProblem(String msg, Location loc, int violation, boolean isError) {
		try {
			IMarker marker = loc.file.createMarker(MARKER_ID);
			marker.setAttribute(IMarker.MESSAGE, msg + ": " + loc.key);
			marker.setAttribute(IMarker.CHAR_START, loc.charStart);
			marker.setAttribute(IMarker.CHAR_END, loc.charEnd);
			marker.setAttribute(IMarker.SEVERITY, isError ? IMarker.SEVERITY_ERROR : IMarker.SEVERITY_WARNING);
			marker.setAttribute(KEY, loc.key);
			marker.setAttribute(VIOLATION, violation);
		} catch (CoreException e) {
			FavoritesLog.logError(e);
			return;
		}
//		System.out.println(
//				(isError ? "ERROR: " : "WARNING: ") + msg + " \"" + loc.key + "\" in " + loc.file.getFullPath());
	}

//		The Location inner class is defined as an internal data holder with no associated behavior.
	private static class Location {
		IFile file;
		String key;
		int charStart;
		int charEnd;
	}

	public static void addBuilderToProject(IProject project) {

		// Cannot modify closed projects.
		if (!project.isOpen()) {
			return;
		}

		// Get the description.
		IProjectDescription description;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			FavoritesLog.logError(e);
			return;
		}

		// Look for builder already associated.
		ICommand[] cmds = description.getBuildSpec();
		for (ICommand cmd : cmds) {
			if (BUILDER_ID.equals(cmd.getBuilderName())) {
				return;
			}
		}

		// Associate builder with project.
		ICommand newCmd = description.newCommand();
		newCmd.setBuilderName(BUILDER_ID);
		List newCmds = new ArrayList(Arrays.asList(cmds));
		newCmds.add(newCmd);
		description.setBuildSpec((ICommand[]) newCmds.toArray(new ICommand[newCmds.size()]));
		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			FavoritesLog.logError(e);
		}
	}

	public static void removeBuilderFromProject(IProject project) {

		// Cannot modify closed projects.
		if (!project.isOpen()) {
			return;
		}

		// Get the description.
		IProjectDescription description;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			FavoritesLog.logError(e);
			return;
		}

		// Look for builder.
		int index = -1;
		ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
			if (BUILDER_ID.equals(cmds[j].getBuilderName())) {
				index = j;
				break;
			}
		}
		if (index == -1) {
			return;
		}

		// Remove builder from project.
		List newCmds = new ArrayList(Arrays.asList(cmds));
		newCmds.remove(index);
		description.setBuildSpec((ICommand[]) newCmds.toArray(new ICommand[newCmds.size()]));
		try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			FavoritesLog.logError(e);
		}
	}

}
