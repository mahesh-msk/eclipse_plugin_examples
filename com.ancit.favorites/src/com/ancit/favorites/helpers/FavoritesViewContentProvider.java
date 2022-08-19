/*
 * FILE:            FavoritesViewContentProvider.java
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
package com.ancit.favorites.helpers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

public class FavoritesViewContentProvider implements IStructuredContentProvider, FavoritesManagerListener {
	private TableViewer viewer;
	private FavoritesManager manager;

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TableViewer) viewer;
		if (manager != null) {
			manager.removeFavoritesManagerListener(this);
		}
		manager = (FavoritesManager) newInput;
		if (manager != null) {
			manager.addFavoritesManagerListener(this);
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object parent) {
		return manager.getFavorites();
	}

	@Override
	public void favoritesChanged(FavoritesManagerEvent event) {
		// If this is the UI thread, then make the change.
		if (Display.getCurrent() != null) {
			updateViewer(event);
		}

		// otherwise, redirect to execute on the UI thread.
		Display.getDefault().asyncExec(() -> updateViewer(event));
	}

	private void updateViewer(final FavoritesManagerEvent event) {
		// Use the setRedraw method to reduce flicker
		// when adding or removing multiple items in a table.
		viewer.getTable().setRedraw(false);
		try {
			viewer.remove(event.getItemsRemoved());
			viewer.add(event.getItemsAdded());
		} finally {
			viewer.getTable().setRedraw(true);
		}
	}
}