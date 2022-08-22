/*
 * FILE:            QuickFix.java
 *
 * SW-COMPONENT:    com.ancit.markers
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
package com.ancit.markers.builder;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IMarkerResolution;

public class QuickFix implements IMarkerResolution {
	String label;

	QuickFix(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void run(IMarker marker) {
		MessageDialog.openInformation(null, "QuickFix Demo", "This quick-fix is not yet implemented");
	}
}