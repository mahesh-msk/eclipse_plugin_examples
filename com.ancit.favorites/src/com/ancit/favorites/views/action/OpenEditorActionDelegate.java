/*
 * FILE:            OpenEditorActionDelegate.java
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
package com.ancit.favorites.views.action;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

public class OpenEditorActionDelegate {

	public static void openEditor(IWorkbenchPage page, ISelection selection) {

		// Get the first element.

		if (!(selection instanceof IStructuredSelection)) {
			return;
		}
		Iterator iter = ((IStructuredSelection) selection).iterator();
		if (!iter.hasNext()) {
			return;
		}
		Object elem = iter.next();
		// Adapt the first element to a file.

		if (!(elem instanceof IAdaptable)) {
			return;
		}

		IFile file = ((IAdaptable) elem).getAdapter(IFile.class);
		if (file == null) {
			return;
		}

		// Open an editor on that file.

		try {
			IDE.openEditor(page, file);
		} catch (PartInitException e) {
			System.err.println("Open editor failed: " + file.toString());
		}
	}

}
