/*
 * FILE:            CopyFavoritesAction.java
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import com.ancit.favorite.model.IFavoriteItem;
import com.ancit.favorites.views.FavoritesView2;

public class CopyFavoritesAction extends Action {
	private FavoritesView2 view;

	public CopyFavoritesAction(FavoritesView2 view, String text) {
		super(text);
		this.view = view;
	}

	@Override
	public void run() {
		IFavoriteItem[] items = view.getSelectedFavorites();
		if (items.length == 0) {
			return;
		}
		try {
			view.getClipboard().setContents(new Object[] { asResources(items), asText(items), },
					new Transfer[] { FavoritesTransfer.getInstance(), TextTransfer.getInstance(), });
		} catch (SWTError error) {
			// Copy to clipboard failed.
			// This happens when another application
			// is accessing the clipboard while we copy.
			// Ignore the error.
		}
	}

	public static IResource[] asResources(IFavoriteItem[] items) {
		List resources = new ArrayList();
		for (IFavoriteItem item : items) {
			IResource res = item.getAdapter(IResource.class);
			if (res != null) {
				resources.add(res);
			}
		}
		return (IResource[]) resources.toArray(new IResource[resources.size()]);
	}

	public static String asText(IFavoriteItem[] items) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < items.length; i++) {
			if (i > 0) {
				buf.append(System.lineSeparator());
			}
			buf.append(items[i].getName());
		}
		return buf.toString();
	}
}
