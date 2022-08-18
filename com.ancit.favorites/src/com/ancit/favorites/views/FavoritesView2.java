/*
 * FILE:            FavoritesView2.java
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
package com.ancit.favorites.views;

import java.util.Comparator;
import java.util.Iterator;

import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.ancit.favorites.helpers.FavoritesManager;
import com.ancit.favorites.helpers.FavoritesViewContentProvider;
import com.ancit.favorites.helpers.FavoritesViewSorter;
import com.ancit.favorites.helpers.IFavoriteItem;
import com.ancit.favorites.views.action.AutoResizeTableLayout;
import com.ancit.favorites.views.action.CopyFavoritesAction;
import com.ancit.favorites.views.action.FavoritesDragSource;
import com.ancit.favorites.views.action.FavoritesDropTarget;
import com.ancit.favorites.views.action.FavoritesViewFilterAction;
import com.ancit.favorites.views.action.OpenEditorActionDelegate;
import com.ancit.favorites.views.action.RemoveFavoritesAction;
import com.ancit.favorites.views.action.RenameFavoriteAction;

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

public class FavoritesView2 extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.ancit.favorites.views.FavoritesView2";
	private FavoritesViewFilterAction filterAction;

	@Inject
	IWorkbench workbench;

	private ISelectionListener pageSelectionListener;

	private TableColumn typeColumn;
	private TableColumn nameColumn;
	private TableColumn locationColumn;

	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private Clipboard clipboard;
	private CopyFavoritesAction copyAction;

	private RemoveFavoritesAction removeAction;
	private RenameFavoriteAction renameAction;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
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
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		final Table table = viewer.getTable();
		AutoResizeTableLayout layout = new AutoResizeTableLayout(table);
		table.setLayout(layout);

		typeColumn = new TableColumn(table, SWT.LEFT);
		typeColumn.setText("");
//		typeColumn.setWidth(18);
		layout.addColumnData(new ColumnPixelData(18));

		nameColumn = new TableColumn(table, SWT.LEFT);
		nameColumn.setText("Name");
//		nameColumn.setWidth(200);
		layout.addColumnData(new ColumnWeightData(200));

		locationColumn = new TableColumn(table, SWT.LEFT);
		locationColumn.setText("Location");
//		locationColumn.setWidth(450);
		layout.addColumnData(new ColumnWeightData(200));

		table.setHeaderVisible(true);
		table.setLinesVisible(false);

		viewer.setContentProvider(new FavoritesViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(FavoritesManager.getManager());
		createTableSorter();

		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(viewer.getControl(), "com.ancit.favorites.viewer");
		getSite().setSelectionProvider(viewer);
		createActions();
		createContextMenu();
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		createToolbarButtons();
		createViewPulldownMenu();
		hookKeyboardActions();
		hookGlobalActions();
		hookDragAndDrop();
		hookMouse();
		hookPageSelection();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(FavoritesView2.this::fillContextMenu);
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
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		boolean isEmpty = viewer.getSelection().isEmpty();
		removeAction.setEnabled(!isEmpty);
		manager.add(removeAction);
		copyAction.setEnabled(!isEmpty);
		manager.add(copyAction);

		manager.add(renameAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			@Override
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			@Override
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(workbench.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			@Override
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				Object obj = selection.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(event -> doubleClickAction.run());
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "favorites View", message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * For testing purposes only.
	 * 
	 * @return the table viewer in the Favorites view
	 */
	public TableViewer getFavoritesViewer() {
		return viewer;
	}

	private void createTableSorter() {
		Comparator nameComparator = (o1, o2) -> ((IFavoriteItem) o1).getName()
				.compareTo(((IFavoriteItem) o2).getName());
		Comparator locationComparator = (o1, o2) -> ((IFavoriteItem) o1).getLocation()
				.compareTo(((IFavoriteItem) o2).getLocation());
		Comparator typeComparator = (o1, o2) -> ((IFavoriteItem) o1).getType()
				.compareTo(((IFavoriteItem) o2).getType());

		FavoritesViewSorter sorter = new FavoritesViewSorter(viewer,
				new TableColumn[] { nameColumn, locationColumn, typeColumn },
				new Comparator[] { nameComparator, locationComparator, typeComparator });
		viewer.setSorter(sorter);
	}

	public IFavoriteItem[] getSelectedFavorites() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		IFavoriteItem[] items = new IFavoriteItem[selection.size()];
		Iterator iter = selection.iterator();
		int index = 0;
		while (iter.hasNext()) {
			items[index] = (IFavoriteItem) iter.next();
			index++;
		}
		return items;
	}

	private void createActions() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		ISharedImages platformImages = workbench.getSharedImages();
		removeAction = new RemoveFavoritesAction(this, "Remove");
		removeAction.setImageDescriptor(platformImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		removeAction
				.setDisabledImageDescriptor(platformImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		removeAction.setToolTipText("Remove the selected favorite items");

		copyAction = new CopyFavoritesAction(this, "Copy");
		copyAction.setImageDescriptor(platformImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		copyAction.setDisabledImageDescriptor(platformImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		copyAction.setToolTipText("Copy the selected favorite items");

		renameAction = new RenameFavoriteAction(this, viewer.getTable(), "Rename");

	}

	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(FavoritesView2.this::fillContextMenu);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void createToolbarButtons() {
		getViewSite().getActionBars().getToolBarManager().add(removeAction);
		removeAction.setEnabled(false);
		viewer.addSelectionChangedListener(event -> removeAction.setEnabled(!event.getSelection().isEmpty()));
	}

	private void createViewPulldownMenu() {
		IMenuManager menu = getViewSite().getActionBars().getMenuManager();
		filterAction = new FavoritesViewFilterAction(viewer, "Filter...");
		menu.add(filterAction);
	}

	private void hookKeyboardActions() {
		viewer.getControl().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent event) {
				handleKeyReleased(event);
			}
		});
	}

	protected void handleKeyReleased(KeyEvent event) {
		if (event.character == SWT.DEL && event.stateMask == 0) {
			removeAction.run();
		}
	}

	private void hookGlobalActions() {
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.DELETE.getId(), removeAction);
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);

	}

	public Clipboard getClipboard() {
		if (clipboard == null) {
			clipboard = new Clipboard(getSite().getShell().getDisplay());
		}
		return clipboard;
	}

	private void hookDragAndDrop() {
		new FavoritesDragSource(this, viewer);
		new FavoritesDropTarget(this, viewer);
	}

	private void hookMouse() {
		viewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if ((e.stateMask & SWT.ALT) != 0) {
					renameAction.run();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				OpenEditorActionDelegate.openEditor(getSite().getPage(), viewer.getSelection());
			}
		});
	}

	private void hookPageSelection() {
		pageSelectionListener = this::pageSelectionChanged;
		getSite().getPage().addPostSelectionListener(pageSelectionListener);
	}

	protected void pageSelectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part == this || !(selection instanceof IStructuredSelection)) {
			return;
		}
		IStructuredSelection sel = (IStructuredSelection) selection;
		IFavoriteItem[] items = FavoritesManager.getManager().existingFavoritesFor(sel.iterator());
		if (items.length > 0) {
			viewer.setSelection(new StructuredSelection(items), true);
		}
	}

	@Override
	public void dispose() {
		if (clipboard != null) {
			clipboard.dispose();
		}
		if (pageSelectionListener != null) {
			getSite().getPage().removePostSelectionListener(pageSelectionListener);
		}
		super.dispose();
	}
}
