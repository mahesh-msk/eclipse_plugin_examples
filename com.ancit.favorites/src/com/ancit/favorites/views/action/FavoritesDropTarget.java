/*
 * FILE:            FavoritesDropTarget.java
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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;

import com.ancit.favorites.helpers.FavoritesManager;
import com.ancit.favorites.views.FavoritesView2;

public class FavoritesDropTarget extends DropTargetAdapter {
	public FavoritesDropTarget(FavoritesView2 view, TableViewer viewer) {
		DropTarget target = new DropTarget(viewer.getControl(), DND.DROP_MOVE | DND.DROP_COPY);
		target.setTransfer(FavoritesTransfer.getInstance(), JavaUI.getJavaElementClipboardTransfer());
		target.addDropListener(this);
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		if (event.detail == DND.DROP_MOVE || event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}
	}

	@Override
	public void drop(DropTargetEvent event) {
		FavoritesManager mgr = FavoritesManager.getManager();
		if (FavoritesTransfer.getInstance().isSupportedType(event.currentDataType)
				&& event.data instanceof IResource[]) {
			mgr.addFavorites(mgr.newFavoritesFor((IResource[]) event.data));
			event.detail = DND.DROP_COPY;
		} else if (JavaUI.getJavaElementClipboardTransfer().isSupportedType(event.currentDataType)
				&& event.data instanceof IJavaElement[]) {
			mgr.addFavorites(mgr.newFavoritesFor((IJavaElement[]) event.data));
			event.detail = DND.DROP_COPY;
		} else {
			event.detail = DND.DROP_NONE;
		}
	}
}