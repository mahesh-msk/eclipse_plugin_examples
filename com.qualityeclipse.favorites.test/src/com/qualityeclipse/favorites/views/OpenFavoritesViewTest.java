/*
 * FILE:            OpenFavoritesViewTest.java
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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.ancit.favorites.views.FavoritesView2;
import com.ancit.favorites.views.action.OpenFavoritesViewActionDelegate;

public class OpenFavoritesViewTest extends AbstractFavoritesTest {

	public OpenFavoritesViewTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// Ensure that the view is not open.
		waitForJobs();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart view = page.findView(FavoritesView2.ID);
		if (view != null) {
			page.hideView(view);
		}

		// Delay for 3 seconds so that
		// the Favorites view can be seen.
		waitForJobs();
		delay(3000);
	}

	public void testOpenFavoritesView() {

		// Execute the operation.
		new Action("OpenFavoritesViewTest") {
			@Override
			public void run() {
				IWorkbenchWindowActionDelegate delegate = new OpenFavoritesViewActionDelegate();
				delegate.init(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				delegate.selectionChanged(this, StructuredSelection.EMPTY);
				delegate.run(this);
			}
		}.run();

		// Test that the operation completed successfully.
		waitForJobs();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		assertTrue(page.findView(FavoritesView2.ID) != null);
	}
}
