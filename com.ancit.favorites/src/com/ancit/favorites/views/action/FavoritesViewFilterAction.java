/*
 * FILE:            FavoritesViewFilterAction.java
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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Shell;

import com.ancit.favorites.helpers.FavoritesViewNameFilter;

public class FavoritesViewFilterAction extends Action {
	private final Shell shell;
	private final FavoritesViewNameFilter nameFilter;

	public FavoritesViewFilterAction(StructuredViewer viewer, String text) {
		super(text);
		shell = viewer.getControl().getShell();
		nameFilter = new FavoritesViewNameFilter(viewer);
	}

	public void run() {
		InputDialog dialog = new InputDialog(
				shell, "Favorites View Filter", "Enter a name filter pattern" + " (* = any string, ? = any character)"
						+ System.lineSeparator() + "or an empty string for no filtering:",
				nameFilter.getPattern(), null);
		if (dialog.open() == InputDialog.OK) {
			nameFilter.setPattern(dialog.getValue().trim());
		}
	}
}