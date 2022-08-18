/*
 * FILE:            MenuExample.java
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
package com.ancit.favorites.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MenuExample {
	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Menu Example");
		shell.setBounds(100, 100, 200, 100);
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		MenuItem fileMenu = new MenuItem(bar, SWT.CASCADE);
		fileMenu.setText("&File");
		Menu subMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenu.setMenu(subMenu);
		MenuItem selectItem = new MenuItem(subMenu, SWT.NULL);
		selectItem.setText("&Select Me Now\tCtrl+S");
		selectItem.setAccelerator(SWT.CTRL + 'S');
		selectItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				System.out.println("I was selected!");
			}
		});
		MenuItem sep = new MenuItem(subMenu, SWT.SEPARATOR);
		MenuItem exitItem = new MenuItem(subMenu, SWT.NULL);
		exitItem.setText("&Exit");
		exitItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				shell.dispose();
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}