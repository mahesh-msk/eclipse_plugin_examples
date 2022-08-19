/*
 * FILE:            FavoritesPerspectiveFactory.java
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
package com.ancit.favorites.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class FavoritesPerspectiveFactory implements IPerspectiveFactory {

	private static final String FAVORITES_VIEW_ID = "com.ancit.favorites.views.FavoritesView";
	private static final String FAVORITES_ACTION_ID = "com.ancit.favorites.workbenchActionSet";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// Get the editor area.
		String editorArea = layout.getEditorArea();

		// Put the Outline view on the left.
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.LEFT, 0.25f, editorArea);

		// Put the Favorites view on the bottom with
		// the Tasks view.
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.66f, editorArea);
		bottom.addView(FAVORITES_VIEW_ID);
		bottom.addView(IPageLayout.ID_TASK_LIST);
		bottom.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW);

		// Add the Favorites action set.
		layout.addActionSet(FAVORITES_ACTION_ID);
	}

}
