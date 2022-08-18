/*
 * FILE:            FavoritesViewLabelProvider.java
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

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

class FavoritesViewLabelProvider extends LabelProvider implements ITableLabelProvider {
	public String getColumnText(Object obj, int index) {
		switch (index) {
		case 0: // Type column
			return "";

		case 1: // Name column
			if (obj instanceof IFavoriteItem) {
				return ((IFavoriteItem) obj).getName();
			}
			if (obj != null) {
				return obj.toString();
			}
			return "";
		case 2: // Location column
			if (obj instanceof IFavoriteItem) {
				return ((IFavoriteItem) obj).getLocation();
			}
			return "";
		default:
			return "";
		}
	}

	public Image getColumnImage(Object obj, int index) {
		if (index == 0 && obj instanceof IFavoriteItem) {
			return ((IFavoriteItem) obj).getType().getImage();
		}
		return null;
	}
}