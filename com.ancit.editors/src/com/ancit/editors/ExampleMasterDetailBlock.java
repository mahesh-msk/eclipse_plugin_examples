/*
 * FILE:            ExampleMasterDetailBlock.java
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
package com.ancit.editors;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class ExampleMasterDetailBlock extends MasterDetailsBlock {

	@Override
	protected void createMasterPart(IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR);
		section.setDescription("Model Details");
		section.marginWidth = 100;
		section.marginHeight = 5;
		Composite composite = toolkit.createComposite(section, SWT.WRAP);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		composite.setLayout(gridLayout);
		Table table = toolkit.createTable(composite, SWT.NULL);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 20;
		gridData.widthHint = 100;
		table.setLayout(gridLayout);
		toolkit.paintBordersFor(composite);
		Button button = toolkit.createButton(composite, "Click", SWT.PUSH);
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		button.setLayoutData(gridLayout);
		section.setClient(composite);
		SectionPart sectionPart = new SectionPart(section);
		managedForm.addPart(sectionPart);

		TableViewer tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ArrayContentProvider());
		ArrayList<AddressBook> books = new ArrayList<>();
		AddressBook addressBook = new AddressBook();
		addressBook.setName("Hello");
		books.add(addressBook);
		tableViewer.setInput(addressBook);

		tableViewer.addSelectionChangedListener(event -> {
			managedForm.fireSelectionChanged(sectionPart, event.getSelection());
		});

	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(AddressBook.class, new AddressBookDetailsPage());
	}

	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// TODO Auto-generated method stub

	}

}
