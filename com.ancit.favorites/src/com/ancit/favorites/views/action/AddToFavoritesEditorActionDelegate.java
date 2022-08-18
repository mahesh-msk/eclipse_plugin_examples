/*
 * FILE:            AddToFavoritesEditorActionDelegate.java
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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

public class AddToFavoritesEditorActionDelegate implements IEditorActionDelegate {

	private IWorkbenchPart targetPart;

	@Override
	public void run(IAction action) {
		MessageDialog.openInformation(targetPart.getSite().getShell(), "Add to Favorites",
				"Triggered the " + getClass().getName() + " action");

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetPart = targetEditor;

	}

}
