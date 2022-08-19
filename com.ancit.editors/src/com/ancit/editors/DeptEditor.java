/*
 * FILE:            DeptEditor.java
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPartConstants;

import com.ancit.editors.constant.MyConstants;
import com.ancit.editors.listner.DirtyUtils;
import com.ancit.editors.model.Department;
import com.ancit.editors.model.DepartmentDAO;

// IReusableEditor: Can setting new input value for this Editor.
public class DeptEditor extends AbstractBaseEditor implements IReusableEditor {

	public static final String ID = "com.ancit.editors.depteditor";
	private Integer deptId;
	private Text text_deptNo;
	private Text text_deptName;
	private Text text_location;

	public DeptEditor() {
	}

	/**
	 * Create contents of the editor part.
	 *
	 * @param parent
	 */
	@Override
	public void createPartControl2(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		Label lblDeptNo = new Label(container, SWT.NONE);
		lblDeptNo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDeptNo.setText("Dept No");

		text_deptNo = new Text(container, SWT.BORDER);
		text_deptNo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblDeptName = new Label(container, SWT.NONE);
		lblDeptName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDeptName.setText("Dept Name");

		text_deptName = new Text(container, SWT.BORDER);
		text_deptName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblLocation = new Label(container, SWT.NONE);
		lblLocation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLocation.setText("Location");

		text_location = new Text(container, SWT.BORDER);
		text_location.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);

		DirtyListenerImpl dirtyListener = new DirtyListenerImpl();
		DirtyUtils.registryDirty(dirtyListener, this.text_deptName, this.text_deptNo, this.text_location);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			String deptNo = this.text_deptNo.getText();
			String deptName = this.text_deptName.getText();
			String location = this.text_location.getText();
			if (this.deptId != null) {
				Department dept = DepartmentDAO.updateDepartment(deptId, deptNo, deptName, location);

				this.setInput(new DeptEditorInput(dept));
			} else {
				Department dept = DepartmentDAO.insertDepartment(deptNo, deptName, location);
				this.setInput(new DeptEditorInput(dept));
			}
			this.setDirty(false);
			this.firePropertyChange(MyConstants.EDITOR_DATA_CHANGED);
		} catch (Exception e) {
			MessageDialog.openError(this.getSite().getShell(), "Error", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected Control[] registryDirtyControls() {
		return new Control[] { this.text_deptName, this.text_deptNo, this.text_location };
	}

	@Override
	public void showData() {
		DeptEditorInput ip = (DeptEditorInput) this.getEditorInput();
		Department dept = ip.getDept();

		this.deptId = dept.getDeptId();
		this.text_deptName.setText(dept.getDeptName() == null ? "" : dept.getDeptName());
		this.text_deptNo.setText(dept.getDeptNo() == null ? "" : dept.getDeptNo());
		this.text_location.setText(dept.getLocation() == null ? "" : dept.getLocation());
		// Clear dirty.
		this.setDirty(false);
	}

	// Override setInput(..) with public (IReusableEditor)
	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
		firePropertyChange(IWorkbenchPartConstants.PROP_INPUT);
	}

	public String getDeptInfo() {
		DeptEditorInput input = (DeptEditorInput) this.getEditorInput();
		Department dept = input.getDept();
		if (dept == null) {
			return "";
		}
		return dept.getDeptNo() + " - " + dept.getDeptName() + " - " + dept.getLocation();
	}

}
