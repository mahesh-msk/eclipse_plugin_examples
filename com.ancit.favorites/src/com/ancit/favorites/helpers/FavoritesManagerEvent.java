/*
 * FILE:            FavoritesManagerEvent.java
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

import java.util.EventObject;

import com.ancit.favorite.model.IFavoriteItem;

public class FavoritesManagerEvent extends EventObject {
	private static final long serialVersionUID = 3697053173951102953L;

	private final IFavoriteItem[] added;
	private final IFavoriteItem[] removed;

	public FavoritesManagerEvent(FavoritesManager source, IFavoriteItem[] itemsAdded, IFavoriteItem[] itemsRemoved) {
		super(source);
		added = itemsAdded;
		removed = itemsRemoved;
	}

	public IFavoriteItem[] getItemsAdded() {
		return added;
	}

	public IFavoriteItem[] getItemsRemoved() {
		return removed;
	}
}
