/*
 * FILE:            PersonTableLabelProvider.java
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
package com.ancit.favorites.jface.viewers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class PersonTableLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public String getColumnText(Object element, int index) {
		Person person = (Person) element;
		switch (index) {
		case 0:
			return person.firstName;
		case 1:
			return person.lastName;
		case 2:
			return Integer.toString(person.age);
		case 3:
			return Integer.toString(person.children.length);
		default:
			return "unknown " + index;
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
}