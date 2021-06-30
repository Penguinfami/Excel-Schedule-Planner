
import java.awt.Color;

public class LoggerTableFormat {

    public static class TableColors {
        static Color MISSED = Color.decode("#ff6161"); // red
        static Color TODAY = Color.GREEN;
        static Color IN_PROGRESS = Color.decode("#aaffb4"); // light green
        static Color COMPLETE = Color.decode("#13daf6"); // blue
        static Color NORMAL = Color.WHITE;
        static Color FILTER_BUTTON = Color.decode("#eaeaea");
        static Color TODAY_HEADING = Color.decode("#007309"); // darker green
        static Color IN_PROGRESS_HEADING = Color.decode("#00C410"); // lighter green
        static Color MISSED_HEADING = Color.decode("#BD0000"); // darker red

    }

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

    public static int[] todaysActivitiesWidths = {300,290};

}
