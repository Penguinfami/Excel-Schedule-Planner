import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import javax.swing.JComboBox;

public class ManageAgenda extends JPanel {

    private JComboBox<String> listNameComboBox;

    private JPanel buttonPanel = new JPanel();
    private JButton openButton = new JButton("OPEN SCHEDULE");
    private JButton deleteButton = new JButton("DELETE SCHEDULE");

    private JLabel label = new JLabel("<html><h1>Select an existing scheduler</h1></hmtl>");

    private JLabel successLabel = new JLabel("");

    public ManageAgenda(LoggerButtonListener listener) {
        this.setLayout(new GridLayout(4, 1));
        this.add(label);
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(openButton);
        buttonPanel.add(deleteButton);
        openButton.addActionListener(listener);
        openButton.setFont(new Font("Verdana", Font.BOLD, 30));
        deleteButton.addActionListener(listener);
        deleteButton.setFont(new Font("Verdana", Font.BOLD, 30));

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
