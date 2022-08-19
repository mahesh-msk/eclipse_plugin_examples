/*
 * FILE:            FavoritesTransfer.java
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import com.ancit.favorite.model.IFavoriteItem;
import com.ancit.favorites.helpers.FavoritesManager;

public class FavoritesTransfer extends ByteArrayTransfer {

	private static final FavoritesTransfer INSTANCE = new FavoritesTransfer();

	public static FavoritesTransfer getInstance() {
		return INSTANCE;
	}

	private FavoritesTransfer() {
	}

	private static final String TYPE_NAME = "favorites-transfer-format:" + System.currentTimeMillis() + ":"
			+ INSTANCE.hashCode();

	private static final int TYPEID = registerType(TYPE_NAME);

	@Override
	protected int[] getTypeIds() {
		return new int[] { TYPEID };
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { TYPE_NAME };
	}

	@Override
	protected void javaToNative(Object data, TransferData transferData) {

		if (!(data instanceof IFavoriteItem[])) {
			return;
		}
		IFavoriteItem[] items = (IFavoriteItem[]) data;

		/**
		 * The serialization format is: (int) number of items Then, the following for
		 * each item: (String) the type of item (String) the item-specific info glob
		 */
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DataOutputStream dataOut = new DataOutputStream(out);
			dataOut.writeInt(items.length);
			for (IFavoriteItem item : items) {
				dataOut.writeUTF(item.getType().getId());
				dataOut.writeUTF(item.getInfo());
			}
			dataOut.close();
			out.close();
			super.javaToNative(out.toByteArray(), transferData);
		} catch (IOException e) {
			// Send nothing if there were problems.
		}
	}

	@Override
	protected Object nativeToJava(TransferData transferData) {
		/**
		 * The serialization format is: (int) number of items Then, the following for
		 * each item: (String) the type of item (String) the item-specific info glob
		 */
		byte[] bytes = (byte[]) super.nativeToJava(transferData);
		if (bytes == null) {
			return null;
		}
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			FavoritesManager mgr = FavoritesManager.getManager();
			int count = in.readInt();
			List items = new ArrayList(count);
			for (int i = 0; i < count; i++) {
				String typeId = in.readUTF();
				String info = in.readUTF();
				items.add(mgr.newFavoriteFor(typeId));
			}
			return items.toArray(new IFavoriteItem[items.size()]);
		} catch (IOException e) {
			return null;
		}
	}

}
