
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.*;

public class LoggerTableFormat {

    public enum ValueType {
        STRING,
        DATE,
    }

    public static class RowHeight {
        static int headerHeight = 45;
        static int activityHeight = 40;
    }

    ;

    public enum ValueRole {
        DATE_START,
        DATE_END,
        DATE_CREATED,
        DATE_COMPLETED,
        TITLE,
        INFO,
    }

    public static ValueType[] typeOrder = {ValueType.DATE, ValueType.DATE, ValueType.STRING, ValueType.STRING, ValueType.DATE, ValueType.DATE};
    public static ValueRole[] roleOrder =
            {ValueRole.DATE_START, ValueRole.DATE_END, ValueRole.TITLE, ValueRole.INFO, ValueRole.DATE_CREATED, ValueRole.DATE_COMPLETED};

    public static String[] headerTitles = {"Start Date", "End Date",
            "Name", "Info", "Date Created", "Date Completed"};

    public static int[] headerWidths = {90, 90, 170, 310, 100, 120};

}
