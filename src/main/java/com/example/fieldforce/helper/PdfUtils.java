package com.example.fieldforce.helper;

import com.example.fieldforce.entity.SaleOrderDetail;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class PdfUtils {

    static String SODheaders[] = new String[]{"Item Name", "Pieces", "Boxes", "Sale Price"};

    private BaseFont bfBold;
    private BaseFont bf;
    private BaseFont tf;

    public void createPDF(String path, String shopName, String orderDate, List<SaleOrderDetail> saleOrderDetails) {

        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();

        try {
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.addAuthor("bhanu");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("Bhanu");
            doc.addTitle("Customer Order");

            doc.add(new Paragraph("ShopName:" + shopName));
            doc.add(new Paragraph("OrderDate:" + orderDate));

            Rectangle one = new Rectangle(210, 780); // Set page size, this is thermal print size
            doc.setPageSize(one);
            doc.open();

            PdfContentByte cb = docWriter.getDirectContent();
            doc.add(createTable(saleOrderDetails));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (doc != null) {
                doc.close();
            }
            if (docWriter != null) {
                docWriter.close();
            }
        }
    }

    private void initializeFonts() {
        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            tf = BaseFont.createFont("tunga.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PdfPTable createTable(List<SaleOrderDetail> saleOrderDetails) throws DocumentException {

        PdfPTable table = new PdfPTable(SODheaders.length);
        table.setWidthPercentage(288 / 5.23f);
        table.setWidths(new int[]{2, 1, 1});
        PdfPCell cell;
        for (int i = 0; i < SODheaders.length; i++) {
            cell = new PdfPCell(new Phrase(SODheaders[i]));
            cell.setColspan(3);
            table.addCell(cell);
        }
        for (SaleOrderDetail saleOrderDetail : saleOrderDetails) {
            cell = new PdfPCell(new Phrase(saleOrderDetail.getItemName()));
            cell.setColspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(saleOrderDetail.getPieces()));
            cell.setColspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(saleOrderDetail.getBoxes()));
            cell.setColspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("" + saleOrderDetail.getSalePrice()));
            cell.setColspan(3);
            table.addCell(cell);
        }
        return table;
    }
}
