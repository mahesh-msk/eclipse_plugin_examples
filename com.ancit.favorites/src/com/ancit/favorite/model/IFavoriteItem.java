/*
 * FILE:            IFavoriteItem.java
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
package com.ancit.favorite.model;

import org.eclipse.core.runtime.IAdaptable;

import com.ancit.favorites.helpers.FavoriteItemType;

public interface IFavoriteItem extends IAdaptable {
	String getName();

	void setName(String newName);

	String getLocation();

	boolean isFavoriteFor(Object obj);

	FavoriteItemType getType();

	String getInfo();

	IFavoriteItem[] NONE = {};
}