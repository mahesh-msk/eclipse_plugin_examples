/*
 * FILE:            CutFavoritesAction.java
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

public class CutFavoritesAction extends Action {
	private CopyFavoritesAction copyAction;
	private RemoveFavoritesAction removeAction;

	public CutFavoritesAction(CopyFavoritesAction copyAction, RemoveFavoritesAction removeAction, String text) {
		super(text);
		this.copyAction = copyAction;
		this.removeAction = removeAction;
	}

	public void run() {
		copyAction.run();
		removeAction.run();
	}
}