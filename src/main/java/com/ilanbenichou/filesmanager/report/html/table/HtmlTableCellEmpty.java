package com.ilanbenichou.filesmanager.report.html.table;

import com.ilanbenichou.filesmanager.report.html.builder.CssClassesEnum;

public final class HtmlTableCellEmpty extends HtmlTableCell {

	public HtmlTableCellEmpty() {
		super("");
		super.addCssClass(CssClassesEnum.EMPTY);
	}

}