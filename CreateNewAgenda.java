import javax.swing.*;
import java.awt.*;

public class CreateNewAgenda extends JPanel {


    private JTextField inputName;
    private JButton createButton;
    private String errors;
    public CreateNewAgenda(LoggerButtonListener listener, String[] errorPossibilities) {

        errors = "<html><h1><b>The schedule name <em>cannot:</em></b></h1>";
        for (String possibleErrors : errorPossibilities){
            errors+= "<br>" + possibleErrors;
        }
        errors += "</html>";

        createButton = new JButton("CREATE SCHEDULE");
        createButton.addActionListener(listener);
        createButton.setFont(new Font("Verdana", Font.BOLD, 30));

        this.setLayout(new GridLayout(5, 1));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,1));
        JLabel label = new JLabel("<html><h1><b>The schedule name <em>cannot:</em></b></h1></html");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(label);
        
        reAddComponents();

    }

    private void reAddComponents() {
        this.removeAll();
        this.add(new JLabel("<html><h1>Create a new schedule</h1><b><h4>Name the new schedule (Max 30 characters):</h4><br></html>"));
        inputName = new JTextField(20);
        this.add(inputName);
        this.add(createButton);
    }

    public String getTextField() {
        return inputName.getText();
    }

    public void displayErrorMsg() {
        reAddComponents();
        JOptionPane.showMessageDialog(this, errors, "Invalid name.", JOptionPane.PLAIN_MESSAGE);
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
