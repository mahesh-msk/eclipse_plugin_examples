/*
 * FILE:            TableViewerExample.java
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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TableViewerExample {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Table Viewer Example");
		shell.setBounds(100, 100, 325, 200);
		shell.setLayout(new FillLayout());

		final TableViewer tableViewer = new TableViewer(shell, SWT.SINGLE | SWT.FULL_SELECTION);
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		String[] columnNames = { "First Name", "Last Name", "Age", "Num Children" };
		int[] columnWidths = { 100, 100, 35, 75 };
		int[] columnAlignments = { SWT.LEFT, SWT.LEFT, SWT.CENTER, SWT.CENTER };
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(table, columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
		}

		tableViewer.setLabelProvider(new PersonTableLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(Person.example());

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}