/*
 * FILE:            PasteFavoritesAction.java
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

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.dnd.Transfer;

import com.ancit.favorites.helpers.FavoritesManager;
import com.ancit.favorites.views.FavoritesView2;

public class PasteFavoritesAction extends Action {
	private FavoritesView2 view;

	public PasteFavoritesAction(FavoritesView2 view, String text) {
		super(text);
		this.view = view;
	}

	@Override
	public void run() {
		if (pasteResources()) {
			return;
		}
		if (pasteJavaElements()) {
		}
	}

	private boolean pasteResources() {
		FavoritesTransfer transfer = FavoritesTransfer.getInstance();
		IResource[] resources = (IResource[]) view.getClipboard().getContents(transfer);
		if (resources == null || resources.length == 0) {
			return false;
		}
		FavoritesManager mgr = FavoritesManager.getManager();
		mgr.addFavorites(mgr.newFavoritesFor(resources));
		return true;
	}

	private boolean pasteJavaElements() {
		Transfer transfer = JavaUI.getJavaElementClipboardTransfer();
		IJavaElement[] elements = (IJavaElement[]) view.getClipboard().getContents(transfer);
		if (elements == null || elements.length == 0) {
			return false;
		}
		FavoritesManager mgr = FavoritesManager.getManager();
		mgr.addFavorites(mgr.newFavoritesFor(elements));
		return true;
	}
}