package com.ilanbenichou.filesmanager.report.html.table;

import java.util.List;

public final class HtmlTable {

	private final List<String> columnsNameList;

	private final List<HtmlTableRow> rowsList;

	public HtmlTable(final List<String> columnsNameList, final List<HtmlTableRow> rowsList) {
		this.columnsNameList = columnsNameList;
		this.rowsList = rowsList;
	}

	public HtmlTable(final List<HtmlTableRow> rowsList) {
		this(null, rowsList);
	}

	public List<String> getColumnsNameList() {
		return this.columnsNameList;
	}

	public List<HtmlTableRow> getRowsList() {
		return this.rowsList;
	}

}