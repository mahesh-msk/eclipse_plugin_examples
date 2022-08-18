/*
 * FILE:            AddToFavoritesActionDelegate.java
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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.ancit.favorites.helpers.FavoritesManager;

public class AddToFavoritesActionDelegate implements IObjectActionDelegate {

//	private IWorkbenchPart targetPart;
	private ISelection selection;

	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			FavoritesManager mgr = FavoritesManager.getManager();
			Iterator iter = ((IStructuredSelection) selection).iterator();
			mgr.addFavorites(mgr.newFavoritesFor(iter));
		}

	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
		action.setEnabled(!selection.isEmpty());
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
//		this.targetPart = targetPart;

	}

}
