/*
 * FILE:            PropertiesEditor.java
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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;

public class PropertiesEditor extends MultiPageEditorPart {

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof IFileEditorInput)) {
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		}
		super.init(site, input);
	}

//		Next, add two fields plus methods to create the Source and Properties pages.

	private TreeViewer treeViewer;
	private TextEditor textEditor;

	@Override
	protected void createPages() {
		createPropertiesPage();
		createSourcePage();
		updateTitle();
	}

	void createPropertiesPage() {
		treeViewer = new TreeViewer(getContainer(), SWT.MULTI | SWT.FULL_SELECTION);

		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);

		TreeColumn keyColumn = new TreeColumn(tree, SWT.NONE);
		keyColumn.setText("Key");
		keyColumn.setWidth(150);

		TreeColumn valueColumn = new TreeColumn(tree, SWT.NONE);
		valueColumn.setText("Value");
		valueColumn.setWidth(150);

		int index = addPage(tree);
		setPageText(index, "Properties");
	}

	void createSourcePage() {
		try {
			textEditor = new TextEditor();
			int index = addPage(textEditor, getEditorInput());
			setPageText(index, "Source");
		} catch (PartInitException e) {
			System.err.println("Error creating nested text editor");
		}
	}

	void updateTitle() {
		IEditorInput input = getEditorInput();
		setPartName(input.getName());
		setTitleToolTip(input.getToolTipText());
	}

//		When the focus shifts to the editor, the setFocus() method is called; it must then redirect focus to the appropriate editor based on which page is currently selected.

	@Override
	public void setFocus() {
		switch (getActivePage()) {
		case 0:
			treeViewer.getTree().setFocus();
			break;
		case 1:
			textEditor.setFocus();
			break;
		}
	}

//		When the user directly or indirectly requests that a marker be revealed, ensure that the Source page is active then redirect the request to the text editor. You could do something different when the Properties page is active, but that would require additional editor model infrastructure.

	public void gotoMarker(IMarker marker) {
		setActivePage(1);
		textEditor.getAdapter(IGotoMarker.class).gotoMarker(marker);
	}

//		Three methods are involved in saving editor content. If the isSaveAsAllowed() method returns false, then the doSaveAs() method is never called.

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		textEditor.doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		textEditor.doSaveAs();
		setInput(textEditor.getEditorInput());
		updateTitle();
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub

	}

}
