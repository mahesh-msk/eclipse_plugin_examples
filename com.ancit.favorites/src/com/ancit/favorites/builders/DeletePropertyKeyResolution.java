/*
 * FILE:            DeletePropertyKeyResolution.java
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

import org.eclipse.swt.graphics.Image;

public class DeletePropertyKeyResolution extends AbstractKeyResolution {

	@Override
	public String getDescription() {
		return "Delete property key/value pair" + " in the plugin.properties file";
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public String getLabel() {
		return "Delete property key";
	}

}