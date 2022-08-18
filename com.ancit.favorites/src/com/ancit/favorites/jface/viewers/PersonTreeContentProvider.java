/*
 * FILE:            PersonTreeContentProvider.java
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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class PersonTreeContentProvider extends ArrayContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {
		Person person = (Person) parentElement;
		return person.children;
	}

	@Override
	public Object getParent(Object element) {
		Person person = (Person) element;
		return person.parent;
	}

	@Override
	public boolean hasChildren(Object element) {
		Person person = (Person) element;
		return person.children.length > 0;
	}
}