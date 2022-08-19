/*
 * FILE:            FavoritesViewTest.java
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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import com.ancit.favorites.views.FavoritesView2;

import junit.framework.TestCase;

/**
 * The class <code>FavoritesViewTest</code> contains tests for the class
 * {@link com.qualityeclipse.favorites.views.FavoritesView2}.
 *
 * @pattern JUnit Test Case
 * @generatedBy CodePro Studio
 */
public class FavoritesViewTest extends AbstractFavoritesTest {
	private static final String VIEW_ID = "com.ancit.favorites.views.FavoritesView";

	/**
	 * The object that is being tested.
	 *
	 * @see com.qualityeclipse.favorites.views.FavoritesView2
	 */
	private FavoritesView2 testView;

	/**
	 * Construct new test instance.
	 *
	 * @param name the test name
	 */
	public FavoritesViewTest(String name) {
		super(name);
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// Initialize the test fixture for each test
		// that is run.
		waitForJobs();
		testView = (FavoritesView2) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.showView(VIEW_ID);

		// Delay for 3 seconds so that
		// the Favorites view can be seen.
		waitForJobs();
		delay(3000);

		// Add additional setup code here.
	}

	/**
	 * Perform post-test cleanup.
	 *
	 * @throws Exception
	 *
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		// Dispose of test fixture.
		waitForJobs();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(testView);

		// Add additional teardown code here.
	}

	/**
	 * Run the view test.
	 */
	public void testView() {
		TableViewer viewer = testView.getFavoritesViewer();
		Object[] expectedContent = { "One", "Two", "Three" };
		Object[] expectedLabels = new String[] { "One", "Two", "Three" };

		// Assert valid content.
		IStructuredContentProvider contentProvider = (IStructuredContentProvider) viewer.getContentProvider();
		assertEquals(expectedContent, contentProvider.getElements(viewer.getInput()));

		// Assert valid labels.
		ITableLabelProvider labelProvider = (ITableLabelProvider) viewer.getLabelProvider();
		for (int i = 0; i < expectedLabels.length; i++) {
			assertEquals(expectedLabels[i], labelProvider.getColumnText(expectedContent[i], 1));
		}
	}

}