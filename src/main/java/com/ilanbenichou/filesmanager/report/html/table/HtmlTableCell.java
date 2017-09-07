package com.ilanbenichou.filesmanager.report.html.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ilanbenichou.filesmanager.date.DateHelper;
import com.ilanbenichou.filesmanager.file.FilesHelper;
import com.ilanbenichou.filesmanager.report.html.builder.CssClassesEnum;
import com.ilanbenichou.filesmanager.report.html.builder.HtmlReportBuilderHelper;

import j2html.tags.ContainerTag;

public class HtmlTableCell {

	private String id;

	private List<CssClassesEnum> cssClassesList;

	private String onClickJsAction;

	private Integer colspan;

	private final Value value;

	private static class Value {

		private final String valueString;

		private final ContainerTag valueTag;

		private Value(final String valueString, final ContainerTag valueTag) {
			this.valueString = valueString;
			this.valueTag = valueTag;
		}

	}

	public HtmlTableCell(final String valueString) {
		this.value = new Value(valueString, null);
	}

	public HtmlTableCell(final boolean valueBoolean) {
		this(valueBoolean ? "Yes" : "No");
	}

	public HtmlTableCell(final Date valueDate) {
		this(DateHelper.formatYyyyMmDdHhMmSs(valueDate));
	}

	public HtmlTableCell(final int valueInt) {
		this(String.valueOf(valueInt));
	}

	public HtmlTableCell(final long valueLong, final boolean isSize) {
		this(isSize ? FilesHelper.fileSizeToPrettyString(valueLong) + " bytes (" + FilesHelper.fileSizeToString(valueLong) + ")" : String.valueOf(valueLong));
	}

	public HtmlTableCell(final String valueLink, final String onClickJsAction) {
		this(valueLink);
		this.addCssClass(CssClassesEnum.LINK);
		this.onClickJsAction = onClickJsAction;
	}

	public HtmlTableCell(final HtmlTable table) {
		this.value = new Value(null, HtmlReportBuilderHelper.buildTableTag(table));
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public List<CssClassesEnum> getCssClassesList() {
		return this.cssClassesList;
	}

	public HtmlTableCell addCssClass(final CssClassesEnum cssClass) {
		if (this.cssClassesList == null) {
			this.cssClassesList = new ArrayList<>();
		}
		this.cssClassesList.add(cssClass);

		return this;

	}

	public String getOnClickJsAction() {
		return this.onClickJsAction;
	}

	public Integer getColspan() {
		return this.colspan;
	}

	public void setColspan(final Integer colspan) {
		this.colspan = colspan;
	}

	public boolean hasText() {
		return this.value.valueString != null;
	}

	public String getText() {
		return this.value.valueString;
	}

	public ContainerTag getTag() {
		return this.value.valueTag;
	}

}