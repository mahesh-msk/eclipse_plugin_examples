/*
 * FILE:            FavoritesPreferencePage.java
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
package com.ancit.favorites.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.ancit.favorites.FavoritesPlugin;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class FavoritesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private BooleanFieldEditor namePrefEditor;
	private BooleanFieldEditor locationPrefEditor;

	public FavoritesPreferencePage() {
		super(GRID);
		setPreferenceStore(FavoritesPlugin.getDefault().getPreferenceStore());
		setDescription("Favorites view column visibility:");
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	public void createFieldEditors() {
		namePrefEditor = new BooleanFieldEditor(PreferenceConstants.FAVORITES_VIEW_NAME_COLUMN_VISIBLE,
				"Show name column", getFieldEditorParent());
		addField(namePrefEditor);
		locationPrefEditor = new BooleanFieldEditor(PreferenceConstants.FAVORITES_VIEW_LOCATION_COLUMN_VISIBLE,
				"Show location column", getFieldEditorParent());
		addField(locationPrefEditor);
	}

	@Override
	protected void checkState() {
		super.checkState();
		if (!isValid()) {
			return;
		}
		if (!namePrefEditor.getBooleanValue() && !locationPrefEditor.getBooleanValue()) {
			setErrorMessage("Must have at least one column visible");
			setValid(false);
		} else {
			setErrorMessage(null);
			setValid(true);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		if (FieldEditor.VALUE.equals(event.getProperty())
				&& (event.getSource() == namePrefEditor || event.getSource() == locationPrefEditor)) {
			checkState();
		}
	}

}