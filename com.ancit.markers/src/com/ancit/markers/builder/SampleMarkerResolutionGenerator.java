/*
 * FILE:            SampleMarkerResolutionGenerator.java
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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class SampleMarkerResolutionGenerator implements IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker mk) {
		try {
			Object problem = mk.getAttribute("WhatsUp");
			return new IMarkerResolution[] { new QuickFix("Fix #1 for " + problem),
					new QuickFix("Fix #2 for " + problem), };
		} catch (CoreException e) {
			return new IMarkerResolution[0];
		}
	}

}
