
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.Font;

public class UpdatePlannedActivity extends JPanel implements ActionListener {


    private JLabel successLabel = new JLabel("");

    private int index = 0;

    private JComboBox activityListComboBox;

    private Font buttonFont = new Font("Verdana", Font.BOLD, 25);

    private JButton setCompleteButton;
    private JButton deleteActivityButton;
    private JButton editActivityButton;


    private JPanel optionButtons = new JPanel();

    UpdatePlannedActivity(LoggerButtonListener listener) {
        this.setLayout(new BorderLayout());


        setCompleteButton = new JButton("SET AS COMPLETE");
        setCompleteButton.addActionListener(listener);
        setCompleteButton.setFont(buttonFont);
        editActivityButton = new JButton("EDIT ACTIVITY");
        editActivityButton.addActionListener(listener);
        editActivityButton.setFont(buttonFont);
        deleteActivityButton = new JButton("DELETE ACTIVITY");
        deleteActivityButton.addActionListener(listener);
        deleteActivityButton.setFont(buttonFont);

        this.add(new JLabel("<html><h1>MANAGE SCHEDULED ACTIVITIES</h1><html>"), BorderLayout.NORTH);

        optionButtons.setLayout(new GridLayout(1, 3));
        optionButtons.add(setCompleteButton);
        optionButtons.add(editActivityButton);
        optionButtons.add(deleteActivityButton);


        this.add(optionButtons, BorderLayout.SOUTH);

        this.add(successLabel, BorderLayout.SOUTH);

    }


    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == activityListComboBox) {
            index = ((JComboBox) event.getSource()).getSelectedIndex();
        }
    }

    public void updatePanel(String[] names) {

        this.removeAll();
        this.setLayout(new GridLayout(4, 1));

        activityListComboBox = new JComboBox(names);
        activityListComboBox.addActionListener(this);
        activityListComboBox.setFont(new Font("Verdana", Font.BOLD, 13));
        activityListComboBox.setSelectedIndex(0);


        this.add(new JLabel("<html><h1>MANAGE SCHEDULED ACTIVITIES</h1><html>"), BorderLayout.NORTH);
        this.add(activityListComboBox, BorderLayout.CENTER);
        this.add(optionButtons, BorderLayout.SOUTH);

        this.add(successLabel, BorderLayout.SOUTH);

    }

    public void displaySuccessLabel(String str) {
        successLabel.setText(str);
    }

    public void removeSuccessLabel() {
        successLabel.setText("");
    }

    public int getSelectedIndex() {
        return index;
    }
}
