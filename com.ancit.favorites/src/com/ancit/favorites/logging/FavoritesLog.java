/*
 * FILE:            FavoritesLog.java
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
package com.ancit.favorites.logging;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.ancit.favorites.FavoritesPlugin;

public class FavoritesLog {

//	The first group of methods that follow are for convenience, appending information, error messages, and exceptions to the log for the Favorites plug-in.

	public static void logInfo(String message) {
		log(IStatus.INFO, IStatus.OK, message, null);

	}

	public static void logError(Throwable exception) {
		logError("Unexpected Exception", exception);

	}

	public static void logError(String message, Throwable exception) {
		log(IStatus.ERROR, IStatus.OK, message, exception);
	}

//	Each of the preceding methods ultimately calls the following methods which create a status object (see Section 3.6.1, Status objects, on page 123) and then append that status object to the log.

	public static void log(int severity, int code, String message, Throwable exception) {
		log(createStatus(severity, code, message, exception));
	}

	public static IStatus createStatus(int severity, int code, String message, Throwable exception) {
		return new Status(severity, FavoritesPlugin.PLUGIN_ID, code, message, exception);
	}

	public static void log(IStatus status) {
		FavoritesPlugin.getDefault().getLog().log(status);
	}

}
