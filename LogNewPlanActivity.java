import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.util.Calendar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JOptionPane;

public class LogNewPlanActivity extends JPanel implements ActionListener {

    private JLabel successLabel = new JLabel("");

    private String[] months = {"-", "January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};

    private Calendar calendar = Calendar.getInstance();
    private String selectedMonth = months[calendar.get(Calendar.MONTH) + 1];
    private JComboBox monthList;

    private String selectedMonth2 = months[0];
    private JComboBox monthList2;

    private Integer selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
    private JComboBox dayList;
    private Integer selectedDay2 = null;
    private JComboBox dayList2;

    private Integer selectedYear = calendar.get(Calendar.YEAR);
    private JComboBox yearList;
    private Integer selectedYear2 = null;
    private JComboBox yearList2;

    private JTextField activityTitle;

    private JTextField activityInfo;


    private JButton createButton;

    public LogNewPlanActivity(LoggerButtonListener listener) {
        this.setLayout(new GridLayout(5, 1));

        JPanel datePanel = new JPanel();

        datePanel.setLayout(new GridLayout(2, 2));

        // set up months list
        monthList = new JComboBox(months);
        monthList.setSelectedIndex(calendar.get(Calendar.MONTH) + 1);
        monthList.addActionListener(this);

        monthList2 = new JComboBox(months);
        monthList2.setSelectedIndex(0);
        monthList2.addActionListener(this);

        //set up days list
        Integer[] days = new Integer[32];
        days[0] = null;
        for (int i = 1; i < 32; i++) {
            days[i] = i;
        }
        dayList = new JComboBox(days);
        dayList.setSelectedIndex(selectedDay);
        dayList.addActionListener(this);

        dayList2 = new JComboBox(days);
        dayList2.setSelectedIndex(0);
        dayList2.addActionListener(this);

        // set up years list

        Integer[] years = new Integer[11];
        years[0] = null;
        int todaysYear = calendar.get(Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years[i + 1] = todaysYear + i;
        }
        yearList = new JComboBox(years);
        yearList.setSelectedIndex(1);
        yearList.addActionListener(this);

        yearList2 = new JComboBox(years);
        yearList2.setSelectedIndex(0);
        yearList2.addActionListener(this);

        activityTitle = new JTextField(20);
        activityTitle.addActionListener(this);

        activityInfo = new JTextField(50);
        activityInfo.addActionListener(this);

        createButton = new JButton("CREATE");
        createButton.setFont(new Font("Verdana", Font.BOLD, 40));
        createButton.addActionListener(listener);


        datePanel.add(new JLabel("<html>Start Date</html>"));
        datePanel.add(monthList);
        datePanel.add(dayList);
        datePanel.add(yearList);

        datePanel.add(new JLabel("<html>End Date</html>"));
        datePanel.add(monthList2);
        datePanel.add(dayList2);
        datePanel.add(yearList2);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(2, 1));
        JLabel label = new JLabel("<html>Title of Activity</html>");
        label.setFont(new Font("Verdana", Font.BOLD, 14));
        label.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(label);
        titlePanel.add(activityTitle);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 1));
        label = new JLabel("<html>Activity Info</html>");
        label.setFont(new Font("Verdana", Font.BOLD, 14));
        label.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(label);
        infoPanel.add(activityInfo);

        JPanel successInfoPanel = new JPanel();
        successInfoPanel.setLayout(new FlowLayout());
        successInfoPanel.setLayout(new GridLayout(1, 1));
        successLabel.setHorizontalAlignment(JLabel.CENTER);
        successLabel.setVerticalAlignment(JLabel.TOP);
        successInfoPanel.add(successLabel);

        this.add(datePanel);
        this.add(titlePanel);
        this.add(infoPanel);
        this.add(createButton);
        this.add(successInfoPanel);

    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JComboBox) {
            JComboBox cb = (JComboBox) event.getSource();
            if (cb == monthList) {
                selectedMonth = (String) cb.getSelectedItem();
            } else if (cb == dayList) {
                selectedDay = (Integer) cb.getSelectedItem();
            } else if (cb == yearList) {
                selectedYear = (Integer) cb.getSelectedItem();
            } else if (cb == monthList2) {
                selectedMonth2 = (String) cb.getSelectedItem();
            } else if (cb == dayList2) {
                selectedDay2 = (Integer) cb.getSelectedItem();
            } else if (cb == yearList2) {
                selectedYear2 = (Integer) cb.getSelectedItem();
            }
        }
    }

    public void displayErrorMessage(String missingComponent) {
        JOptionPane.showMessageDialog(this,"<html><h3>Reason:<em>" + missingComponent + "</em></h3></html>", "Activity Not Created", JOptionPane.PLAIN_MESSAGE);
        this.revalidate();
    }

    public void displaySuccessMessage() {
        successLabel.setText("<html><h1>Successfully Created Activity <em>" + activityTitle.getText() + "</em></h1></html>");
        this.revalidate();
    }

    public void removeSuccessMessage() {
        successLabel.setText("");
        this.revalidate();
    }

    public void clearInput() {
        calendar = Calendar.getInstance();

        //months
        monthList.setSelectedIndex(calendar.get(Calendar.MONTH) + 1);
        selectedMonth = months[calendar.get(Calendar.MONTH) + 1];
        monthList2.setSelectedIndex(0);
        selectedMonth2 = "-";

        // days
        dayList.setSelectedIndex(calendar.get(Calendar.DAY_OF_MONTH));
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        dayList2.setSelectedIndex(0);
        selectedDay2 = null;

        // years
        yearList.setSelectedIndex(1);
        selectedYear = calendar.get(Calendar.YEAR);
        yearList2.setSelectedIndex(0);
        selectedYear2 = null;


    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public Integer getSelectedDay() {
        return selectedDay;
    }

    public Integer getSelectedYear() {
        return selectedYear;
    }

    public String getSelectedMonth2() {
        return selectedMonth2;
    }

    public Integer getSelectedDay2() {
        return selectedDay2;
    }

    public Integer getSelectedYear2() {
        return selectedYear2;
    }

    public String getTitle() {
        return activityTitle.getText();
    }

    public String getActivityInfo() {
        return activityInfo.getText();
    }
}
