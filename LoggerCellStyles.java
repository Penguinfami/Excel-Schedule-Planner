
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;


public class LoggerCellStyles {

    public XSSFCellStyle headerStyle;

    public XSSFCellStyle textStyle;

    public XSSFCellStyle dateStyle;

    private XSSFWorkbook workbook;

    public LoggerCellStyles(XSSFWorkbook workbook) {

        this.workbook = workbook;
        headerStyle = workbook.createCellStyle();
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Verdana");
        font.setFontHeightInPoints(Short.parseShort("12"));
        headerStyle.setFont(font);


        textStyle = workbook.createCellStyle();
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        textStyle.setAlignment(HorizontalAlignment.CENTER);
        textStyle.setWrapText(true);
        textStyle.setShrinkToFit(true);

        dateStyle = workbook.createCellStyle();
        XSSFFont font2 = workbook.createFont();
        font2.setFontHeightInPoints(Short.parseShort("13"));
        dateStyle.setFont(font2);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);


    }


    public XSSFCellStyle getHeaderStyle() {
        return headerStyle;
    }

    public XSSFCellStyle getDateStyle() {
        return dateStyle;
    }

    public XSSFCellStyle getTextStyle() {
        return textStyle;
    }

    public XSSFCellStyle[] getStyleOrder() {
        XSSFCellStyle[] order = {
                getDateStyle(), getDateStyle(), getTextStyle(), getTextStyle(), getDateStyle(), getDateStyle()
        };
        return order;
    }

}



