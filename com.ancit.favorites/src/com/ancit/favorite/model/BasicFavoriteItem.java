/*
 * FILE:            BasicFavoriteItem.java
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.ancit.favorites.FavoritesPlugin;

public class BasicFavoriteItem {

	public static final QualifiedName COMMENT_PROPKEY = new QualifiedName(FavoritesPlugin.PLUGIN_ID, "comment");
	private Color color;
	private static final Map colorCache = new HashMap();
	private static Color defaultColor;
	public static final String COMMENT_PREFKEY = "defaultComment";

	public Color getColor() {
		if (color == null) {
			return getDefaultColor();
		}
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public static Color getDefaultColor() {
		if (defaultColor == null) {
			defaultColor = getColor(new RGB(0, 0, 0));
		}
		return defaultColor;
	}

	public static void setDefaultColor(Color color) {
		defaultColor = color;
	}

	public static Color getColor(RGB rgb) {
		Color color = (Color) colorCache.get(rgb);
		if (color == null) {
			Display display = Display.getCurrent();
			color = new Color(display, rgb);
			colorCache.put(rgb, color);
		}
		return color;
	}

	public static void disposeColors() {
		Iterator iter = colorCache.values().iterator();
		while (iter.hasNext()) {
			((Color) iter.next()).dispose();
		}
		colorCache.clear();
	}

	public static String getDefaultComment() {
		return FavoritesPlugin.getDefault().getPluginPreferences().getString(COMMENT_PREFKEY);
	}

	public static void setDefaultComment(String comment) {
		FavoritesPlugin.getDefault().getPluginPreferences().setValue(COMMENT_PREFKEY, comment);
	}
}