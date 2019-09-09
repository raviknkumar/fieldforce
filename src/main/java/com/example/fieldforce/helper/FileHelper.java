package com.example.fieldforce.helper;

public class FileHelper {
    public static String getFilePath(String shopName, String orderDate){
        return "src/files/"+shopName+"_"+orderDate+".xlsx";
    }
}
