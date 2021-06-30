import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
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
import javax.swing.JOptionPane;
public class RenameExistingAgenda extends JPanel {

    private JLabel titleLabel;
    private JLabel successLabel;
    private JTextField inputName;
    private JButton renameButton;
    private String previousName;
    private String errors;
    public RenameExistingAgenda(LoggerButtonListener listener, String[] errorPossibilities) {

        errors = "<html><h1><b>The schedule name <em>cannot:</em></b></h1>";
        for (String possibleErrors : errorPossibilities){
            errors+= "<br>" + possibleErrors;
        }
        errors += "</html>";

        titleLabel = new JLabel("");
        successLabel = new JLabel("");

        inputName = new JTextField();

        renameButton = new JButton("RENAME");
        renameButton.addActionListener(listener);
        renameButton.setFont(new Font("Verdana", Font.BOLD, 30));

        this.setLayout(new GridLayout(5, 1));

        this.add(titleLabel);
        this.add(inputName);
        this.add(renameButton);

        refreshComponents();

    }

    public void updateScheduleCurrentName(String name){
        titleLabel.setText("<html><h1>Current schedule name: " + name + "</h1><b><h2>Enter new name (Max 30 characters):</h2><br></html>");
        previousName = name;
    }

    public String getPreviousName(){
        return previousName;
    }

    public void refreshComponents() {
        inputName.setText("");
        this.remove(successLabel);
    }

    public String getNewName() {
        return inputName.getText();
    }

    public void displayErrorMsg() {
        JOptionPane.showMessageDialog(this,errors, "Invalid name", JOptionPane.PLAIN_MESSAGE);
        this.revalidate();
        this.repaint();
    }

    public void successfulRenameMsg(String oldName, String newName) {
        refreshComponents();
        successLabel.setText("<html><h1>The schedule <em>" + oldName + "</em> has been renamed to <em>" + newName + "</em></h1></html>");
        this.add(successLabel);
        this.revalidate();
        this.repaint();
    }


}
