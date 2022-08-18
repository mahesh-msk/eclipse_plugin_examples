/*
 * FILE:            TreeExample.java
 *
 * SW-COMPONENT:    com.ancit.favorites
 *
 * DESCRIPTION:     -
 *
 * COPYRIGHT:       � 2015 - 2022 Robert Bosch GmbH
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeExample {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Tree Example");
		shell.setBounds(100, 100, 200, 200);
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.SINGLE);
		for (int i = 1; i < 4; i++) {
			TreeItem grandParent = new TreeItem(tree, 0);
			grandParent.setText("Grand Parent - " + i);
			for (int j = 1; j < 4; j++) {
				TreeItem parent = new TreeItem(grandParent, 0);
				parent.setText("Parent - " + j);
				for (int k = 1; k < 4; k++) {
					TreeItem child = new TreeItem(parent, 0);
					child.setText("Child - " + k);
				}
			}
		}
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				TreeItem[] selected = tree.getSelection();
				if (selected.length > 0) {
					System.out.println("Selected: " + selected[0].getText());
				}
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