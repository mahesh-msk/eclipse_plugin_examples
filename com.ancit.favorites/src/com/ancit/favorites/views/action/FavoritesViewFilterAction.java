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

import com.ancit.bosch.filters.FavoritesViewLocationFilter;
import com.ancit.bosch.filters.FavoritesViewNameFilter;
import com.ancit.bosch.filters.FavoritesViewTypeFilter;
import com.ancit.favorites.dialog.FavoritesFilterDialog;

public class FavoritesViewFilterAction extends Action {
	private final Shell shell;
	private final FavoritesViewNameFilter nameFilter;
	private final FavoritesViewLocationFilter locationFilter;
	private final FavoritesViewTypeFilter typeFilter;

	public FavoritesViewFilterAction(StructuredViewer viewer, String text) {
		super(text);
		shell = viewer.getControl().getShell();
		nameFilter = new FavoritesViewNameFilter(viewer);
		locationFilter = new FavoritesViewLocationFilter(viewer);
		typeFilter = new FavoritesViewTypeFilter(viewer);
	}

	@Override
	public void run() {
		FavoritesFilterDialog dialog = new FavoritesFilterDialog(shell, nameFilter.getPattern(), typeFilter.getTypes(),
				locationFilter.getPattern());
		if (dialog.open() != InputDialog.OK) {
			return;
		}
		nameFilter.setPattern(dialog.getNamePattern());
		locationFilter.setPattern(dialog.getLocationPattern());
		typeFilter.setPattern(dialog.getSelectedTypes().toString());
	}
}