/*
 * FILE:            FavoritesManager.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.ancit.favorite.model.IFavoriteItem;

public class FavoritesManager implements IResourceChangeListener {
	private static FavoritesManager manager;
	private Collection favorites;

	private FavoritesManager() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}

	private List listeners = new ArrayList();

	public void addFavoritesManagerListener(FavoritesManagerListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeFavoritesManagerListener(FavoritesManagerListener listener) {
		listeners.remove(listener);
	}

	private void fireFavoritesChanged(IFavoriteItem[] itemsAdded, IFavoriteItem[] itemsRemoved) {
		FavoritesManagerEvent event = new FavoritesManagerEvent(this, itemsAdded, itemsRemoved);
		for (Object listener : listeners) {
			((FavoritesManagerListener) listener).favoritesChanged(event);
		}
	}

	public static FavoritesManager getManager() {
		if (manager == null) {
			manager = new FavoritesManager();
		}
		return manager;
	}

	public IFavoriteItem[] getFavorites() {
		if (favorites == null) {
			loadFavorites();
		}
		return (IFavoriteItem[]) favorites.toArray(new IFavoriteItem[favorites.size()]);
	}

	private void loadFavorites() {
		// temporary implementation
		// to prepopulate list with projects
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		favorites = new HashSet(projects.length);
		for (IProject project : projects) {
			favorites.add(new FavoriteResource(FavoriteItemType.WORKBENCH_FOLDER, project));
		}
	}

	public IFavoriteItem newFavoriteFor(Object obj) {
		FavoriteItemType[] types = FavoriteItemType.getTypes();
		for (FavoriteItemType type : types) {
			IFavoriteItem item = type.newFavorite(obj);
			if (item != null) {
				return item;
			}
		}
		return null;
	}

	public IFavoriteItem[] newFavoritesFor(Iterator iter) {
		if (iter == null) {
			return IFavoriteItem.NONE;
		}
		Collection items = new HashSet(20);
		while (iter.hasNext()) {
			IFavoriteItem item = newFavoriteFor(iter.next());
			if (item != null) {
				items.add(item);
			}
		}
		return (IFavoriteItem[]) items.toArray(new IFavoriteItem[items.size()]);
	}

	public IFavoriteItem[] newFavoritesFor(Object[] objects) {
		if (objects == null) {
			return IFavoriteItem.NONE;
		}
		return newFavoritesFor(Arrays.asList(objects).iterator());
	}

	public IFavoriteItem existingFavoriteFor(Object obj) {
		if (obj == null) {
			return null;
		}
		Iterator iter = favorites.iterator();
		while (iter.hasNext()) {
			IFavoriteItem item = (IFavoriteItem) iter.next();
			if (item.isFavoriteFor(obj)) {
				return item;
			}
		}
		return null;
	}

	public IFavoriteItem[] existingFavoritesFor(Iterator iter) {
		List result = new ArrayList(10);
		while (iter.hasNext()) {
			IFavoriteItem item = existingFavoriteFor(iter.next());

			if (item != null) {
				result.add(item);
			}
		}
		return (IFavoriteItem[]) result.toArray(new IFavoriteItem[result.size()]);
	}

	public void addFavorites(IFavoriteItem[] items) {
		if (favorites == null) {
			loadFavorites();
		}
		if (favorites.addAll(Arrays.asList(items))) {
			fireFavoritesChanged(items, IFavoriteItem.NONE);
		}
	}

	public void removeFavorites(IFavoriteItem[] items) {
		if (favorites == null) {
			loadFavorites();
		}
		if (favorites.removeAll(Arrays.asList(items))) {
			fireFavoritesChanged(IFavoriteItem.NONE, items);
		}
	}

	public static void shutdown() {
		if (manager != null) {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(manager);
//			manager.saveFavorites();
			manager = null;
		}
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		System.out.println("FavoritesManager - resource change event");
		try {
//			event.getDelta().accept(delta -> {
//				StringBuffer buf = new StringBuffer(80);
//				switch (delta.getKind()) {
//				case IResourceDelta.ADDED:
//					buf.append("ADDED");
//					break;
//				case IResourceDelta.REMOVED:
//					buf.append("REMOVED");
//					break;
//				case IResourceDelta.CHANGED:
//					buf.append("CHANGED");
//					break;
//				default:
//					buf.append("[");
//					buf.append(delta.getKind());
//					buf.append("]");
//					break;
//				}
//				buf.append(" ");
//				buf.append(delta.getResource());
//				System.out.println(buf);
//				return true;
//			});

			Collection itemsToRemove = new HashSet();
			event.getDelta().accept(delta -> {
				if (delta.getKind() == IResourceDelta.REMOVED) {
					IFavoriteItem item = existingFavoriteFor(delta.getResource());
					if (item != null) {
						itemsToRemove.add(item);
					}
				}
				return true;
			});
			if (itemsToRemove.size() > 0) {
				removeFavorites((IFavoriteItem[]) itemsToRemove.toArray(new IFavoriteItem[itemsToRemove.size()]));
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}

	}

}