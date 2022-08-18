/*
 * FILE:            RemoveFavoritesAction.java
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

import com.ancit.favorites.helpers.FavoritesManager;
import com.ancit.favorites.views.FavoritesView2;

public class RemoveFavoritesAction extends Action {
	private FavoritesView2 view;

	public RemoveFavoritesAction(FavoritesView2 view, String text) {
		super(text);
		this.view = view;
	}

	@Override
	public void run() {
		FavoritesManager.getManager().removeFavorites(view.getSelectedFavorites());
	}
}