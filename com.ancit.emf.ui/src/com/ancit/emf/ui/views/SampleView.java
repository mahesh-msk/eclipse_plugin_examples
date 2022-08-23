/*
 * FILE:            SampleView.java
 *
 * SW-COMPONENT:    com.ancit.emf.ui
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
package com.ancit.emf.ui.views;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import com.ancit.emf.ui.Activator;

import SchoolModel.School;
import SchoolModel.SchoolModelFactory;
import SchoolModel.SchoolModelPackage;
import SchoolModel.Student;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class SampleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.ancit.emf.ui.views.SampleView";

	@Inject
	IWorkbench workbench;

	private Table table;
	private TableViewer viewer;

	private Action addStudentAction;
	private Action removeStudentAction;
	private Action saveStudentModelAction;
	private Action loadStudentModelAction;
	private Action doubleClickAction;

	private SchoolModel.Class cls;

	private ResourceSetImpl resourceSetImpl;

	private School school;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Student) {
				Student student = (Student) element;
				switch (columnIndex) {
				case 0:
					return Integer.toString(student.getSeatNo());
				case 1:
					return student.getName();
				case 2:
					return student.getDateOfBirth();
				default:
					break;
				}

			}
			return "";
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		composite.setLayout(tableColumnLayout);

		viewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = viewer.getTable();
		AutoResizeTableLayout layout = new AutoResizeTableLayout(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn tableViewerColumn1 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn slNoCol = tableViewerColumn1.getColumn();
		slNoCol.setText("Sl. No.");
		tableColumnLayout.setColumnData(slNoCol, new ColumnPixelData(150, true, true));

		TableViewerColumn tableViewerColumn2 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn nameCol = tableViewerColumn2.getColumn();
		nameCol.setText("Name");
		tableColumnLayout.setColumnData(nameCol, new ColumnPixelData(150, true, true));

		TableViewerColumn tableViewerColumn3 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn dobCol = tableViewerColumn3.getColumn();
		dobCol.setText("D.O.B");
		tableColumnLayout.setColumnData(dobCol, new ColumnPixelData(150, true, true));

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		createSchoolModel();
		addContentListener();

		// initialize and configure emf resourceset
		initializeResourceSet();

		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(viewer.getControl(), "com.ancit.emf.ui.viewer");
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void initializeResourceSet() {
		resourceSetImpl = new ResourceSetImpl();
		// Registering the EPackage to this ResourceSet
		resourceSetImpl.getPackageRegistry().put(SchoolModelPackage.eNS_URI, SchoolModelPackage.eINSTANCE);
		// Registering fileextension to its ResourceFactory
		resourceSetImpl.getResourceFactoryRegistry().getExtensionToFactoryMap().put("school",
				new XMIResourceFactoryImpl());

	}

	private void addContentListener() {
		cls.eAdapters().add(new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				System.out.println("New Student Added");
				super.notifyChanged(notification);
			}
		});
	}

	private void createSchoolModel() {
		school = SchoolModelFactory.eINSTANCE.createSchool();
		school.setName("School");

		cls = SchoolModelFactory.eINSTANCE.createClass();
		cls.setStandard("1");
		school.getClasses().add(cls);

		Student student = SchoolModelFactory.eINSTANCE.createStudent();
		student.setName("Shiva");
		student.setSeatNo(1);
		student.setDateOfBirth("05/10/1999");
		cls.getStudents().add(student);

		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(cls.getStudents());
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(SampleView.this::fillContextMenu);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(addStudentAction);
		manager.add(new Separator());
		manager.add(removeStudentAction);
	}

	private void fillContextMenu(IMenuManager manager) {

		manager.add(addStudentAction);
		boolean isEmpty = viewer.getSelection().isEmpty();
		removeStudentAction.setEnabled(!isEmpty);
		manager.add(removeStudentAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addStudentAction);
		manager.add(removeStudentAction);
		manager.add(saveStudentModelAction);
		manager.add(loadStudentModelAction);
	}

	private void makeActions() {
		addStudentAction = new Action() {
			@Override
			public void run() {

				if (cls != null) {

					StudentDetailsDialog dialog = new StudentDetailsDialog(Display.getDefault().getActiveShell());
					dialog.create();
					if (dialog.open() == Window.OK) {
						Student student = SchoolModelFactory.eINSTANCE.createStudent();
						student.setName(dialog.getStudentName());
						student.setSeatNo(dialog.getSeatNo());
						student.setDateOfBirth(dialog.getDoj());
						cls.getStudents().add(student);
						viewer.setInput(cls.getStudents());
					}

				} else {
					showMessage("Unable to add student");
				}

			}
		};
		addStudentAction.setText("Add Student");
		addStudentAction.setToolTipText("Add student...");
		addStudentAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		removeStudentAction = new Action() {
			@Override
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		removeStudentAction.setText("Action 2");
		removeStudentAction.setToolTipText("Action 2 tooltip");
		removeStudentAction
				.setImageDescriptor(workbench.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			@Override
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				Object obj = selection.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};

		saveStudentModelAction = new Action() {
			@Override
			public void run() {
				if (school != null) {
					// Create your EMF resource
					Resource resource = resourceSetImpl.createResource(URI.createFileURI("c://temp//student.school"));
					// writing the model into emf resource
					resource.getContents().add(school);
					// saving the resource
					try {
						resource.save(null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		};
		saveStudentModelAction.setText("Save Model");
		saveStudentModelAction.setToolTipText("Save model...");
		saveStudentModelAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		loadStudentModelAction = new Action() {
			@Override
			public void run() {
				// Loading an EMF resource
				Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
				URL url = FileLocator.find(bundle, new Path("./student.school"), null);
				try {
					url = FileLocator.toFileURL(url);
					File file = URIUtil.toFile(URIUtil.toURI(url));
					Resource loadedResource = resourceSetImpl.getResource(URI.createFileURI(file.getAbsolutePath()),
							true);
					School school = (School) loadedResource.getContents().get(0);
					viewer.setInput(school.getClasses().get(0).getStudents());
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}

			}
		};
		loadStudentModelAction.setText("Load Model");
		loadStudentModelAction.setToolTipText("Load model...");
		loadStudentModelAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(event -> doubleClickAction.run());
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Sample View", message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
