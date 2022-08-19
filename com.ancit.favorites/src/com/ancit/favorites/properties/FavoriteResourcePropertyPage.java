/*
 * FILE:            FavoriteResourcePropertyPage.java
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
package com.ancit.favorites.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import com.ancit.favorite.model.BasicFavoriteItem;
import com.ancit.favorites.logging.FavoritesLog;

public class FavoriteResourcePropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

	private Text textField;

	@Override
	protected Control createContents(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		panel.setLayout(layout);

		Label label = new Label(panel, SWT.NONE);
		label.setLayoutData(new GridData());
		label.setText("Comment that appears as hover help in the Favorites view:");

		textField = new Text(panel, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		textField.setLayoutData(new GridData(GridData.FILL_BOTH));
		textField.setText(getCommentPropertyValue());

		return panel;
	}

//	The PropertyPage class contains a getElement() accessor method for retrieving the object whose properties are being edited. Create accessor methods for getting and setting the comment associated with the current element:

	protected String getCommentPropertyValue() {
		IResource resource = getElement().getAdapter(IResource.class);
		try {
			String value = resource.getPersistentProperty(BasicFavoriteItem.COMMENT_PROPKEY);
			if (value == null) {
				return BasicFavoriteItem.getDefaultComment();
			}
			return value;
		} catch (CoreException e) {
			FavoritesLog.logError(e);
			return e.getMessage();
		}
	}

	protected void setCommentPropertyValue(String comment) {
		IResource resource = getElement().getAdapter(IResource.class);
		String value = comment;
		if (value.equals(BasicFavoriteItem.getDefaultComment())) {
			value = null;
		}
		try {
			resource.setPersistentProperty(BasicFavoriteItem.COMMENT_PROPKEY, value);
		} catch (CoreException e) {
			FavoritesLog.logError(e);
		}
	}

//	Because FavoriteResourcePropertyPage extends PropertyPage and inherits behavior from the Preference page framework (see Section 12.2.3, PreferencePage, on page 460), the performOk() method is called when the OK button is clicked, giving the Property page an opportunity to save its values.

	@Override
	public boolean performOk() {
		setCommentPropertyValue(textField.getText());
		return super.performOk();
	}

}
