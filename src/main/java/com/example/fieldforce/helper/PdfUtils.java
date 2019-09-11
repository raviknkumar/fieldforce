package com.example.fieldforce.helper;

import com.example.fieldforce.entity.SaleOrderDetail;
import com.example.fieldforce.model.ItemDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import javax.swing.text.StyleConstants;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PdfUtils {

    static String SODheaders[] = new String[]{"Item Name", "Pieces", "Boxes", "Price", "Tax", "Amount"};

    private BaseFont bfBold;
    private BaseFont bf;
    private BaseFont tf;
    private Integer HEADER_FONT_SIZE = 16;

    public void createPDF(String path, String shopName, String orderDate, List<SaleOrderDetail> saleOrderDetails
            , Map<Integer, ItemDto> itemIdToItemMap) {

        RectangleReadOnly rectangleReadOnly = new RectangleReadOnly(297.5F, 842.0F);
        Document doc = new Document(rectangleReadOnly);

        PdfWriter docWriter = null;
        initializeFonts();

        try {
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();
            doc.addAuthor("bhanu");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("Bhanu");
            doc.addTitle("Customer Order");

            Font f = new Font();
            f.setStyle(Font.BOLD);
            f.setSize(HEADER_FONT_SIZE);
            Paragraph p = new Paragraph(shopName, f);
            p.setAlignment(Element.ALIGN_CENTER);
            doc.add(p);

            doc.add(new Paragraph("OrderDate:" + orderDate));
            doc.add(new Paragraph("\n"));

            Rectangle one = new Rectangle(210, 780); // Set page size, this is thermal print size
            doc.setPageSize(one);

            setSaleOrderDetails(saleOrderDetails, itemIdToItemMap);

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

    private void setSaleOrderDetails(List<SaleOrderDetail> saleOrderDetails, Map<Integer, ItemDto> itemIdToItemMap) {
        for (SaleOrderDetail saleOrderDetail : saleOrderDetails) {
            ItemDto itemDto = itemIdToItemMap.get(saleOrderDetail.getItemId());
            saleOrderDetail.setOriginalPrice(itemDto.getBoxPrice());
            saleOrderDetail.setTaxPrice(itemDto.getTaxPercent());
        }
    }

    private void initializeFonts() {
        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PdfPTable createTable(List<SaleOrderDetail> saleOrderDetails) throws DocumentException {

        PdfPTable table = new PdfPTable(SODheaders.length);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 1, 1, 1, 1, 1});

        PdfPCell cell;

        Font fontH1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

        for (String soDheader : SODheaders) {
            cell = new PdfPCell(new Phrase(soDheader, fontH1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderColor(BaseColor.DARK_GRAY);
            table.addCell(cell);
        }

        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

        for (SaleOrderDetail saleOrderDetail : saleOrderDetails) {
            cell = createCell(saleOrderDetail.getItemName(), font);
            table.addCell(cell);
            Integer pieces = saleOrderDetail.getPieces();
            cell = createCell(pieces!=null ? pieces.toString() : "0", font);
            table.addCell(cell);
            Integer boxes = saleOrderDetail.getBoxes();
            cell = createCell(boxes!=null ? boxes.toString() : "0", font);
            table.addCell(cell);
            //price
            Double originalPrice = saleOrderDetail.getOriginalPrice();
            String price = originalPrice!=null ? originalPrice.toString() : "NA";
            cell = createCell(price, font);
            table.addCell(cell);
            //tax price -> tax %
            Double tax = saleOrderDetail.getTaxPrice();
            String taxPrice = tax!=null ? tax.toString() + "%" : "NA";
            cell = createCell(taxPrice, font);
            table.addCell(cell);
            //amount
            Double salePrice = saleOrderDetail.getSalePrice();
            String salePriceStr = salePrice!=null ? salePrice.toString() : "NA";
            cell = createCell(salePriceStr, font);
            table.addCell(cell);
        }
        return table;
    }

    private PdfPCell createCell(String text, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPaddingLeft(2F);
        cell.setBorderColor(BaseColor.DARK_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
}
