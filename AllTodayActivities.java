
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class AllTodayActivities extends JPanel{

    private JTable table,table2 , table3;
    private String[] headers;
    private JScrollPane scrollPane, scrollPane2, scrollPane3;
    private JLabel heading1, heading2, heading3;

    public AllTodayActivities(){
        this.setLayout(new BorderLayout());
        headers = new String[]{"Title", "Info"};
        heading1 = new JLabel("<html><h1>Activities Ending Today</h1><html>");
        heading1.setHorizontalAlignment(SwingConstants.CENTER);
        heading1.setForeground(LoggerTableFormat.TableColors.TODAY_HEADING);
        heading2 = new JLabel("<html><h1>Activities In Progress</h1><html>");
        heading2.setHorizontalAlignment(SwingConstants.CENTER);
        heading2.setForeground(LoggerTableFormat.TableColors.IN_PROGRESS_HEADING);
        heading3 = new JLabel("<html><h1>Late</h1><html>");
        heading3.setHorizontalAlignment(SwingConstants.CENTER);
        heading3.setForeground(LoggerTableFormat.TableColors.MISSED_HEADING);
    }

    public void updateTable(String[][] endsToday, String[][] endsLater, String[][] late){
        this.removeAll();

        // add the html to wrap the title/info strings in the table
        for (int i = 0; i < endsToday.length; i++) {
            endsToday[i][0] = "<HTML><b><h3>" + endsToday[i][0] + "</h3></b></HTML>";
            endsToday[i][1] = "<HTML><b><h3>" + endsToday[i][1] + "</h3></b></HTML>";
        }
        table = new JTable();
        table.setBackground(LoggerTableFormat.TableColors.TODAY);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(headers);
        table.setModel(model);
        table.setRowHeight(80);

        int totalHeight = 0;
        int heightAdded = 0;
       
        for (int i = 0; i < endsToday.length; i++) {
            model.insertRow(i , endsToday[i]);
            heightAdded = 80;
            if (endsToday[i][0].length() > 130) {
                table.setRowHeight( 1, 130 + (endsToday[i][0].length()) / 3);
                heightAdded = 130 + (endsToday[i][0].length()) / 3;
            }
            if (endsToday[i][1].length() > 100) {
                table.setRowHeight(i, 80 + (endsToday[i][1].length()) / 3);
                heightAdded = 80 + (endsToday[i][1].length()) / 3;
            }
            totalHeight += heightAdded;
        }
        for (int i = 0; i < 2; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(LoggerTableFormat.todaysActivitiesWidths[i]);
        }

        scrollPane = new JScrollPane(table);

        if (totalHeight < 200){
            heightAdded = totalHeight;
        } else {
            heightAdded = 200;
        }

        table.setRowSelectionAllowed(false);

        table.setPreferredScrollableViewportSize(new Dimension(500,heightAdded));

        scrollPane.setPreferredSize(new Dimension(510,200));


        for (int i = 0; i < endsLater.length; i++) {
            endsLater[i][0] = "<HTML><b><h3>" + endsLater[i][0] + "</h3></b></HTML>";
            endsLater[i][1] = "<HTML><b><h3>" + endsLater[i][1] + "</h3></b></HTML>";
        }

        table2 = new JTable();
        table2.setBackground(LoggerTableFormat.TableColors.IN_PROGRESS);
        table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        DefaultTableModel model2 = new DefaultTableModel();
        model2.setColumnIdentifiers(headers);
        table2.setModel(model2);
        table2.setRowHeight(80);

        int totalHeight2 = 0;
        int heightAdded2 = 0;
        for (int i = 0; i < endsLater.length; i++) {
            model2.insertRow(i, endsLater[i]);
            heightAdded2 = 80;
            if (endsLater[i][0].length() > 130) {
                table2.setRowHeight(i, 130 + (endsLater[i][0].length()) / 3);
                heightAdded2 = 130 + (endsLater[i][0].length()) / 3;
            }
            if (endsLater[i][1].length() > 100) {
                table2.setRowHeight(i, 80 + (endsLater[i][1].length()) / 3);
                heightAdded2 = 80 + (endsLater[i][1].length()) / 3;
            }
            totalHeight2 += heightAdded2;
        }
        for (int i = 0; i < 2; i++) {
            TableColumn column = table2.getColumnModel().getColumn(i);
            column.setPreferredWidth(LoggerTableFormat.todaysActivitiesWidths[i]);
        }

        scrollPane2 = new JScrollPane(table2);

        if (totalHeight2 < 200){
            heightAdded2 = totalHeight2;
        } else {
            heightAdded2 = 200;
        }

        table2.setRowSelectionAllowed(false);

        table2.setPreferredScrollableViewportSize(new Dimension(500,heightAdded2));

        scrollPane2.setPreferredSize(new Dimension(510,heightAdded2));

        // add the html to wrap the title/info strings in the table
        for (int i = 0; i < late.length; i++) {
            late[i][0] = "<HTML><b><h3>" + late[i][0] + "</h3></b></HTML>";
            late[i][1] = "<HTML><b><h3>" + late[i][1] + "</h3></b></HTML>";
        }
        table3 = new JTable();
        table3.setBackground(LoggerTableFormat.TableColors.MISSED);
        table3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        DefaultTableModel model3 = new DefaultTableModel();
        model3.setColumnIdentifiers(headers);
        table3.setModel(model3);
        table3.setRowHeight(80);

        int totalHeight3 = 0;
        int heightAdded3 = 0;
        for (int i = 0; i < late.length; i++) {
            model3.insertRow(i, late[i]);
            heightAdded3 = 80;
            if (late[i][0].length() > 130) {
                table3.setRowHeight(i, 130 + (late[i][0].length()) / 3);
                heightAdded3 = 130 + (late[i][0].length()) / 3;
            }
            if (late[i][1].length() > 100) {
                table3.setRowHeight(i, 80 + (late[i][1].length()) / 3);
                heightAdded3 = 80 + (late[i][1].length()) / 3;
            }
            totalHeight3 += heightAdded3;
        }

        for (int i = 0; i < 2; i++) {
            TableColumn column = table3.getColumnModel().getColumn(i);
            column.setPreferredWidth(LoggerTableFormat.todaysActivitiesWidths[i]);
        }

        scrollPane3 = new JScrollPane(table3);

        if (totalHeight3 < 200){
            heightAdded3 = totalHeight2;
        } else {
            heightAdded3 = 200;
        }

        table3.setRowSelectionAllowed(false);

        table3.setPreferredScrollableViewportSize(new Dimension(500,heightAdded3));

        scrollPane3.setPreferredSize(new Dimension(510,heightAdded3));
        
        
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.fill = (GridBagConstraints.HORIZONTAL);
        g.anchor = GridBagConstraints.FIRST_LINE_START;

        g.gridx = 0;
        g.gridy = 0;
        g.ipady = 0;
        g.weightx = 0.5;
        g.weighty = 0.3;
        tablePanel.add(heading3, g);
        g.gridx = 0;
        g.gridy = 1;
        g.ipady = 400;
        g.weightx = 0;
        g.weighty = 0.8;
        tablePanel.add(scrollPane3,g);
        g.gridx = 1;
        g.gridy = 0;
        g.ipady = 0;
        g.weightx = 0.5;
        g.weighty = 0.3;
        tablePanel.add(heading1,g);
        g.gridx = 1;
        g.gridy = 1;
        g.ipady = 400;
        g.weightx = 0;
        g.weighty = 0.8;
        tablePanel.add(scrollPane,g);
        g.gridx = 2;
        g.gridy = 0;
        g.ipady = 0;
        g.weightx = 0.5;
        g.weighty = 0.3;
        tablePanel.add(heading2,g);
        g.gridx = 2;
        g.gridy = 1;
        g.ipady = 400;
        g.weightx = 0;
        g.weighty = 0.8;
        tablePanel.add(scrollPane2,g);

        this.add(tablePanel, BorderLayout.CENTER);
        
        
        this.add(Box.createRigidArea(new Dimension(10,800)), BorderLayout.WEST);
        this.add(Box.createRigidArea(new Dimension(10,800)), BorderLayout.EAST);
        this.revalidate();
    }

}
