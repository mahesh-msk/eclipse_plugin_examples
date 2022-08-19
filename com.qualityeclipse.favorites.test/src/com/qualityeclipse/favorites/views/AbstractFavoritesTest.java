/*
 * FILE:            AbstractFavoritesTest.java
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

import org.eclipse.swt.widgets.Display;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class AbstractFavoritesTest extends TestCase {

	public AbstractFavoritesTest(String name) {
		super(name);
	}

	/**
	 * Process UI input but do not return for the specified time interval.
	 *
	 * @param waitTimeMillis the number of milliseconds
	 */
	protected void delay(long waitTimeMillis) {
		Display display = Display.getCurrent();

		// If this is the UI thread,
		// then process input.
		if (display != null) {
			long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;
			while (System.currentTimeMillis() < endTimeMillis) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.update();
		}
		// Otherwise, perform a simple sleep.
		else {
			try {
				Thread.sleep(waitTimeMillis);
			} catch (InterruptedException e) {
				// Ignored.
			}
		}
	}

	/**
	 * Wait until all background tasks are complete.
	 */
	public void waitForJobs() {
//      while (Platform.getJobManager().currentJob() != null)
		delay(1000);
	}

	/**
	 * Assert that the two arrays are equal. Throw an AssertionException if they are
	 * not.
	 *
	 * @param expected first array
	 * @param actual   second array
	 */
	private void assertEquals(Object[] expected, Object[] actual) {
		if (expected == null) {
			if (actual == null) {
				return;
			}
			throw new AssertionFailedError("expected is null, but actual is not");
		}
		if (actual == null) {
			throw new AssertionFailedError("actual is null, but expected is not");
		}

		assertEquals("expected.length " + expected.length + ", but actual.length " + actual.length, expected.length,
				actual.length);

		for (int i = 0; i < actual.length; i++) {
			assertEquals("expected[" + i + "] is not equal to actual[" + i + "]", expected[i], actual[i]);
		}
	}
}
