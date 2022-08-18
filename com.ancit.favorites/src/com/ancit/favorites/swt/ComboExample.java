/*
 * FILE:            ComboExample.java
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ComboExample {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Combo Example");
		shell.setBounds(100, 100, 200, 100);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		final Combo combo1 = new Combo(shell, SWT.READ_ONLY);
		final Combo combo2 = new Combo(shell, SWT.DROP_DOWN);
		final Label label = new Label(shell, SWT.CENTER);
		combo1.setItems("First", "Second", "Third");
		combo1.setText("First");
		combo1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				label.setText("Selected: " + combo1.getText());
			}
		});
		combo2.setItems("First", "Second", "Third");
		combo2.setText("First");
		combo2.addModifyListener(event -> label.setText("Entered: " + combo2.getText()));
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}