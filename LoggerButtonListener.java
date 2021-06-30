import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class LoggerButtonListener implements ActionListener {

    private ProgressLoggerFrame parentFrame;

    /**
     * StartButtonListener
     * Construct a start button listener
     *
     * @param parent The parent starting frame
     */
    public LoggerButtonListener(ProgressLoggerFrame parent) {
        this.parentFrame = parent;
    }

    /**
     * actionPerformed
     * The start button pressed and start the game
     *
     * @param event The action event
     */
    public void actionPerformed(ActionEvent event) {
        String button = event.getActionCommand();

        switch (button) {

            case "LOG NEW ACTIVITY":
                parentFrame.logNewActivity();
                break;
            case "CREATE":
                parentFrame.createNewPlan();
                break;
            case "VIEW SCHEDULE":
                parentFrame.viewPlan();
                break;
            case ("UPDATE ACTIVITY"):
                parentFrame.viewUpdateActivity();
                break;
            case ("SET AS COMPLETE"):
                parentFrame.setCompleteActivity();
                break;
            case ("DELETE ACTIVITY"):
                parentFrame.deleteActivity();
                break;
            case ("CREATE SCHEDULE"):
                parentFrame.createAgenda();
                break;
            case ("OPEN SCHEDULE"):
                parentFrame.openSpecificAgenda();
                break;
            case ("RENAME SCHEDULE"):
                parentFrame.viewRenameAgenda();
                break;
            case ("RENAME"):
                parentFrame.renameAgenda();
                break;
            case ("DELETE SCHEDULE"):
                parentFrame.deleteSpecificAgenda();
                break;
            case "NEW SCHEDULE":
                parentFrame.viewCreateAgenda();
                break;
            case "MANAGE SCHEDULES":
                parentFrame.viewManageAgenda();
                break;
            case "UPDATE":
                parentFrame.setUpdatedActivity();
                break;
            case "EDIT ACTIVITY":
                parentFrame.viewEditActivity();
                break;
            case "EXIT":
                parentFrame.closeApplication();
                break;
            case "ALL":
                parentFrame.viewPlan();
                break;
            case "COMPLETE ONLY":
                parentFrame.viewCompleteOnly();
                break;
            case "INCOMPLETE ONLY":
                parentFrame.viewIncompleteOnly();
                break;
            case "All":
                parentFrame.updateActivitySort(0);
                break;
            case "Complete Only":
                parentFrame.updateActivitySort(2);
                break;
            case "Incomplete Only":
                parentFrame.updateActivitySort(1);
                break;
            case "TODAY'S ACTIVITIES":
                parentFrame.viewTodaysActivities();
                break;
        }


    } // end actionPerformed
} // end StartButtonListener
