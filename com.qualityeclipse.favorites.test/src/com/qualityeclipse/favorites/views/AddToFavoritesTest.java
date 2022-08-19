/*
 * FILE:            AddToFavoritesTest.java
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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ISetSelectionTarget;

import com.ancit.favorites.views.action.AddToFavoritesActionDelegate;

public class AddToFavoritesTest extends AbstractFavoritesTest {

	protected IProject project;

	public AddToFavoritesTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		project = root.getProject("TestProj");
		project.create(null);
		project.open(null);
	}

	public void testAddToFavorites() throws CoreException {

		// Show the resource navigator and select the project.
		IViewPart navigator = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.showView("org.eclipse.ui.views.ResourceNavigator");
		StructuredSelection selection = new StructuredSelection(project);
		((ISetSelectionTarget) navigator).selectReveal(selection);

		// Execute the action.
		final IObjectActionDelegate delegate = new AddToFavoritesActionDelegate();
		IAction action = new Action("Test Add to Favorites") {
			@Override
			public void run() {
				delegate.run(this);
			}
		};
		delegate.setActivePart(action, navigator);
		delegate.selectionChanged(action, selection);
		action.run();

		// Add code here at a later time to verify that the
		// Add to Favorites action correctly added the
		// appropriate values to the Favorites view.
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		// Wait for a bit for the system to catch up
		// so that the delete operation does not collide
		// with any background tasks.
		delay(3000);
		waitForJobs();

		project.delete(true, true, null);
	}

}
