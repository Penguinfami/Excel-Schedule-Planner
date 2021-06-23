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
                System.out.println("CREATE");
                parentFrame.createNewPlan();
                break;
            case "VIEW SCHEDULE":
                System.out.println("VIEW SCHEDULE");
                parentFrame.viewPlan();
                break;
            case ("UPDATE ACTIVITY"):
                System.out.println("UPDATE ACTIVITY");
                parentFrame.viewUpdateActivity();
                break;
            case ("SET AS COMPLETE"):
                System.out.println("SET AS COMPLETE");
                parentFrame.setCompleteActivity();
                break;
            case ("DELETE ACTIVITY"):
                System.out.println("DELETE ACTIVITY");
                parentFrame.deleteActivity();
                break;
            case ("CREATE SCHEDULE"):
                System.out.println("CREATE NEW SCHEDULE");
                parentFrame.createAgenda();
                break;
            case ("OPEN SCHEDULE"):
                System.out.println("OPEN SCHEDULE");
                parentFrame.openSpecificAgenda();
                break;
            case ("DELETE SCHEDULE"):
                System.out.println("OPEN SCHEDULE");
                parentFrame.deleteSpecificAgenda();
                break;
            case "CREATE NEW SCHEDULE":
                System.out.println("CREATE NEW SCHEDULE");
                parentFrame.viewCreateAgenda();
                break;
            case "MANAGE EXISTING SCHEDULE":
                System.out.println("OPEN EXISTING SCHEDULE");
                parentFrame.viewManageAgenda();
                break;
            case "UPDATE":
                System.out.println("UPDATE");
                parentFrame.setUpdatedActivity();
                break;
            case "EDIT ACTIVITY":
                System.out.println("EDIT ACTIVITY");
                parentFrame.viewEditActivity();
                break;
            case "EXIT":
                System.out.println("CLOSE");
                parentFrame.closeApplication();
                break;
        }


    } // end actionPerformed
} // end StartButtonListener
