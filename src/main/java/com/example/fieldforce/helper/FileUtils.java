package com.example.fieldforce.helper;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtils {

    public static void saveFile(String filePath, XSSFWorkbook workbook) throws Exception{

        if(filePath == null || filePath.isEmpty())
            throw new Exception("File Path Empty");
        if(workbook == null)
            throw new Exception("WorkBook is Empty");

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        FileOutputStream outputStream = new FileOutputStream(file, false);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

        waitForFileCreation(file);
    }

    public static void saveFile(String filePath, byte[] fileBytes) throws Exception{
        if(filePath == null || filePath.isEmpty())
            throw new Exception("File Path Empty");

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        FileOutputStream outputStream = new FileOutputStream(file, false);
        outputStream.write(fileBytes);
        outputStream.flush();
        outputStream.close();
        waitForFileCreation(file);

    }

    public static boolean isFileExists(String filePath){

        File odlFile = new File(filePath);
        return odlFile.exists();
    }

    private static void waitForFileCreation(File odlInputFile) throws Exception {
        while(!odlInputFile.exists()){
            Thread.sleep(100);
        }
    }
}
