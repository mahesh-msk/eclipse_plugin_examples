/*
 * FILE:            FreeFormPage.java
 *
 * SW-COMPONENT:    com.ancit.editors
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
package com.ancit.editors.formpages;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import com.ancit.editors.Activator;

/**
 * @author dejan
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class FreeFormPage extends FormPage {
	/**
	 * @param id
	 * @param title
	 */
	public FreeFormPage(FormEditor editor) {
		super(editor, "first", Messages.getString("FreeFormPage.label")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText(Messages.getString("FreeFormPage.title")); //$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));
		TableWrapLayout layout = new TableWrapLayout();
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		form.getBody().setLayout(layout);
		TableWrapData td;
		Hyperlink link = toolkit.createHyperlink(form.getBody(), Messages.getString("FreeFormPage.link"), SWT.WRAP); //$NON-NLS-1$
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
				}
			}
		});
		td = new TableWrapData();
		td.align = TableWrapData.LEFT;
		link.setLayoutData(td);
		createExpandable(form, toolkit);
		createFormTextSection(form, toolkit);
	}

	private void createExpandable(final ScrolledForm form, final FormToolkit toolkit) {
		final ExpandableComposite exp = toolkit.createExpandableComposite(form.getBody(), ExpandableComposite.TREE_NODE
		// ExpandableComposite.NONE
		);
		exp.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		exp.setToggleColor(toolkit.getColors().getColor(FormColors.SEPARATOR));
		Composite client = toolkit.createComposite(exp);
		exp.setClient(client);
		TableWrapLayout elayout = new TableWrapLayout();
		client.setLayout(elayout);
		elayout.leftMargin = elayout.rightMargin = 0;
		final Button button = toolkit.createButton(client, Messages.getString("FreeFormPage.button"), SWT.PUSH); //$NON-NLS-1$
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// openFormWizard(button.getShell(), toolkit.getColors());
			}
		});
		exp.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		exp.setText(Messages.getString("FreeFormPage.sectionTitle1")); //$NON-NLS-1$
		TableWrapData td = new TableWrapData();
		// td.colspan = 2;
		td.align = TableWrapData.LEFT;
		// td.align = TableWrapData.FILL;
		exp.setLayoutData(td);
	}

	private void createFormTextSection(final ScrolledForm form, FormToolkit toolkit) {
		Section section = toolkit.createSection(form.getBody(), Section.TWISTIE | Section.DESCRIPTION);
		section.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		section.setToggleColor(toolkit.getColors().getColor(FormColors.SEPARATOR));
		toolkit.createCompositeSeparator(section);
		FormText rtext = toolkit.createFormText(section, false);
		section.setClient(rtext);
		loadFormText(rtext, toolkit);

		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(false);
			}
		});

		section.setText(Messages.getString("FreeFormPage.sectionTitle2")); //$NON-NLS-1$
		section.setDescription(Messages.getString("FreeFormPage.sectionDescription")); //$NON-NLS-1$
		TableWrapData td = new TableWrapData();
		td.align = TableWrapData.FILL;
		td.grabHorizontal = true;
		section.setLayoutData(td);
	}

	private void loadFormText(final FormText rtext, FormToolkit toolkit) {
		rtext.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				MessageDialog.openInformation(rtext.getShell(), Messages.getString("FreeFormPage.mtitle"), //$NON-NLS-1$
						Messages.getString("FreeFormPage.mtext") + e.getHref()); //$NON-NLS-1$
			}
		});
		rtext.setHyperlinkSettings(toolkit.getHyperlinkGroup());
		rtext.setImage("image1", Activator.getDefault().getImage(Activator.IMG_LARGE)); //$NON-NLS-1$
		InputStream is = FreeFormPage.class.getResourceAsStream("index.xml"); //$NON-NLS-1$
		if (is != null) {
			rtext.setContents(is, true);
			try {
				is.close();
			} catch (IOException e) {
			}
		}
	}
}