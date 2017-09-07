package com.ilanbenichou.filesmanager.report.html.table;

import java.util.Arrays;
import java.util.List;

public final class HtmlTableRow {

	private List<HtmlTableCell> cellsList;

	public HtmlTableRow(final HtmlTableCell cell) {
		this.cellsList = Arrays.asList(cell);
	}

	public HtmlTableRow(final List<HtmlTableCell> cellsList) {
		this.cellsList = cellsList;
	}

	public List<HtmlTableCell> getCellsList() {
		return this.cellsList;
	}

}