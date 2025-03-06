package com.Project.core.servlets;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.methods=POST",
        "sling.servlet.paths=/bin/process-excel-download",
        "sling.servlet.extensions=xlsx",
        "sling.servlet.selectors=download"
    }
)
public class ExcelProcessingDownloadServlet extends SlingAllMethodsServlet {
    private static final Logger log = LoggerFactory.getLogger(ExcelProcessingDownloadServlet.class);

    @Activate
    protected void activate() {
        log.info("ExcelProcessingDownloadServlet ACTIVATED!");
    }

    @Deactivate
    protected void deactivate() {
        log.info(" ExcelProcessingDownloadServlet DEACTIVATED!");
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        log.info("🔹 Received POST request to process Excel file...");

        try (InputStream fileInputStream = request.getInputStream();
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            log.info(" Successfully read input Excel file.");

            // Step 1: Read Sheet1 and create a mapping of Group IDs to Group Names
            Sheet sheet1 = workbook.getSheetAt(0);
            log.info(" Processing Sheet1 (Group ID - Group Name Mapping)...");

            Map<String, String> groupMap = new HashMap<>();
            for (Row row : sheet1) {
                if (row.getCell(0) != null && row.getCell(1) != null) {
                    String groupId = row.getCell(0).getStringCellValue();
                    String groupName = row.getCell(1).getStringCellValue();
                    groupMap.put(groupId, groupName);
                    log.debug("🔹 Mapped Group ID: {} → Group Name: {}", groupId, groupName);
                }
            }
            log.info(" Completed processing Sheet1. Total mappings: {}", groupMap.size());

            // Step 2: Read Sheet2 and update Group Names
            Sheet sheet2 = workbook.getSheetAt(1);
            log.info(" Processing Sheet2 (Updating Group Names)...");

            int updatedRows = 0;
            for (Row row : sheet2) {
                Cell groupIdCell = row.getCell(0);
                if (groupIdCell != null) {
                    String groupIds = groupIdCell.getStringCellValue();
                    List<String> groupNames = new ArrayList<>();

                    for (String id : groupIds.split(",")) {
                        String trimmedId = id.trim();
                        if (groupMap.containsKey(trimmedId)) {
                            groupNames.add(groupMap.get(trimmedId));
                        }
                    }

                    if (!groupNames.isEmpty()) {
                        Cell groupNameCell = row.createCell(1, CellType.STRING);
                        groupNameCell.setCellValue(String.join(", ", groupNames));
                        log.debug(" Updated Row: Group IDs '{}' → Group Names '{}'", groupIds, groupNames);
                        updatedRows++;
                    }
                }
            }
            log.info("Completed processing Sheet2. Rows updated: {}", updatedRows);

            // Step 3: Save the processed file to the Downloads folder
            String userHome = System.getProperty("user.home");
            String downloadPath = userHome + File.separator + "Downloads" + File.separator + "Updated_Excel.xlsx";

            try (FileOutputStream fileOutputStream = new FileOutputStream(downloadPath)) {
                workbook.write(fileOutputStream);
            }

            log.info(" Successfully saved updated file to: {}", downloadPath);

            // Send response
            response.setContentType("text/plain");
            response.getWriter().write(" File processed and saved to: " + downloadPath);
            log.info("🔹 Response sent to client.");

        } catch (Exception e) {
            log.error(" Error processing Excel file", e);
            response.getWriter().write(" Error processing Excel file: " + e.getMessage());
        }
    }
}