/*
 * FILE:            ViolationResolutionGenerator.java
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;

public class ViolationResolutionGenerator implements IMarkerResolutionGenerator2 {
	@Override
	public boolean hasResolutions(IMarker marker) {
		switch (getViolation(marker)) {
		case PropertiesFileAuditor.MISSING_KEY_VIOLATION:
			return true;
		case PropertiesFileAuditor.UNUSED_KEY_VIOLATION:
			return true;
		default:
			return false;
		}
	}

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		List resolutions = new ArrayList();
		switch (getViolation(marker)) {
		case PropertiesFileAuditor.MISSING_KEY_VIOLATION:
			resolutions.add(new CreatePropertyKeyResolution());
			break;
		case PropertiesFileAuditor.UNUSED_KEY_VIOLATION:
			resolutions.add(new DeletePropertyKeyResolution());
			resolutions.add(new CommentPropertyKeyResolution());
			break;
		default:
			break;
		}

		return (IMarkerResolution[]) resolutions.toArray(new IMarkerResolution[resolutions.size()]);
	}

	private int getViolation(IMarker marker) {
		return marker.getAttribute(PropertiesFileAuditor.VIOLATION, 0);
	}
}