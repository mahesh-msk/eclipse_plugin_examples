/*
 * FILE:            FavoriteResource.java
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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

public class FavoriteResource implements IFavoriteItem {
	private FavoriteItemType type;
	private IResource resource;
	private String name;

	FavoriteResource(FavoriteItemType type, IResource resource) {
		this.type = type;
		this.resource = resource;
	}

	public static FavoriteResource loadFavorite(FavoriteItemType type, String info) {
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(info));
		if (res == null) {
			return null;
		}
		return new FavoriteResource(type, res);
	}

	@Override
	public String getName() {
		if (name == null) {
			name = resource.getName();
		}
		return name;
	}

	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public String getLocation() {
		IPath path = resource.getLocation().removeLastSegments(1);
		if (path.segmentCount() == 0) {
			return "";
		}
		return path.toString();
	}

	@Override
	public boolean isFavoriteFor(Object obj) {
		return resource.equals(obj);
	}

	@Override
	public FavoriteItemType getType() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof FavoriteResource && resource.equals(((FavoriteResource) obj).resource);
	}

	@Override
	public int hashCode() {
		return resource.hashCode();
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.isInstance(resource)) {
			return resource;
		}
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public String getInfo() {
		return resource.getFullPath().toString();
	}
}
