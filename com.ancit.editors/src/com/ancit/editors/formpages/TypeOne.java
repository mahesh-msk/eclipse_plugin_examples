/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ancit.editors.formpages;

/**
 * @author dejan
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TypeOne extends NamedObject {
	public static final String P_CHOICE="choice"; //$NON-NLS-1$
	public static final String P_FLAG="flag"; //$NON-NLS-1$
	public static final String P_TEXT="text"; //$NON-NLS-1$
	public static final String [] CHOICES = {
			Messages.getString("TypeOne.c1"), Messages.getString("TypeOne.c2"), Messages.getString("TypeOne.c3"), Messages.getString("TypeOne.c4") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	private int choice=0;
	private String text;
	private boolean flag;

	/**
	 * @param name
	 */
	public TypeOne(String name, int choice, boolean flag, String text) {
		super(name);
		this.flag = flag;
		this.text = text;
		this.choice = choice;
	}
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
		model.fireModelChanged(new Object[] {this}, IModelListener.CHANGED, P_CHOICE);
	}
	public boolean getFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
		model.fireModelChanged(new Object[] {this}, IModelListener.CHANGED, P_FLAG);
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		model.fireModelChanged(new Object[] {this}, IModelListener.CHANGED, P_TEXT);
	}
}