
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;

public class ProgressLoggerOptions extends JPanel {
    ProgressLoggerOptions(LoggerButtonListener listener) {
        JButton openAgendaButton = new JButton("MANAGE EXISTING SCHEDULE");
        JButton createAgendaButton = new JButton("CREATE NEW SCHEDULE");

        JButton viewPlanButton = new JButton("VIEW SCHEDULE");
        JButton logNewActivity = new JButton("LOG NEW ACTIVITY");
        JButton completePlannedActivity = new JButton("UPDATE ACTIVITY");
        openAgendaButton.addActionListener(listener);
        createAgendaButton.addActionListener(listener);
        viewPlanButton.addActionListener(listener);
        logNewActivity.addActionListener(listener);
        completePlannedActivity.addActionListener(listener);

        this.add(openAgendaButton);

        this.add(viewPlanButton);

        this.add(logNewActivity);
        this.add(completePlannedActivity);

        this.add(createAgendaButton);
    }

}
