/*
 * FILE:            RenameFavoriteAction.java
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
package com.ancit.favorites.views.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.actions.TextActionHandler;

import com.ancit.favorites.helpers.IFavoriteItem;
import com.ancit.favorites.views.FavoritesView2;

public class RenameFavoriteAction extends Action {
	private static final int COLUMN_TO_EDIT = 1;
	private final FavoritesView2 view;
	private final Table table;
	private final TableEditor tableEditor;
	private final TextActionHandler textActionHandler;
	private Composite editorParent;
	private Text editor;
	private String originalText;

	public RenameFavoriteAction(FavoritesView2 view, Table table, String text) {
		super(text);
		this.view = view;
		this.table = table;
		tableEditor = new TableEditor(table);
		textActionHandler = new TextActionHandler(view.getViewSite().getActionBars());
	}

//	When the
//	action is executed,
//
//	the run() method is called to create and show the cell editor.

	@Override
	public void run() {
		originalText = getTextToEdit();
		if (originalText == null) {
			return;
		}
		if (editor == null) {
			createEditor();
		}
		showEditor(originalText);
	}

	private void createEditor() {
		// Create the parent so that a simple border
		// can be painted around the text editor.
		editorParent = new Composite(table, SWT.NONE);
		TableItem[] tableItems = table.getSelection();
		tableEditor.horizontalAlignment = SWT.LEFT;
		tableEditor.grabHorizontal = true;
		tableEditor.setEditor(editorParent, tableItems[0], COLUMN_TO_EDIT);
		editorParent.setVisible(false);
		editorParent.addListener(SWT.Paint, e -> {
			// Paint a simple border around the text editor.
			Point textSize = editor.getSize();
			Point parentSize = editorParent.getSize();
			int w = Math.min(textSize.x + 4, parentSize.x - 1);
			int h = parentSize.y - 1;
			e.gc.drawRectangle(0, 0, w, h);
		});
		// Create the editor itself.
		editor = new Text(editorParent, SWT.NONE);
		editorParent.setBackground(editor.getBackground());
		editor.addListener(SWT.Modify, e -> {
			Point textSize = editor.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			textSize.x += textSize.y;
			// Add extra space for new characters.
			Point parentSize = editorParent.getSize();
			int w = Math.min(textSize.x, parentSize.x - 4);
			int h = parentSize.y - 2;
			editor.setBounds(2, 1, w, h);
			editorParent.redraw();
		});
		editor.addListener(SWT.Traverse, event -> {
			// Workaround for Bug 20214 due to extra
			// traverse events.
			switch (event.detail) {
			case SWT.TRAVERSE_ESCAPE:
				// Do nothing in this case.
				disposeEditor();
				event.doit = true;
				event.detail = SWT.TRAVERSE_NONE;
				break;
			case SWT.TRAVERSE_RETURN:
				saveChangesAndDisposeEditor();
				event.doit = true;
				event.detail = SWT.TRAVERSE_NONE;
				break;
			}
		});
		editor.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent fe) {
				saveChangesAndDisposeEditor();
			}
		});

		// Add a handler to redirect global cut, copy, etc.
		textActionHandler.addText(editor);
	}

	private void showEditor(String name) {
		editor.setText(name);
		editorParent.setVisible(true);
		Point textSize = editor.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		textSize.x += textSize.y;
		// Add extra space for new characters.
		Point parentSize = editorParent.getSize();
		int w = Math.min(textSize.x, parentSize.x - 4);
		int h = parentSize.y - 2;
		editor.setBounds(2, 1, w, h);
		editorParent.redraw();
		editor.selectAll();
		editor.setFocus();
	}

//	After the
//	user has
//	finished editing
//	the name, the
//	cell editor
//	event listeners
//	call the
//
//	saveChangesAndDisposeEditor() method to save the new name and dispose of the cell editor.

	protected void saveChangesAndDisposeEditor() {
		String newText = editor.getText();
		if (!originalText.equals(newText)) {
			saveChanges(newText);
		}
		disposeEditor();
	}

	protected void disposeEditor() {
		textActionHandler.removeText(editor);
		if (editorParent != null) {
			editorParent.dispose();
			editorParent = null;
			editor = null;
			tableEditor.setEditor(null, null, COLUMN_TO_EDIT);
		}
	}

	protected String getTextToEdit() {
		String text = null;
		IFavoriteItem[] items = view.getSelectedFavorites();
		if (items.length == 1) {
			text = items[0].getName();
		}
		return text;
	}

	protected void saveChanges(String newText) {
		IFavoriteItem[] items = view.getSelectedFavorites();
		if (items.length == 1) {
			items[0].setName(newText);
			view.getFavoritesViewer().refresh(items[0]);
		}
	}
}
