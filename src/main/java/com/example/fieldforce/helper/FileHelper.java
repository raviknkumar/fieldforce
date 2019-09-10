package com.example.fieldforce.helper;

public class FileHelper {
    static String basePath = "src/files/";
    static String UNDERSORE = "_";
    static String PDF_EXTENSION = ".pdf";
    static String EXCEL_EXTENSION = ".xlsx";
    public static String getFilePath(String shopName, String orderDate, String type){
        switch (type) {
            case "excel":
                return basePath + shopName + "_" + orderDate + EXCEL_EXTENSION;
            case "pdf":
                return basePath + shopName + "_" + orderDate + PDF_EXTENSION;
        }
        return null;
    }
}
