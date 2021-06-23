

import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Dimension;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.util.Calendar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public class ProgressPlanTable extends JPanel {

    private JTable table;
    // String dS, String tS, String dE, String tE,
// String tOA,String aI, String dC, String tC, String dCo, String tCo
    private String[] columnNames = LoggerTableFormat.headerTitles;

    private int dateCompletedIndex;
    private int titleIndex;
    private int infoIndex;
    private int endDateIndex;
    private int startDateIndex;

    private static class TableColors {
        static Color MISSED = Color.decode("#ff6161"); // red
        static Color TODAY = Color.GREEN;
        static Color IN_PROGRESS = Color.decode("#aaffb4"); // light green
        static Color COMPLETE = Color.decode("#13daf6"); // blue
        static Color NORMAL = Color.WHITE;


    }

    public ProgressPlanTable() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (int i = 0; i < LoggerTableFormat.roleOrder.length; i++) {
            switch (LoggerTableFormat.roleOrder[i]) {
                case DATE_COMPLETED:
                    dateCompletedIndex = i;
                    break;
                case TITLE:
                    titleIndex = i;
                    break;
                case INFO:
                    infoIndex = i;
                    break;
                case DATE_START:
                    startDateIndex = i;
                    break;
                case DATE_END:
                    endDateIndex = i;
                    break;
            }
        }
    }

    public ProgressPlanTable(String[][] strArray) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (int i = 0; i < LoggerTableFormat.roleOrder.length; i++) {
            switch (LoggerTableFormat.roleOrder[i]) {
                case DATE_COMPLETED:
                    dateCompletedIndex = i;
                    break;
                case TITLE:
                    titleIndex = i;
                    break;
                case INFO:
                    infoIndex = i;
                    break;
                case DATE_START:
                    startDateIndex = i;
                    break;
                case DATE_END:
                    endDateIndex = i;
                    break;
            }
        }
        createTable(strArray);

    }

    public void createTable(final String[][] strArray) {

        LegendPanel legend = new LegendPanel();

        // add the html to wrap the title/info strings in the table
        for (int i = 0; i < strArray.length; i++) {
            strArray[i][titleIndex] = "<HTML><b>" + strArray[i][titleIndex] + "</b></HTML>";
            strArray[i][infoIndex] = "<HTML><b>" + strArray[i][infoIndex] + "</b></HTML>";
        }

        table = new JTable(strArray, columnNames) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);


                Date endDate = new Date(strArray[row][endDateIndex]);
                Date startDate = new Date(strArray[row][startDateIndex]);

                Calendar today = Calendar.getInstance();

                int[] intArray = {today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH)};
                Date todaysDate = new Date(intArray);

                if (strArray[row][dateCompletedIndex] != null && !strArray[row][dateCompletedIndex].equals("")) {
                    c.setBackground(TableColors.COMPLETE);
                } else {
                    if (todaysDate.compareTo(endDate) == -1) {
                        c.setBackground(TableColors.MISSED); // light red
                    } else if (todaysDate.compareTo(endDate) == 0) {
                        c.setBackground(TableColors.TODAY);
                    } else if ((todaysDate.compareTo(startDate) <= 0 && todaysDate.compareTo(endDate) > 0)) {
                        c.setBackground(TableColors.IN_PROGRESS);
                    } else {
                        c.setBackground(TableColors.NORMAL);
                    }
                }

                return c;
            }
        };

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        table.setModel(model);
        table.setRowHeight(80);

        for (int i = 0; i < strArray.length; i++) {
            model.insertRow(i, strArray[i]);
            if (strArray[i][titleIndex].length() > 50) {
                table.setRowHeight(i, 100 + (strArray[i][titleIndex].length()) / 3);
            }
            if (strArray[i][infoIndex].length() > 100) {
                table.setRowHeight(i, 80 + (strArray[i][infoIndex].length()) / 3);
            }
        }

        for (int i = 0; i < columnNames.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(LoggerTableFormat.headerWidths[i]);

        }

        table.setPreferredScrollableViewportSize(new Dimension(800, 470));

        this.removeAll();

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane pane = new JScrollPane(table);

        this.add(legend);
        this.add(Box.createRigidArea(new Dimension(10, 7)));
        this.add(pane);

        legend.repaint();

    }

    private static class LegendPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            this.setLayout(null);
            Font legendFont = new Font("Arial", Font.BOLD, 12);

            // draw the squares
            g.setColor(TableColors.COMPLETE);
            g.fillRect(50, 0, 15, 15);
            g.setColor(TableColors.TODAY);
            g.fillRect(260, 0, 15, 15);
            g.setColor(TableColors.IN_PROGRESS);
            g.fillRect(470, 0, 15, 15);
            g.setColor(TableColors.MISSED);
            g.fillRect(680, 0, 15, 15);

            // draw the square black outline
            g.setColor(Color.BLACK);
            g.drawRect(50, 0, 15, 15);
            g.drawRect(260, 0, 15, 15);
            g.drawRect(470, 0, 15, 15);
            g.drawRect(680, 0, 15, 15);


            // write the legend
            g.setFont(legendFont);
            g.drawString("Complete", 70, 12);
            g.drawString("Today", 280, 12);
            g.drawString("In Progress", 490, 12);
            g.drawString("Late", 700, 12);

        }
    }

    private class Date {
        private int year;
        private int month;
        private int day;

        Date(String dateString) {
            String[] dateArray = dateString.split("-");
            year = Integer.parseInt(dateArray[0]);
            month = Integer.parseInt(dateArray[1]);
            day = Integer.parseInt(dateArray[2]);
        }

        Date(int[] intArray) {
            year = intArray[0];
            month = intArray[1];
            day = intArray[2];
        }


        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }

        // if the date param d is after this date, return 1
        // if the date param d is before this date, return -1;
        // if they are the same date, return 0;
        public int compareTo(Date d) {
            if (getYear() < d.getYear()) {
                return 1;
            } else if (getYear() == d.getYear()) {
                if (getMonth() < d.getMonth()) {
                    return 1;
                } else if (getMonth() == d.getMonth()) {
                    if (getDay() < d.getDay()) {
                        return 1;
                    } else if (getDay() == d.getDay()) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }
    }
}




