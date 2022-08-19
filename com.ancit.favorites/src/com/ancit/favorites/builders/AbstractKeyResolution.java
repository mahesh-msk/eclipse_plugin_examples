/*
 * FILE:            AbstractKeyResolution.java
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
package com.ancit.favorites.builders;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

public class AbstractKeyResolution implements IMarkerResolution2 {

	private IMarker marker;

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public String getLabel() {
		return "";
	}

//	If the user selects this resolution, the run() method is executed, opening or activating the properties editor and appending a new property key/value pair.

	@Override
	public void run(IMarker marker) {

		// Get the corresponding plugin.properties.
		IFile file = marker.getResource().getParent().getFile(new Path("plugin.properties"));
		if (!file.exists()) {
			ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] {});
			try {
				file.create(stream, false, null);
			} catch (CoreException e) {
				e.printStackTrace();
				return;
			}
		}

		// Open or activate the editor.
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IEditorPart part;
		try {
			part = IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
			e.printStackTrace();
			return;
		}

		// Get the editor's document.
		if (!(part instanceof ITextEditor)) {
			return;
		}

		ITextEditor editor = (ITextEditor) part;
		IDocument doc = editor.getDocumentProvider().getDocument(new FileEditorInput(file));

		// Determine the text to be added.
		String key;
		try {
			key = (String) marker.getAttribute(PropertiesFileAuditor.KEY);
		} catch (CoreException e) {
			e.printStackTrace();
			return;
		}

		String text = key + "=Value for " + key;

		// If necessary, add a newline.
		int index = doc.getLength();
		if (index > 0) {
			char ch;
			try {
				ch = doc.getChar(index - 1);
			} catch (BadLocationException e) {
				e.printStackTrace();
				return;
			}

			if (ch != '\r' || ch != '\n') {
				text = System.lineSeparator() + text;
			}
		}

		// Append the new text.
		try {
			doc.replace(index, 0, text);
		} catch (BadLocationException e) {
			e.printStackTrace();
			return;
		}

		// Select the value so the user can type.
		index += text.indexOf('=') + 1;
		editor.selectAndReveal(index, doc.getLength() - index);
	}
}
