package com.ilanbenichou.filesmanager.report.html.builder;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.i;
import static j2html.TagCreator.link;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.p;
import static j2html.TagCreator.script;
import static j2html.TagCreator.title;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.ilanbenichou.filesmanager.FilesManager;
import com.ilanbenichou.filesmanager.bean.FmFile;
import com.ilanbenichou.filesmanager.config.PropertiesLoader;
import com.ilanbenichou.filesmanager.date.DateHelper;
import com.ilanbenichou.filesmanager.report.html.builder.HtmlReportBuilderHelper.SectionColorsNumberEnum;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTable;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableCell;
import com.ilanbenichou.filesmanager.report.html.table.HtmlTableRow;
import com.ilanbenichou.filesmanager.service.Service;

import j2html.tags.ContainerTag;

public abstract class HtmlReportBuilder<T extends Service> {

	private final T service;

	private Date reportDate;

	private ContainerTag htmlTag;

	private static final String STYLESHEET_REL = "stylesheet";

	private static final String TEXT_CSS_TYPE = "text/css";

	private static final String TEXT_JS_TYPE = "text/javascript";

	protected HtmlReportBuilder(final T service) {
		this.service = service;
	}

	private ContainerTag buildHeadTag() {

		ContainerTag headTag = head();

		headTag.with(title("FM Report | " + this.service.getServiceEnum().getDescription()));

		headTag.with(meta().withCharset("UTF-8"));

		headTag.with(meta().attr("http-equiv", "Content-Type").attr("content", "text/html; charset=UTF-8"));
		headTag.with(meta().attr("http-equiv", "X-UA-Compatible").attr("content", "IE=edge"));
		headTag.with(meta().withName("viewport").withContent("width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"));

		headTag.with(meta().withName("description").withContent("Files Manager HTML report"));

		headTag.with(meta().withName("author").withContent("Ilan BENICHOU"));

		headTag.with(meta().withName("msapplication-TileImage").withContent(HtmlReportBuilderHelper.FLAVICON_PATH));
		headTag.with(link().withRel("apple-touch-icon-precomposed").withHref(HtmlReportBuilderHelper.FLAVICON_PATH));
		headTag.with(link().withRel("icon").withHref(HtmlReportBuilderHelper.FLAVICON_PATH).attr("sizes", "32x32"));
		headTag.with(link().withRel("shortcut icon").withHref(HtmlReportBuilderHelper.FLAVICON_PATH).withType("images/x-icon"));

		String cssDirectoryPath = PropertiesLoader.getInstance().getProperties().getReportsResourcesDirectoryPath() + "/css/";

		headTag.with(link().withRel(HtmlReportBuilder.STYLESHEET_REL).withType(HtmlReportBuilder.TEXT_CSS_TYPE).withHref(cssDirectoryPath + "font-awesome.min.css"));
		headTag.with(link().withRel(HtmlReportBuilder.STYLESHEET_REL).withType(HtmlReportBuilder.TEXT_CSS_TYPE).withHref(cssDirectoryPath + "bootstrap.min.css"));
		headTag.with(link().withRel(HtmlReportBuilder.STYLESHEET_REL).withType(HtmlReportBuilder.TEXT_CSS_TYPE).withHref(cssDirectoryPath + "aos.css"));
		headTag.with(link().withRel(HtmlReportBuilder.STYLESHEET_REL).withType(HtmlReportBuilder.TEXT_CSS_TYPE).withHref(cssDirectoryPath + "fmreport.css"));
		headTag.with(link().withRel(HtmlReportBuilder.STYLESHEET_REL).withType(HtmlReportBuilder.TEXT_CSS_TYPE).withHref(cssDirectoryPath + "header.css"));
		headTag.with(link().withRel(HtmlReportBuilder.STYLESHEET_REL).withType(HtmlReportBuilder.TEXT_CSS_TYPE).withHref(cssDirectoryPath + "footer.css"));
		headTag.with(link().withRel(HtmlReportBuilder.STYLESHEET_REL).withType(HtmlReportBuilder.TEXT_CSS_TYPE).withHref(cssDirectoryPath + "media.css"));
		headTag.with(link().withRel(HtmlReportBuilder.STYLESHEET_REL).withType(HtmlReportBuilder.TEXT_CSS_TYPE).withHref(cssDirectoryPath + "jquery.dataTables.min.css"));

		return headTag;

	}

	protected abstract void completeGlobalInformationsTag(final ContainerTag globalInformationsTag, final T service);

	protected abstract void completeBodyTag(final ContainerTag bodyTag, final T service);

	private ContainerTag buildBodyTag() {

		ContainerTag bodyTag = body().withId("page-top").attr("data-spy", "scroll");

		ContainerTag headerTitleTag = div().withId("headerTitle").attr("data-aos", "fade-right");
		headerTitleTag.attr("data-aos-delay", "200");
		headerTitleTag.withText(this.service.getServiceEnum().getDescription());

		ContainerTag headerSubTitleTag = div().withId("headerSubTitle").attr("data-aos", "fade-left");
		headerSubTitleTag.attr("data-aos-delay", "300");
		headerSubTitleTag.withText("Files Manager | Version " + FilesManager.buildVersion());

		ContainerTag headerTitleContainerTag = div().withId("headerTitleContainer");
		headerTitleContainerTag.with(div(div().with(headerTitleTag).with(headerSubTitleTag)));

		ContainerTag headerTag = div().withId("header").withClass("c_section c_section_color_" + SectionColorsNumberEnum.ONE.getValue());
		headerTag.with(headerTitleContainerTag);

		bodyTag.with(headerTag);

		ContainerTag globalInformationsTag = HtmlReportBuilderHelper.buildSectionTag("Global informations", SectionColorsNumberEnum.TWO).withId("globalInformations");
		bodyTag.with(globalInformationsTag);
		this.completeGlobalInformationsTag(globalInformationsTag, this.service);

		this.completeBodyTag(bodyTag, this.service);

		bodyTag.with(a().withHref("javascript:").withId("returnToTop").with(i().withClass("fa fa-chevron-up").attr("aria-hidden", "true")));

		ContainerTag footerTextTag = div().withClass("fnav");
		footerTextTag.with(p().withText("Copyright @ 2017 | Files Manager by Ilan BENICHOU"));
		footerTextTag.with(p(a().withHref("http://www.ilan-benichou.com").withTarget("_blank").withText("www.ilan-benichou.com")));

		ContainerTag footerTag = div().withId("footer");
		footerTag.with(div().withClass("container text-center").with(footerTextTag));
		bodyTag.with(footerTag);

		String jsDirectoryPath = PropertiesLoader.getInstance().getProperties().getReportsResourcesDirectoryPath() + "/js/";

		bodyTag.with(script().withType(HtmlReportBuilder.TEXT_JS_TYPE).withSrc(jsDirectoryPath + "jquery-1.12.4.min.js"));
		bodyTag.with(script().withType(HtmlReportBuilder.TEXT_JS_TYPE).withSrc(jsDirectoryPath + "bootstrap.min.js"));
		bodyTag.with(script().withType(HtmlReportBuilder.TEXT_JS_TYPE).withSrc(jsDirectoryPath + "jquery.dataTables.min.js"));
		bodyTag.with(script().withType(HtmlReportBuilder.TEXT_JS_TYPE).withSrc(jsDirectoryPath + "aos.js"));
		bodyTag.with(script().withType(HtmlReportBuilder.TEXT_JS_TYPE).withSrc(jsDirectoryPath + "fmreport.js"));

		return bodyTag;

	}

	protected static ContainerTag buildSourceFilesListSectionTag(final String title, final SectionColorsNumberEnum sectionColorNumber, final Set<FmFile> filesSet) {
		return HtmlReportBuilder.buildFilesListSectionTag(title, sectionColorNumber, filesSet, "openSourceFile");
	}

	public static ContainerTag buildFilesListSectionTag( //
			final String title, //
			final SectionColorsNumberEnum sectionColorNumber, //
			final Set<FmFile> filesSet, //
			final String openJsFunction //
	) {

		ContainerTag filesListSectionTag = HtmlReportBuilderHelper.buildSectionTag(title, sectionColorNumber);

		List<String> columnsNameList = Arrays.asList("Relative path", "Last update", "Size", "Mime type");

		List<HtmlTableRow> rowsList = new ArrayList<>();

		filesSet.forEach(

				file -> {

					HtmlTableCell relativePathCell = openJsFunction == null ? //
					new HtmlTableCell(file.getInitialRelativePath().toString()) //
							: //
					new HtmlTableCell(file.getInitialRelativePath().toString(), openJsFunction + "(this)");

					rowsList.add( //
							new HtmlTableRow( //
									Arrays.asList( //
											relativePathCell, //
											new HtmlTableCell(file.getLastModifiedDate()), //
											new HtmlTableCell(file.getSize(), true), new HtmlTableCell(file.getMimeType())

									) //
					) //
					);

				}

		);

		filesListSectionTag.with(HtmlReportBuilderHelper.buildTableTag(new HtmlTable(columnsNameList, rowsList)));

		return filesListSectionTag;

	}

	protected final void build() {

		this.reportDate = DateHelper.nowDate();

		this.htmlTag = html().with(this.buildHeadTag());
		this.htmlTag.with(this.buildBodyTag());

	}

	protected final T getService() {
		return this.service;
	}

	protected final Date getReportDate() {
		return this.reportDate;
	}

	protected final ContainerTag getHtmlTag() {
		return this.htmlTag;
	}

}