/*
 * FILE:            TreeViewerExample.java
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
package com.ancit.favorites.jface.viewers;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TreeViewerExample {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Tree Viewer Example");
		shell.setBounds(100, 100, 200, 200);
		shell.setLayout(new FillLayout());

		final TreeViewer treeViewer = new TreeViewer(shell, SWT.SINGLE);
		treeViewer.setLabelProvider(new PersonListLabelProvider());
		treeViewer.setContentProvider(new PersonTreeContentProvider());
		treeViewer.setInput(Person.example());

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}