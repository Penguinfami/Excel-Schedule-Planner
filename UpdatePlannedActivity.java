
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Dimension;

public class UpdatePlannedActivity extends JPanel implements ActionListener {


    private JLabel successLabel = new JLabel("");

    private int index;

    private JComboBox activityListComboBox;

    private Font buttonFont = new Font("Verdana", Font.BOLD, 25);

    private JButton setCompleteButton;
    private JButton deleteActivityButton;
    private JButton editActivityButton;

    private JButton incompleteButton = new JButton ("Incomplete Only");
    private JButton completeButton = new JButton ("Complete Only");
    private JButton allButton = new JButton ("All");

    private JPanel optionButtons = new JPanel();

    private JPanel titleSortPanel = new JPanel();

    public UpdatePlannedActivity(LoggerButtonListener listener) {

        this.setLayout(new BorderLayout());

        this.index = 0;

        setCompleteButton = new JButton("SET AS COMPLETE");
        setCompleteButton.addActionListener(listener);
        setCompleteButton.setFont(buttonFont);
        editActivityButton = new JButton("EDIT ACTIVITY");
        editActivityButton.addActionListener(listener);
        editActivityButton.setFont(buttonFont);
        deleteActivityButton = new JButton("DELETE ACTIVITY");
        deleteActivityButton.addActionListener(listener);
        deleteActivityButton.setFont(buttonFont);

        optionButtons.setLayout(new GridLayout(1, 3));
        optionButtons.add(setCompleteButton);
        optionButtons.add(editActivityButton);
        optionButtons.add(deleteActivityButton);

        titleSortPanel.setLayout(new BoxLayout(titleSortPanel, BoxLayout.X_AXIS));
        titleSortPanel.add(new JLabel("<html><h1>MANAGE SCHEDULED ACTIVITIES</h1><html>"));
        titleSortPanel.add(Box.createRigidArea(new Dimension(100,10)));
        incompleteButton.addActionListener(listener);
        completeButton.addActionListener(listener);
        allButton.addActionListener(listener);
        titleSortPanel.add(incompleteButton);
        titleSortPanel.add(completeButton);
        titleSortPanel.add(allButton);

        activityListComboBox = new JComboBox<String>();

        this.add(titleSortPanel);

        this.add(activityListComboBox);

        this.add(optionButtons);

        this.add(successLabel);

    }


    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == activityListComboBox) {
            index = ((JComboBox) event.getSource()).getSelectedIndex();
        }
    }

    public void updatePanel(String[] names) {

        this.removeAll();
        this.setLayout(new GridLayout(4, 1));

        activityListComboBox = new JComboBox<String>(names);
        activityListComboBox.addActionListener(this);
        activityListComboBox.setFont(new Font("Verdana", Font.BOLD, 13));
        activityListComboBox.setSelectedIndex(0);

        this.add(titleSortPanel);

        this.add(activityListComboBox);

        this.add(optionButtons);

        this.add(successLabel);

        this.revalidate();
    }

    public void displaySuccessLabel(String str) {
        successLabel.setText(str);
    }

    public void displayErrorMsg(String str) {
        JOptionPane.showMessageDialog(this, "<html><h2>Activity Not Updated:</h2>" + str + "</html>", "Error", JOptionPane.PLAIN_MESSAGE);
    }

    public void removeSuccessLabel() {
        successLabel.setText("");
    }

    public int getSelectedIndex() {
        return index;
    }

    public String getSelectedString(){
        return (String)activityListComboBox.getSelectedItem();
    }
}
