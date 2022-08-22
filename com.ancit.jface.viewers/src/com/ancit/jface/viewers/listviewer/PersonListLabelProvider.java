/*
 * FILE:            PersonListLabelProvider.java
 *
 * SW-COMPONENT:    com.ancit.jface.viewers
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
package com.ancit.jface.viewers.listviewer;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.ancit.jface.viewers.model.Person;

public class PersonListLabelProvider extends LabelProvider {
	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		Person person = (Person) element;
		return person.firstName + " " + person.lastName;
	}
}