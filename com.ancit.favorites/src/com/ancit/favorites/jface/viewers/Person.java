/*
 * FILE:            Person.java
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

public class Person {
	public String firstName = "John";
	public String lastName = "Doe";
	public int age = 37;
	public Person[] children = {};
	public Person parent = null;

	public Person(String firstName, String lastName, int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}

	public Person(String firstName, String lastName, int age, Person[] children) {
		this(firstName, lastName, age);
		this.children = children;
		for (int i = 0; i < children.length; i++) {
			children[i].parent = this;
		}
	}

	public static Person[] example() {
		return new Person[] {
				new Person("Dan", "Rubel", 41,
						new Person[] { new Person("Beth", "Rubel", 11), new Person("David", "Rubel", 6) }),
				new Person("Eric", "Clayberg", 42,
						new Person[] { new Person("Lauren", "Clayberg", 9), new Person("Lee", "Clayberg", 7) }),
				new Person("Mike", "Taylor", 55) };
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}