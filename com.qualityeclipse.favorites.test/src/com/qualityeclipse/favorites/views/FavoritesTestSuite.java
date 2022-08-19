/*
 * FILE:            FavoritesTestSuite.java
 *
 * SW-COMPONENT:    com.qualityeclipse.favorites.test
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
package com.qualityeclipse.favorites.views;

import junit.framework.TestSuite;

public class FavoritesTestSuite {

	public static TestSuite suite() {

		TestSuite suite = new TestSuite("Favorites test suite");

		suite.addTest(new TestSuite(FavoritesViewTest.class));

		suite.addTest(new TestSuite(OpenFavoritesViewTest.class));

		return suite;

	}

}