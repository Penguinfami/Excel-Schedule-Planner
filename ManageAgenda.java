
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.Color;

public class ManageAgenda extends JPanel {

    private JComboBox<String> listNameComboBox;

    private JPanel buttonPanel = new JPanel();
    private JButton openButton = new JButton("OPEN SCHEDULE");
    private JButton renameButton = new JButton("RENAME SCHEDULE");
    private JButton deleteButton = new JButton("DELETE SCHEDULE");

    private JLabel label = new JLabel("<html><h1>Select an existing scheduler</h1></hmtl>");

    private JLabel successLabel = new JLabel("");

    public ManageAgenda(LoggerButtonListener listener) {
        this.setLayout(new GridLayout(4, 1));
        this.add(label);
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(openButton);
        buttonPanel.add(renameButton);
        buttonPanel.add(deleteButton);
        openButton.setBackground(Color.decode("#F2F2F2")); // light gray
        renameButton.setBackground(Color.decode("#F2F2F2")); // light gray
        deleteButton.setBackground(Color.decode("#B0B0B0")); // even darker gray
        openButton.addActionListener(listener);
        openButton.setFont(new Font("Verdana", Font.BOLD, 27));
        renameButton.addActionListener(listener);
        renameButton.setFont(new Font("Verdana", Font.BOLD, 23));
        deleteButton.addActionListener(listener);
        deleteButton.setFont(new Font("Verdana", Font.BOLD, 20));

    }

    public String getSelectedAgenda() {
        return (String) listNameComboBox.getSelectedItem();
    }

    public void setSuccessLabel(String str) {
        successLabel.setText("<html><h1>" + str + "</h1></hmtl>");
    }

    public void updateComboBox(String[] listNames) {

        String[] listNamesWithLeadingSpace = new String[listNames.length + 1];
        listNamesWithLeadingSpace[0] = "";

        for (int i = 0; i < listNames.length; i++) {
            listNamesWithLeadingSpace[i + 1] = listNames[i];
        }
        listNameComboBox = new JComboBox<String>(listNamesWithLeadingSpace);
        listNameComboBox.setFont(new Font("Verdana", Font.BOLD, 16));
        listNameComboBox.setAlignmentX(JComboBox.CENTER_ALIGNMENT);
        listNameComboBox.setSelectedIndex(0);
        this.removeAll();
        this.add(label);
        this.add(listNameComboBox);
        this.add(buttonPanel);
        this.add(successLabel);

    }
}
