/*
 * FILE:            ListViewerExample.java
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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ListViewerExample {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("List Viewer Example");
		shell.setBounds(100, 100, 200, 100);
		shell.setLayout(new FillLayout());
		final ListViewer listViewer = new ListViewer(shell, SWT.SINGLE);
		listViewer.setLabelProvider(new PersonListLabelProvider());
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setInput(Person.example());
		listViewer.setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object p1, Object p2) {
				return ((Person) p1).lastName.compareToIgnoreCase(((Person) p2).lastName);
			}
		});
		listViewer.addSelectionChangedListener(event -> {
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			System.out.println("Selected: " + selection.getFirstElement());
		});
		listViewer.addDoubleClickListener(event -> {
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			System.out.println("Double Clicked: " + selection.getFirstElement());
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