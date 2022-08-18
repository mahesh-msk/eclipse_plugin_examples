/*
 * FILE:            FavoritesDragSource.java
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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

import com.ancit.favorites.views.FavoritesView2;

public class FavoritesDragSource implements DragSourceListener {
	private FavoritesView2 view;

	public FavoritesDragSource(FavoritesView2 view, TableViewer viewer) {
		this.view = view;
		DragSource source = new DragSource(viewer.getControl(), DND.DROP_COPY);
		source.setTransfer(TextTransfer.getInstance(), FavoritesTransfer.getInstance());
		source.addDragListener(this);
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		event.doit = view.getSelectedFavorites().length > 0;
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = CopyFavoritesAction.asText(view.getSelectedFavorites());
		} else if (FavoritesTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = CopyFavoritesAction.asResources(view.getSelectedFavorites());
		}
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		// If this was a MOVE operation,
		// then remove the items that were moved.
	}
}