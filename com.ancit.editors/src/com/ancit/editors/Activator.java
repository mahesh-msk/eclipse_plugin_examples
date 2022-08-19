/*
 * FILE:            Activator.java
 *
 * SW-COMPONENT:    com.ancit.editors
 *
 * DESCRIPTION:     -
 *
 * COPYRIGHT:       � 2015 - 2022 Robert Bosch GmbH
 *
 * The reproduction, distribution and utilization of this file as
 * well as the communication of its contents to others without express
 * authorization is prohibited. Offenders will be held liable for the
 * payment of damages. All rights reserved in the event of the grant
 * of a patent, utility model or design.
 */
package com.ancit.editors;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.ancit.editors"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	// The shared instance.
	// Resource bundle.
	private ResourceBundle resourceBundle;
	private FormColors formColors;
	public static final String IMG_FORM_BG = "formBg"; //$NON-NLS-1$
	public static final String IMG_LARGE = "large"; //$NON-NLS-1$
	public static final String IMG_HORIZONTAL = "horizontal"; //$NON-NLS-1$
	public static final String IMG_VERTICAL = "vertical"; //$NON-NLS-1$
	public static final String IMG_SAMPLE = "sample"; //$NON-NLS-1$
	public static final String IMG_WIZBAN = "wizban"; //$NON-NLS-1$
	public static final String IMG_LINKTO_HELP = "linkto_help"; //$NON-NLS-1$
	public static final String IMG_HELP_TOPIC = "help_topic"; //$NON-NLS-1$
	public static final String IMG_CLOSE = "close"; //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public Activator() {
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("org.eclipse.ui.forms.examples.internal.ExamplesPluginResources"); //$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		registerImage(registry, IMG_FORM_BG, "form_banner.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_LARGE, "large_image.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_HORIZONTAL, "th_horizontal.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_VERTICAL, "th_vertical.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_SAMPLE, "sample.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_WIZBAN, "newprj_wiz.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_LINKTO_HELP, "linkto_help.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_HELP_TOPIC, "help_topic.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_CLOSE, "close_view.gif"); //$NON-NLS-1$
	}

	private void registerImage(ImageRegistry registry, String key, String fileName) {
		try {
			IPath path = new Path("icons/" + fileName); //$NON-NLS-1$
			URL url = find(path);
			if (url != null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public Image getImage(String key) {
		return getImageRegistry().get(key);
	}

	public ImageDescriptor getImageDescriptor(String key) {
		return getImageRegistry().getDescriptor(key);
	}

	public FormColors getFormColors(Display display) {
		if (formColors == null) {
			formColors = new FormColors(display);
			formColors.markShared();
		}
		return formColors;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/*
	 * 
	 * Returns the string from the plugin's resource bundle,or'key'if not*found.
	 */

	public static String getResourceString(String key) {
		ResourceBundle bundle = Activator.getDefault().getResourceBundle();
		try {
			return bundle != null ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

}
