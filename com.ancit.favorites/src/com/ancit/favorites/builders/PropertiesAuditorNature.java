/*
 * FILE:            PropertiesAuditorNature.java
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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.ancit.favorites.logging.FavoritesLog;

public class PropertiesAuditorNature implements IProjectNature {
	private IProject project;

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

	@Override
	public void configure() throws CoreException {
		PropertiesFileAuditor.addBuilderToProject(project);
		new Job("Properties File Audit") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					project.build(PropertiesFileAuditor.FULL_BUILD, PropertiesFileAuditor.BUILDER_ID, null, monitor);
				} catch (CoreException e) {
					FavoritesLog.logError(e);
				}
				return Status.OK_STATUS;
			}
		}.schedule();
	}

	@Override
	public void deconfigure() throws CoreException {
		PropertiesFileAuditor.removeBuilderFromProject(project);
		PropertiesFileAuditor.deleteAuditMarkers(project);
	}
}