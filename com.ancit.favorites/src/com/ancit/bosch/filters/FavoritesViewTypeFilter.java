/*
 * FILE:            FavoritesViewTypeFilter.java
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
package com.ancit.bosch.filters;

import org.eclipse.core.text.StringMatcher;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.ancit.favorite.model.IFavoriteItem;

public class FavoritesViewTypeFilter extends ViewerFilter {
	private final StructuredViewer viewer;
	private String pattern = "";
	private StringMatcher matcher;

	public FavoritesViewTypeFilter(StructuredViewer viewer) {
		this.viewer = viewer;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String newPattern) {
		boolean filtering = matcher != null;
		if (newPattern != null && newPattern.trim().length() > 0) {
			pattern = newPattern;
			matcher = new StringMatcher(pattern, true, false);
			if (!filtering) {
				viewer.addFilter(this);
			} else {
				viewer.refresh();
			}
		} else {
			pattern = "";
			matcher = null;
			if (filtering) {
				viewer.removeFilter(this);
			}
		}
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return matcher.match(((IFavoriteItem) element).getName());
	}

	public String getTypes() {
		return null;
	}
}
