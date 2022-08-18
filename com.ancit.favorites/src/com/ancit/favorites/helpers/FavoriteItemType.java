/*
 * FILE:            FavoriteItemType.java
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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public abstract class FavoriteItemType implements Comparable {

	private static final ISharedImages PLATFORM_IMAGES = PlatformUI.getWorkbench().getSharedImages();

	private final String id;
	private final String printName;
	private final int ordinal;

	private FavoriteItemType(String id, String name, int position) {
		this.id = id;
		this.ordinal = position;
		this.printName = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return printName;
	}

	public abstract Image getImage();

	public abstract IFavoriteItem newFavorite(Object obj);

	public abstract IFavoriteItem loadFavorite(String info);

	@Override
	public int compareTo(Object arg) {
		return this.ordinal - ((FavoriteItemType) arg).ordinal;
	}

	public static final FavoriteItemType UNKNOWN = new FavoriteItemType("Unknown", "Unknown", 0) {
		@Override
		public Image getImage() {
			return null;
		}

		@Override
		public IFavoriteItem newFavorite(Object obj) {
			return null;
		}

		@Override
		public IFavoriteItem loadFavorite(String info) {
			return null;
		}
	};

	public static final FavoriteItemType WORKBENCH_FILE = new FavoriteItemType("WBFile", "Workbench File", 1) {
		@Override
		public Image getImage() {
			return PLATFORM_IMAGES.getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FILE);
		}

		@Override
		public IFavoriteItem newFavorite(Object obj) {
			if (!(obj instanceof IFile)) {
				return null;
			}
			return new FavoriteResource(this, (IFile) obj);
		}

		@Override
		public IFavoriteItem loadFavorite(String info) {
			return FavoriteResource.loadFavorite(this, info);
		}
	};

	public static final FavoriteItemType WORKBENCH_FOLDER = new FavoriteItemType("WBFolder", "Workbench Folder", 2) {
		@Override
		public Image getImage() {
			return PLATFORM_IMAGES.getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
		}

		@Override
		public IFavoriteItem newFavorite(Object obj) {
			if (!(obj instanceof IFolder)) {
				return null;
			}
			return new FavoriteResource(this, (IFolder) obj);
		}

		@Override
		public IFavoriteItem loadFavorite(String info) {
			return FavoriteResource.loadFavorite(this, info);
		}
	};

	private static final FavoriteItemType[] TYPES = { UNKNOWN, WORKBENCH_FILE, WORKBENCH_FOLDER };

	public static FavoriteItemType[] getTypes() {
		return TYPES;
	}

}
