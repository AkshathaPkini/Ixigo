import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class ExcelReadWrite {
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    XSSFCell cell;
    Row row;
    int rowCount;
    int columnCount;
    String cellText;
    FileInputStream fis;
    String dateFmt;
    public static ExcelReadWrite xlReadWrite;


    public XSSFSheet getSheet(String filepath,String sheetName) throws IOException {
        try {
            fis = new FileInputStream(filepath);
            workbook =new XSSFWorkbook(fis);
            sheet=workbook.getSheet(sheetName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(fis!=null){
                fis.close();
            }
        }
        return sheet;
    }
    // find whether sheets exists
    public boolean isSheetExist(String sheetName){
        int index=workbook.getSheetIndex(sheetName);
        if(index==-1)
            return false;
        else
            return true;
    }


    public int getRowCount() throws IOException {
        rowCount = sheet.getLastRowNum();
        return rowCount;
    }
    public int getColumnCount(int rownum) throws IOException {
        row = sheet.getRow(rownum);
        columnCount = row.getLastCellNum();// alternatively can use getPhysicalnumber of cells to get the actual filled cell count
        return columnCount;
    }


    public String getCellData(String colName,int rowNum) {
        int col_Num=0;
        row = sheet.getRow(0);
        DataFormatter df = new DataFormatter();
        df.addFormat("dd/mm/yyyy", new java.text.SimpleDateFormat("dd.MM.yyyy"));
        for(int colIter=0;colIter<row.getLastCellNum();colIter++) {
            if(row.getCell(colIter).getStringCellValue().trim().equals(colName.trim())) {
                col_Num=colIter;
                break;
            }
        }
        row =sheet.getRow(rowNum);
        if (row!=null) {
            cell = (XSSFCell) row.getCell(col_Num);
            if (cell.getCellStyle().getDataFormat() == 14) { //default short date without explicit formatting
                Date date = cell.getDateCellValue();
                dateFmt = "dd.mm.yyyy"; //default date format for this
                cellText =new CellDateFormatter(dateFmt).format(date);
                dateFmt = cell.getCellStyle().getDataFormatString(); //other data formats with explicit formatting
            }
            cellText = df.formatCellValue(cell).trim();
        }
        else {
            cellText ="";
        }
        return cellText;
    }

    public String getCellData(int colNum,int rowNum) {
        DataFormatter df = new DataFormatter();
        df.addFormat("dd/mm/yyyy", new java.text.SimpleDateFormat("dd.MM.yyyy"));
        row =sheet.getRow(rowNum);
        if( row != null) {
            cell = (XSSFCell) row.getCell(colNum);
            if (cell.getCellStyle().getDataFormat() == 14) { //default short date without explicit formatting
                Date date = cell.getDateCellValue();
                dateFmt = "dd.mm.yyyy"; //default date format for this
                cellText =new CellDateFormatter(dateFmt).format(date);
                dateFmt = cell.getCellStyle().getDataFormatString(); //other data formats with explicit formatting
            }
            cellText = df.formatCellValue(cell).trim();
        }
        else {
            cellText ="";
        }
        return cellText;
    }
}
