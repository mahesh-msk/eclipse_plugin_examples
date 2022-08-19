/*
 * FILE:            DeptCommand.java
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
package com.ancit.editors.command;

/*
 * FILE:            DeptCommand.java
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
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ancit.editors.DeptEditor;
import com.ancit.editors.DeptEditorInput;
import com.ancit.editors.model.Department;

public class DeptCommand extends AbstractHandler {

	public static final String ID = "command.dept";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get the page
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		// get the selection
		ISelection selection = HandlerUtil.getCurrentSelection(event);

		Object selectObj = null;

		// Having selected on DeptListView
		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			selectObj = ((IStructuredSelection) selection).getFirstElement();
		}

		// No Selection on DeptListView
		// (Create new Department).
		else {
			// Create new Department.
			selectObj = new Department();
		}

		Department dept = (Department) selectObj;
		DeptEditorInput input = new DeptEditorInput(dept);

		boolean found = false;

		// Opening Editor references
		IEditorReference[] eRefs = page.getEditorReferences();
		for (IEditorReference ref : eRefs) {
			IEditorPart editor = ref.getEditor(false);
			if (editor instanceof DeptEditor) {
				// Restore
				DeptEditor deptEditor = (DeptEditor) ref.getEditor(true);
				found = true;

				boolean saved = true;

				// If editor is dirty, save it.
				if (deptEditor.isDirty()) {
					saved = page.saveEditor(deptEditor, true);
				}
				if (saved) {

					// Reset input for DeptEditor.
					page.reuseEditor(deptEditor, input);
					deptEditor.showData();
				}
			}
		}
		if (!found) {
			try {
				page.openEditor(input, DeptEditor.ID);
			} catch (PartInitException e) {
				throw new RuntimeException(e);
			}
		}

		return null;
	}
}
