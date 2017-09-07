package com.ilanbenichou.filesmanager.report;

import com.ilanbenichou.filesmanager.service.impl.FindDuplicateFilesService;
import com.ilanbenichou.filesmanager.service.impl.FindNewFilesService;
import com.ilanbenichou.filesmanager.service.impl.GenerateReportService;
import com.ilanbenichou.filesmanager.service.impl.SynchronizeDirectoriesService;

public interface ReportVisitor {

	public void visit(final GenerateReportService service);

	public void visit(final FindNewFilesService service);

	public void visit(final FindDuplicateFilesService service);

	public void visit(final SynchronizeDirectoriesService service);

	public void writeAndOpenReport();

}