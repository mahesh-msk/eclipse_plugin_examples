/*
 * FILE:            FavoritesViewSorter.java
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
package com.ancit.favorites.helpers;

import java.util.Comparator;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

public class FavoritesViewSorter extends ViewerSorter {
	// Simple data structure for grouping
	// sort information by column.
	private static class SortInfo {
		int columnIndex;
		Comparator comparator;
		boolean descending;
	}

	private TableViewer viewer;
	private SortInfo[] infos;

	public FavoritesViewSorter(TableViewer viewer, TableColumn[] columns, Comparator[] comparators) {
		this.viewer = viewer;
		infos = new SortInfo[columns.length];
		for (int i = 0; i < columns.length; i++) {
			infos[i] = new SortInfo();
			infos[i].columnIndex = i;
			infos[i].comparator = comparators[i];
			infos[i].descending = false;
			createSelectionListener(columns[i], infos[i]);
		}
	}

	@Override
	public int compare(Viewer viewer, Object favorite1, Object favorite2) {
		for (SortInfo element : infos) {
			int result = element.comparator.compare(favorite1, favorite2);
			if (result != 0) {
				if (element.descending) {
					return -result;
				}
				return result;
			}
		}
		return 0;
	}

	private void createSelectionListener(final TableColumn column, final SortInfo info) {
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sortUsing(info);
			}
		});
	}

	protected void sortUsing(SortInfo info) {
		if (info == infos[0]) {
			info.descending = !info.descending;
		} else {
			for (int i = 0; i < infos.length; i++) {
				if (info == infos[i]) {
					System.arraycopy(infos, 0, infos, 1, i);
					infos[0] = info;
					info.descending = false;
					break;
				}
			}
		}
		viewer.refresh();
	}
}