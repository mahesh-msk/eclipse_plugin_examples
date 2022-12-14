/*
 * FILE:            AutoResizeTableLayout.java
 *
 * SW-COMPONENT:    com.ancit.favorites
 *
 * DESCRIPTION:     -
 *
 * COPYRIGHT:       ? 2015 - 2022 Robert Bosch GmbH
 *
 * The reproduction, distribution and utilization of this file as
 * well as the communication of its contents to others without express
 * authorization is prohibited. Offenders will be held liable for the
 * payment of damages. All rights reserved in the event of the grant
 * of a patent, utility model or design.
 */
package com.ancit.emf.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class AutoResizeTableLayout extends TableLayout implements ControlListener {
	private final Table table;
	private List columns = new ArrayList();
	private boolean autosizing = false;

	public AutoResizeTableLayout(Table table) {
		this.table = table;
		table.addControlListener(this);
	}

	@Override
	public void addColumnData(ColumnLayoutData data) {
		columns.add(data);
		super.addColumnData(data);
	}

	@Override
	public void controlMoved(ControlEvent e) {
	}

	@Override
	public void controlResized(ControlEvent e) {
		if (autosizing) {
			return;
		}
		autosizing = true;
		try {
			autoSizeColumns();
		} finally {
			autosizing = false;
		}
	}

	private void autoSizeColumns() {
		int width = table.getClientArea().width;

		// XXX: Layout is being called with an invalid value
		// the first time it is being called on Linux.
		// This method resets the layout to null,
		// so we run it only when the value is OK.
		if (width <= 1) {
			return;
		}

		TableColumn[] tableColumns = table.getColumns();
		int size = Math.min(columns.size(), tableColumns.length);
		int[] widths = new int[size];
		int fixedWidth = 0;
		int numberOfWeightColumns = 0;
		int totalWeight = 0;

		// First calculate space occupied by fixed columns.
		for (int i = 0; i < size; i++) {
			ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
			if (col instanceof ColumnPixelData) {
				int pixels = ((ColumnPixelData) col).width;
				widths[i] = pixels;
				fixedWidth += pixels;
			} else if (col instanceof ColumnWeightData) {
				ColumnWeightData cw = (ColumnWeightData) col;
				numberOfWeightColumns++;
				int weight = cw.weight;
				totalWeight += weight;
			} else {
				throw new IllegalStateException("Unknown column layout data");
			}
		}
		// Do we have columns that have a weight?
		if (numberOfWeightColumns > 0) {
			// Now, distribute the rest
			// to the columns with weight.
			int rest = width - fixedWidth;
			int totalDistributed = 0;
			for (int i = 0; i < size; i++) {
				ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
				if (col instanceof ColumnWeightData) {
					ColumnWeightData cw = (ColumnWeightData) col;
					int weight = cw.weight;
					int pixels = totalWeight == 0 ? 0 : weight * rest / totalWeight;
					if (pixels < cw.minimumWidth) {
						pixels = cw.minimumWidth;
					}
					totalDistributed += pixels;
					widths[i] = pixels;
				}
			}

			// Distribute any remaining pixels
			// to columns with weight.
			int diff = rest - totalDistributed;
			for (int i = 0; diff > 0; i++) {
				if (i == size) {
					i = 0;
				}
				ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
				if (col instanceof ColumnWeightData) {
					++widths[i];
					--diff;
				}
			}
		}

		for (int i = 0; i < size; i++) {
			if (tableColumns[i].getWidth() != widths[i]) {
				tableColumns[i].setWidth(widths[i]);
			}
		}
	}
}