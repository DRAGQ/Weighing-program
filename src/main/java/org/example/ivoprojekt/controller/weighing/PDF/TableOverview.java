package org.example.ivoprojekt.controller.weighing.PDF;

import javafx.scene.layout.HBox;
import org.example.ivoprojekt.api.response.WeighingPrintResponse;
import org.example.ivoprojekt.api.response.WeighingTableOverviewResponse;
import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.pdf.BaseFont;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class TableOverview {
    //private List<WeighingTableOverviewResponse> weighing;
    private Document document;
    private PdfPTable table;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;

    public TableOverview(List<WeighingTableOverviewResponse> weighing, String timePeriod, ArrayList<String> filters) {
        this.document = new Document();
        this.totalIncome = new BigDecimal(0);
        this.totalExpense = new BigDecimal(0);
        createPDF(weighing, timePeriod, filters);
    }

    private void createPDF(List<WeighingTableOverviewResponse> weighing, String timePeriod, ArrayList<String> filters) {
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("tables.pdf"));
            document.open();

            document.add(createTitle());
            document.add(createTimePeriodParagraph(timePeriod));

            createTable();
            addRowsToTable(weighing);
            createSummary(weighing);
            document.add(table);
            //document.add(createDescriptionText(description));

            document.add(createResultTable());

            document.add(new Paragraph("Nastavenie filtra:", createFont(10)));
            for (String filter : filters) {
                document.add(new Paragraph(filter, createFont(10)));
            }


            this.document.close();
            openPdfFile();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private PdfPTable createResultTable() {
        float width = document.getPageSize().getWidth();
        PdfPTable table =  new PdfPTable(new float[]{20f, 20f, 20f, 20f, 20f, 20f});
        table.setTotalWidth(width - 72);
        table.setHorizontalAlignment(0);
        table.setLockedWidth(true);

        PdfPCell cell1 = createFooterCell("Prijem spolu: ", "regular");
        PdfPCell cell2 = createFooterCell(String.valueOf(totalIncome) + " kg", "");
        PdfPCell cell3 = createFooterCell("Vydaj spolu: ", "regular");
        PdfPCell cell4 = createFooterCell(String.valueOf(totalExpense) + " kg", "");
        PdfPCell cell5 = createFooterCell("Rozdiel +/-: ", "regular");
        PdfPCell cell6 = createFooterCell(String.valueOf(totalIncome.doubleValue() - totalExpense.doubleValue()) + " kg", "");

        cell1.setBorder(0);
        cell3.setBorder(0);
        cell5.setBorder(0);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        return table;
    }


    private PdfPCell createFooterCell(String text, String textFond) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, (textFond.equals("regular") ? createFont(10) : createBoldFont(13))));
        //PdfPCell cell = new PdfPCell(new Paragraph(text, createFont(10)));
        cell.setPaddingBottom(4);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private Paragraph createTitle() {
        Paragraph title = new Paragraph("Prehľad váženia", createFont(15));
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(10);
        return title;
    }

    private Paragraph createTimePeriodParagraph(String timePeriod) {
        Paragraph timePeriodP = new Paragraph("Časové obdobie: " + timePeriod, createFont(10));
        timePeriodP.setAlignment(Paragraph.ALIGN_CENTER);
        timePeriodP.setSpacingAfter(10);
        return timePeriodP;
    }

    public void addRowsToTable(List<WeighingTableOverviewResponse> response) {
        for (WeighingTableOverviewResponse r : response) {
            PdfPTable innerTableDate = new PdfPTable(1);
            PdfPCell dateCell = new PdfPCell(createCell(r.getDateTime(),16));
            PdfPCell timeCell = new PdfPCell(createCell(changeTimeFormat(r.getEntry()) + " - " + changeTimeFormat(r.getDeparture()),16));
            dateCell.setBorderWidthBottom(0);
            timeCell.setBorderWidthTop(0);

            innerTableDate.addCell(dateCell);
            innerTableDate.addCell(timeCell);
            PdfPCell outerCellDate = new PdfPCell(innerTableDate);
            outerCellDate.setBorder(0);
            this.table.addCell(outerCellDate);
/// /////////////////////////////////////////
            //addCellToTable(createCell(r.getDateTime()));
            addCellToTable(createCell(String.valueOf(r.getNumber()),16));
            addCellToTable(createCell(r.getType(),16));
            addCellToTable(createCell(r.getPartner(),16));
            addCellToTable(createCell(r.getVehicle(),16));
            addCellToTable(createCell(r.getMaterial(),16));

            PdfPTable innerTable = new PdfPTable(3);
            innerTable.addCell(createCell(String.valueOf(r.getGross()),16));
            innerTable.addCell(createCell(String.valueOf(r.getTara()),16));

            PdfPCell rightCell = createCell(String.valueOf(r.getNett()),16);
            rightCell.setBorderWidthRight(0.5f);
            innerTable.addCell(rightCell);

            PdfPCell outerCell = new PdfPCell(innerTable);
            outerCell.setBorder(0);
            table.addCell(outerCell);
        }
        Arrays.stream(table.getRows().getLast().getCells()).forEach(cell -> cell.setBorderWidthBottom(0.5f));
    }

    private void createSummary(List<WeighingTableOverviewResponse> response) {
        for (WeighingTableOverviewResponse r : response) {
            if (r.getType().equals("Príjem")) {

                this.totalIncome = this.totalIncome.add(r.getNett());
            } else {
                this.totalExpense = this.totalExpense.add(r.getNett());
            }
        }
        System.out.println("Total income: " + totalIncome);
        System.out.println("Total expense: " + totalExpense);
        System.out.println("Total difference: " + (totalIncome.doubleValue() - totalExpense.doubleValue()));
    }

    public String changeTimeFormat(String time) {
        DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter uiFormat = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime ltE = LocalTime.parse(time, dbFormat);
        time = (ltE.format(uiFormat));
        return time;
    }

    private void createTable() {
        float width = document.getPageSize().getWidth();
        float[] columnWidth = {130f, 100f, 80f, 200f, 100f, 100f, 200f};

        this.table = new PdfPTable(columnWidth);
        table.getDefaultCell().setBorder(0);
        table.setHorizontalAlignment(0);
        table.setTotalWidth(width - 72);
        table.setLockedWidth(true);
        createTableHeader();
    }

    private void createTableHeader() {
        addCellToTable(createCell("Dátum",20));
        addCellToTable(createCell("Číslo",20));
        addCellToTable(createCell("Typ",20));
        addCellToTable(createCell("Partner",20));
        addCellToTable(createCell("Vozidlo",20));
        addCellToTable(createCell("Materiál",20));

        createInnerTable();

    }

    private void addCellToTable(PdfPCell cell) {
        this.table.addCell(cell);
    }

    private void createInnerTable() {
        PdfPTable innerTable = new PdfPTable(3);

        PdfPCell innerCell1 = createCell("Hmotnosť",20);
        PdfPCell innerCell2 = createCell("Brutto",20);
        PdfPCell innerCell3 = createCell("Tara",20);
        PdfPCell innerCell4 = createCell("Netto",20);

        innerCell1.setColspan(3);

        innerCell1.setBorderWidthRight(0.5f);
        innerCell1.setBorderWidthBottom(0);
        innerCell3.setBorderWidthRight(0);
        innerCell4.setBorderWidthRight(0.5f);

        innerTable.addCell(innerCell1);
        innerTable.addCell(innerCell2);
        innerTable.addCell(innerCell3);
        innerTable.addCell(innerCell4);

        PdfPCell outerCell = new PdfPCell(innerTable);
        outerCell.setBorder(0);
        table.addCell(outerCell);
    }

    /*private void addInnerTable(PdfPCell cell) {

    }*/

    private PdfPCell createCell(String textInsideCell, int height) {
        PdfPCell cell = new PdfPCell(new Paragraph(textInsideCell, createFont(10)));
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        cell.setPaddingBottom(4);
        cell.setMinimumHeight(height);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private Paragraph createDescriptionText(String text) {
        Paragraph descriptionP = new Paragraph(text, createFont(10));
        descriptionP.setAlignment(Paragraph.ALIGN_LEFT);
        descriptionP.setSpacingAfter(10);
        return descriptionP;
    }

    private void openPdfFile() {
        try {
            File pdfFile = new File("tables.pdf");
            Desktop.getDesktop().open(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Font createFont(float size) {
        try {
            InputStream stream = getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf");
            BaseFont bf = BaseFont.createFont("Roboto-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, stream.readAllBytes(), null);
            return new Font(bf, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Font createBoldFont(float size) {
        try {
            InputStream stream = getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf");
            BaseFont bf = BaseFont.createFont("Roboto-Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, stream.readAllBytes(), null);
            return new Font(bf, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
