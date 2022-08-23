/*
 * FILE:            StudentDetailsDialog.java
 *
 * SW-COMPONENT:    com.ancit.emf.ui
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
package com.ancit.emf.ui.views;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class StudentDetailsDialog extends TitleAreaDialog {

	private Text seatNoTxt;
	private Text studentNameTxt;
	private Text dateOfJoinTxt;

	private int seatNo;
	private String studentName;
	private String dateOfdJoin;

	public StudentDetailsDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Student details Dialog");
		setMessage("This is a TitleAreaDialog", IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createSeatNo(container);
		createStudentName(container);
		createDateOfJoin(container);

		return area;
	}

	private void createSeatNo(Composite container) {
		Label lbtSeatNo = new Label(container, SWT.NONE);
		lbtSeatNo.setText("Seat No");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		seatNoTxt = new Text(container, SWT.BORDER);
		seatNoTxt.setLayoutData(dataFirstName);
	}

	private void createStudentName(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("Student Name");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		studentNameTxt = new Text(container, SWT.BORDER);
		studentNameTxt.setLayoutData(dataFirstName);
	}

	private void createDateOfJoin(Composite container) {
		Label lbtDoj = new Label(container, SWT.NONE);
		lbtDoj.setText("Date Of Join");

		GridData dataDoj = new GridData();
		dataDoj.grabExcessHorizontalSpace = true;
		dataDoj.horizontalAlignment = GridData.FILL;
		dateOfJoinTxt = new Text(container, SWT.BORDER);
		dateOfJoinTxt.setLayoutData(dataDoj);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	private void saveInput() {
		seatNo = Integer.parseInt(seatNoTxt.getText());
		studentName = studentNameTxt.getText();
		dateOfdJoin = dateOfJoinTxt.getText();

	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public int getSeatNo() {
		return seatNo;
	}

	public String getStudentName() {
		return studentName;
	}

	public String getDoj() {
		return dateOfdJoin;
	}
}