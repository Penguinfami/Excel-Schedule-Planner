

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
import javax.swing.JButton;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;

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
    private JButton displayIncompleteButton;
    private JButton displayCompleteButton;
    private JButton displayAllButton;
    private LegendPanel legend;
    private JScrollPane pane;


    public ProgressPlanTable(LoggerButtonListener listener) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.displayAllButton = new JButton("ALL");
        displayAllButton.addActionListener(listener);
        displayAllButton.setBackground(LoggerTableFormat.TableColors.FILTER_BUTTON);
        this.displayIncompleteButton = new JButton("INCOMPLETE ONLY");
        displayIncompleteButton.addActionListener(listener);
        displayIncompleteButton.setBackground(LoggerTableFormat.TableColors.FILTER_BUTTON);
        this.displayCompleteButton = new JButton("COMPLETE ONLY");
        displayCompleteButton.addActionListener(listener);
        displayCompleteButton.setBackground(LoggerTableFormat.TableColors.FILTER_BUTTON);
        legend = new LegendPanel();
        legend.setPreferredSize(new Dimension(900,30));
        legend.setLayout(new FlowLayout(FlowLayout.RIGHT));
        legend.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        legend.add(displayAllButton);
        legend.add(displayCompleteButton);
        legend.add(displayIncompleteButton);
        table = new JTable();
        pane = new JScrollPane();
        this.add(table);
        this.add(pane);
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
    }

    public void createTable( String[][] strArray) {
        this.removeAll();

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
                    c.setBackground(LoggerTableFormat.TableColors.COMPLETE);
                } else {
                    if (todaysDate.compareTo(endDate) == -1) {
                        c.setBackground(LoggerTableFormat.TableColors.MISSED); // light red
                    } else if (todaysDate.compareTo(endDate) == 0) {
                        c.setBackground(LoggerTableFormat.TableColors.TODAY);
                    } else if ((todaysDate.compareTo(startDate) <= 0 && todaysDate.compareTo(endDate) > 0)) {
                        c.setBackground(LoggerTableFormat.TableColors.IN_PROGRESS);
                    } else {
                        c.setBackground(LoggerTableFormat.TableColors.NORMAL);
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

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //table.setPreferredScrollableViewportSize(new Dimension(800, 430));

        pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(900,500));
        this.add(legend);
        this.add(pane);

        this.revalidate();


    }

    private static class LegendPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            this.setLayout(null);
            Font legendFont = new Font("Arial", Font.BOLD, 12);

            // draw the squares
            g.setColor(LoggerTableFormat.TableColors.COMPLETE);
            g.fillRect(50, 10, 15, 15);
            g.setColor(LoggerTableFormat.TableColors.TODAY);
            g.fillRect(190, 10, 15, 15);
            g.setColor(LoggerTableFormat.TableColors.IN_PROGRESS);
            g.fillRect(330, 10, 15, 15);
            g.setColor(LoggerTableFormat.TableColors.MISSED);
            g.fillRect(470, 10, 15, 15);

            // draw the square black outline
            g.setColor(Color.BLACK);
            g.drawRect(50, 10, 15, 15);
            g.drawRect(190, 10, 15, 15);
            g.drawRect(330, 10, 15, 15);
            g.drawRect(470, 10, 15, 15);


            // write the legend
            g.setFont(legendFont);
            g.drawString("Complete", 70, 22);
            g.drawString("Today", 210, 22);
            g.drawString("In Progress", 350, 22);
            g.drawString("Late", 490, 22);

        }
    }


}




