/*
 * FILE:            ResizableDialog.java
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
package com.ancit.favorites.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

public abstract class ResizableDialog extends Dialog {

	protected Rectangle cachedBounds;

	private static final String TAG_X = "x";
	private static final String TAG_Y = "y";
	private static final String TAG_WIDTH = "width";
	private static final String TAG_HEIGHT = "height";

	public ResizableDialog(Shell parentShell) {
		super(parentShell);
	}

	public ResizableDialog(IShellProvider parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	}

//		Next, to preserve the size and location of the dialog across invocations, subclasses of this new class must supply a location in which to store values. For more about IDialogSettings see Section 11.2.7, Dialog settings, on page 441.

	protected abstract IDialogSettings getDialogSettings();

//		Methods for loading the bounds from the dialog settings and saving the bounds into the dialog settings are neeed, as follows:

	private Rectangle loadBounds() {
		IDialogSettings settings = getDialogSettings();
		try {
			return new Rectangle(settings.getInt(TAG_X), settings.getInt(TAG_Y), settings.getInt(TAG_WIDTH),
					settings.getInt(TAG_HEIGHT));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private void saveBounds(Rectangle bounds) {
		IDialogSettings settings = getDialogSettings();
		settings.put(TAG_X, bounds.x);
		settings.put(TAG_Y, bounds.y);
		settings.put(TAG_WIDTH, bounds.width);
		settings.put(TAG_HEIGHT, bounds.height);
	}

//		You need to override the getInitialLocation() and getInitialSize() methods so that, when the dialog is first opened, its prior location and size are restored.

	@Override
	protected Point getInitialSize() {

		// Track the current dialog bounds.
		getShell().addControlListener(new ControlListener() {
			@Override
			public void controlMoved(ControlEvent arg0) {
				cachedBounds = getShell().getBounds();
			}

			@Override
			public void controlResized(ControlEvent arg0) {
				cachedBounds = getShell().getBounds();
			}
		});

		// Answer the size from the previous incarnation.
		Rectangle b1 = getShell().getDisplay().getBounds();
		Rectangle b2 = loadBounds();
		if (b2 != null) {
			return new Point(b1.width < b2.width ? b1.width : b2.width, b1.height < b1.height ? b2.height : b2.height);
		}

		return super.getInitialSize();
	}

	@Override
	protected Point getInitialLocation(Point initialSize) {

		// Answer the location from the previous incarnation.
		Rectangle displayBounds = getShell().getDisplay().getBounds();
		Rectangle bounds = loadBounds();
		if (bounds != null) {
			int x = bounds.x;
			int y = bounds.y;
			int maxX = displayBounds.x + displayBounds.width - initialSize.x;
			int maxY = displayBounds.y + displayBounds.height - initialSize.y;
			if (x > maxX) {
				x = maxX;
			}
			if (y > maxY) {
				y = maxY;
			}
			if (x < displayBounds.x) {
				x = displayBounds.x;
			}
			if (y < displayBounds.y) {
				y = displayBounds.y;
			}
			return new Point(x, y);
		}
		return super.getInitialLocation(initialSize);
	}

//		Finally, override the close method to save the dialog bounds for future incarnations:

	@Override
	public boolean close() {
		boolean closed = super.close();
		if (closed && cachedBounds != null) {
			saveBounds(cachedBounds);
		}
		return closed;
	}

}
