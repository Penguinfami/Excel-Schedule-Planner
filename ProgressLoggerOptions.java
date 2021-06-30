
import javax.swing.JPanel;
import javax.swing.JButton;


public class ProgressLoggerOptions extends JPanel {

    public ProgressLoggerOptions(LoggerButtonListener listener) {
        JButton todaysActivitiesButton = new JButton("TODAY'S ACTIVITIES");

        JButton openAgendaButton = new JButton("MANAGE SCHEDULES");
        JButton createAgendaButton = new JButton("NEW SCHEDULE");

        JButton viewPlanButton = new JButton("VIEW SCHEDULE");
        JButton logNewActivity = new JButton("LOG NEW ACTIVITY");
        JButton completePlannedActivity = new JButton("UPDATE ACTIVITY");
        todaysActivitiesButton.addActionListener(listener);
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

        this.add(todaysActivitiesButton);

    }

}
