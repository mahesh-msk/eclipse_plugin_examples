/*
 * FILE:            OpenFormEditorAction.java
 *
 * SW-COMPONENT:    com.ancit.editors
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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import com.ancit.editors.formpages.SimpleFormEditorInput;

/**
 * @see IWorkbenchWindowActionDelegate
 */
public class OpenFormEditorAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	@Override
	public void run(IAction action) {
		// Get the active page.
		if (window == null) {
			return;
		}
		openEditor(new SimpleFormEditorInput("Simple Editor"), //$NON-NLS-1$
				"com.ancit.editors.sampleformeditor"); //$NON-NLS-1$
	}

	/*
	 * 
	 */
	protected void openEditor(String inputName, String editorId) {
		openEditor(new FormEditorInput(inputName), editorId);
	}

	protected void openEditor(IEditorInput input, String editorId) {
		IWorkbenchPage page = window.getActivePage();
		try {
			page.openEditor(input, editorId);
		} catch (PartInitException e) {
			System.out.println(e);
		}
	}

	protected IWorkbenchWindow getWindow() {
		return window;
	}

	/**
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	@Override
	public void dispose() {
	}

	/**
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}
