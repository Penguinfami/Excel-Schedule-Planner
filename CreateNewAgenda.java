import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class CreateNewAgenda extends JPanel {

    private JLabel errorMsg = new JLabel("<html><h1>Invalid name. A new Schedule was not created</h1></html>");
    private JComboBox<String> listAgendas;
    private JTextField inputName;
    private JButton createButton;

    CreateNewAgenda(LoggerButtonListener listener) {
        createButton = new JButton("CREATE SCHEDULE");
        createButton.addActionListener(listener);
        createButton.setFont(new Font("Verdana", Font.BOLD, 30));

        this.setLayout(new GridLayout(5, 1));


        reAddComponents();

    }

    private void reAddComponents() {
        this.removeAll();
        this.add(new JLabel("<html><h1>Create a new schedule</h1><b><h4>Name the new schedule (Max 20 characters):</h4><br></html>"));
        inputName = new JTextField(20);
        this.add(inputName);
        this.add(createButton);
    }

    public String getTextField() {
        return inputName.getText();
    }

    public void displayErrorMsg() {
        reAddComponents();
        this.add(errorMsg);
        this.revalidate();
        this.repaint();
    }

    public void removeErrorMsg() {
        reAddComponents();
        this.remove(errorMsg);
        this.revalidate();
        this.repaint();
    }

    public void newScheduleMsg() {
        reAddComponents();
        this.add(new JLabel("<html><h1>A new schedule was created</h1></html>"));
        this.revalidate();
        this.repaint();
    }


}