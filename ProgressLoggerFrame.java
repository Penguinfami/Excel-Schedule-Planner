
import javax.swing.JFrame;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.JOptionPane;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.ResolverStyle;
import java.time.format.DateTimeParseException;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

public class ProgressLoggerFrame extends JFrame {

    private final String[] columnNames = LoggerTableFormat.headerTitles;

    private JPanel currentPanel;

    private LoggerButtonListener buttonListener = new LoggerButtonListener(this);
    private AllTodayActivities todaysActivities = new AllTodayActivities();
    private TitlePanel titlePanel = new TitlePanel();
    private ProgressLoggerOptions options = new ProgressLoggerOptions(buttonListener);
    private LogNewPlanActivity logNewPlan = new LogNewPlanActivity(buttonListener);
    private ProgressPlanTable planTable = new ProgressPlanTable(buttonListener);

    private UpdatePlannedActivity updateActivity = new UpdatePlannedActivity(buttonListener);

    private EditPlannedActivity editActivity = new EditPlannedActivity(buttonListener);

    private ManageAgenda manageAgenda = new ManageAgenda(buttonListener);

    private CreateNewAgenda createNewAgenda;

    private RenameExistingAgenda renameExistingAgenda;

    private PriorityLinkedList<Activity> listOfActivities = new PriorityLinkedList<Activity>();
    private SimpleLinkedList<String> listOfAgendas = new SimpleLinkedList<String>();

    private String nameCurrentAgenda;

    private XSSFWorkbook workbook;
    private XSSFSheet currentSheet;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private DataFormatter dataFormatter = new DataFormatter();
    private int numColumns = 6; // number of columns in the currentSheet table

    private CellStyle[] styleOrder;

    private LoggerCellStyles cellStyles;

    public ProgressLoggerFrame() {
        super("Progress Calendar");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 720);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        loadWorkbook();
        String[] errorPossibilities = {"be already taken by an existing schedule","be blank", "be named \"" + workbook.getSheetAt(0).getSheetName() + "\"", "be named \"history\"",
                "start or end with an apostrophe (')", "contain any of the following characters <em> \\ / ? * [ ] : </em>"};
        createNewAgenda = new CreateNewAgenda(buttonListener, errorPossibilities);
        renameExistingAgenda = new RenameExistingAgenda(buttonListener, errorPossibilities);
        cellStyles = new LoggerCellStyles(workbook);
        styleOrder = cellStyles.getStyleOrder();
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(options, BorderLayout.SOUTH);
        this.setVisible(true);
        this.requestFocusInWindow();

        currentPanel = manageAgenda;
        viewManageAgenda();


    }


    public void closeApplication() {
        try {
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void loadWorkbook() {
        try {
            FileInputStream input = new FileInputStream(new File("MyWorkbook.xlsx"));
            workbook = new XSSFWorkbook(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet listNamesSheet = workbook.getSheetAt(0);
        int index = 1;
        XSSFRow row = listNamesSheet.getRow(index);
        while (row != null) {
            XSSFCell cell = row.getCell(0);
            if (cell == null) {
                break;
            } else if (cell.getStringCellValue().trim().equals("")) {
                break;
            }
            index++;
            listOfAgendas.add(cell.getStringCellValue());
            row = listNamesSheet.getRow(index);
        }

        nameCurrentAgenda = "";
        titlePanel.updateAgendaName(nameCurrentAgenda);

    }

    public void createListOfActivities(String nameOfAgenda) {

        try {
            currentSheet = workbook.getSheet(nameOfAgenda);

            listOfActivities.clear();

            if (currentSheet == null) return;

            boolean isFinished = false;

            XSSFRow row;
            XSSFCell cell;

            for (int i = 1; !isFinished; i++) {
                row = currentSheet.getRow(i);
                if (row == null) {
                    isFinished = true;
                } else if (row.getCell(0) == null) {
                    isFinished = true;
                } else if (row.getCell(0).getCellType() == CellType.BLANK) {
                    isFinished = true;
                } else { // if the row is indeed an activity row

                    String[] args = new String[numColumns];
                    for (int j = 0; j < numColumns; j++) {
                        cell = row.getCell(j);
                        if (cell == null) {
                            cell = row.createCell(j);
                        }

                        if (LoggerTableFormat.typeOrder[j] == LoggerTableFormat.ValueType.DATE) {
                            if (cell.getCellType() == CellType.STRING) {
                                args[j] = cell.getStringCellValue();
                            } else if (cell.getCellType() == CellType.NUMERIC) { // if the date is displayed as a number, convert it to a string
                                args[j] = dateFormatter.format(cell.getDateCellValue());
                            }
                        } else if (LoggerTableFormat.typeOrder[j] == LoggerTableFormat.ValueType.STRING) {
                            if (cell.getCellType() == CellType.STRING) {
                                args[j] = cell.getStringCellValue();
                            } else if (cell.getCellType() == CellType.NUMERIC) { // if the user set a title as a number, convert it to a string
                                args[j] = dataFormatter.formatCellValue(cell);
                            }
                        }

                    }

                    // get today's date, start date and end date as Date objects
                    Calendar today = Calendar.getInstance();
                    Date todaysDate = new Date(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
                    String[] date = args[0].split("-"); // using start date
                    Date startDate = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    date = args[1].split("-"); // using end date

                    Date endDate = new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    int priority;

                    // if the start date has already passed / is today, the priority is based on end date
                    // if the start date is still in the future, the priority is based on start date
                    if (todaysDate.compareTo(startDate) <= 0){
                        priority = 99999999 - (endDate.getYear() * 10000 + endDate.getMonth() * 100 + endDate.getDay());
                    } else {
                        priority =  -(startDate.getYear() * 10000 + startDate.getMonth() * 100 + startDate.getDay());
                    }

                    listOfActivities.add(new Activity(args[0], args[1], args[2], args[3], args[4], args[5]), priority);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void viewTodaysActivities(){
        currentPanel.setVisible(false);
        currentPanel = todaysActivities;
        this.add(todaysActivities, BorderLayout.CENTER);
        String[][][] array = getTodaysActivities();
        todaysActivities.updateTable(array[0], array[1], array[2]);
        todaysActivities.setVisible(true);
        this.revalidate();
    }

    public String[][][] getTodaysActivities(){
        PriorityLinkedList<String[]> activityList = new PriorityLinkedList<String[]>();
        PriorityLinkedList<String[]> endsLaterList = new PriorityLinkedList<String[]>();
        PriorityLinkedList<String[]> lateList = new PriorityLinkedList<String[]>();
        Activity activity;
        Date endDate;
        Date startDate;
        Calendar today = Calendar.getInstance();
        int priority;
        String agendaName;
        Date todaysDate = new Date(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
        for (int i = 0; i < listOfAgendas.size(); i++){
            createListOfActivities(listOfAgendas.get(i));
            agendaName = "<em>" + listOfAgendas.get(i) + ":</em><br>";
            for (int j = 0; j < listOfActivities.size(); j++){
                activity = listOfActivities.get(j);
                endDate = new Date(activity.getDateEnd());
                startDate = new Date(activity.getDateStart());
                priority = 99999999 - (endDate.getYear() * 10000 + endDate.getMonth() * 100 + endDate.getDay());
                if (activity.getDateCompleted() == null && endDate.compareTo(todaysDate) == 0){
                    activityList.add(new String[]{ agendaName + activity.getTitleOfActivity(), activity.getActivityInfo()}, priority);
                } else if (activity.getDateCompleted() == null && startDate.compareTo(todaysDate) >= 0 && endDate.compareTo(todaysDate) < 0){
                    endsLaterList.add(new String[]{agendaName + activity.getTitleOfActivity(), activity.getActivityInfo()}, priority);
                } else if (activity.getDateCompleted() == null && endDate.compareTo(todaysDate) > 0){
                    lateList.add(new String[]{agendaName + activity.getTitleOfActivity(), activity.getActivityInfo()}, priority);
                }
            }
        }
        String[][] activitiesArray = new String[activityList.size()][2];
        for (int i = 0; i < activityList.size(); i++){
            activitiesArray[i] = activityList.get(i);
        }

        String[][] endsLaterArray = new String[endsLaterList.size()][2];
        for (int i = 0; i < endsLaterList.size(); i++){
            endsLaterArray[i] = endsLaterList.get(i);
        }

        String[][] lateArray = new String[lateList.size()][2];
        for (int i = 0; i < lateList.size(); i++){
            lateArray[i] = lateList.get(i);
        }

        // clear the list of activities to return to what it was before
        listOfActivities.clear();
        createListOfActivities(nameCurrentAgenda);

        return new String[][][] {activitiesArray, endsLaterArray, lateArray};
    }


    public void viewPlan() {
        if (currentSheet == null) return;
        this.add(planTable, BorderLayout.CENTER);
        planTable.createTable(createActivitiesArray());
        currentPanel.setVisible(false);
        currentPanel = planTable;
        planTable.setVisible(true);
        this.revalidate();
    }

    public void logNewActivity() {
        if (currentSheet == null) return;
        logNewPlan.removeSuccessMessage();
        currentPanel.setVisible(false);
        currentPanel = logNewPlan;
        logNewPlan.setVisible(true);
        this.add(logNewPlan, BorderLayout.CENTER);
        this.revalidate();

    }

    public void createNewPlan() {

        String missingComponents = checkPlanValidity(logNewPlan);
        if (missingComponents != null) {
            logNewPlan.displayErrorMessage(missingComponents);
            return;
        }

        // if all inputs are valid

        Calendar today = Calendar.getInstance();
        String todayString = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + today.get(Calendar.DAY_OF_MONTH);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        String dateStartString = "";
        Integer selectedMonth = 0;
        Integer selectedMonth2 = 0;
        Integer selectedDay = logNewPlan.getSelectedDay();
        Integer selectedDay2 = logNewPlan.getSelectedDay2();
        Integer selectedYear = logNewPlan.getSelectedYear();
        Integer selectedYear2 = logNewPlan.getSelectedYear2();


        // Convert the Month String to the number value
        for (int i = 0; i < 12; i++) {
            if (months[i].equals(logNewPlan.getSelectedMonth())) {
                dateStartString = logNewPlan.getSelectedYear() + "-" + (i + 1) + "-" + logNewPlan.getSelectedDay().toString();
                selectedMonth = i + 1;
            }
        }

        String dateEndString = null;
        if (!logNewPlan.getSelectedMonth2().equals("-")) { // if an end date was set
            for (int i = 0; i < 12; i++) {
                if (months[i].equals(logNewPlan.getSelectedMonth2())) {
                    dateEndString = logNewPlan.getSelectedYear2() + "-" + (i + 1) + "-" + logNewPlan.getSelectedDay2().toString();
                    selectedMonth2 = i + 1;
                }
            }
        } else { // otherwise, set the end date as the same as the start date
            dateEndString = logNewPlan.getSelectedYear() + "-" + selectedMonth + "-" + logNewPlan.getSelectedDay().toString();
            selectedMonth2 = selectedMonth;
            selectedDay2 = selectedDay;
            selectedYear2 = selectedYear;
        }

        Activity newActivity = new Activity(dateStartString, dateEndString, logNewPlan.getTitle(), logNewPlan.getActivityInfo(), todayString, null);


        Date startDate = new Date(selectedYear,selectedMonth, selectedDay);
        Date todaysDate = new Date(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));

        int priority;

        // if the start date has already passed / is today, the priority is based on end date
        // if the start date is still in the future, the priority is based on start date
        if (todaysDate.compareTo(startDate) <= 0){
            priority = 99999999 - (selectedYear2 * 10000 + selectedMonth2 * 100 + selectedDay2);
        } else {
            priority =  -(selectedYear * 10000 + selectedMonth * 100 + selectedDay);
        }

        listOfActivities.add(newActivity, priority);
        System.out.print("Created new plan");
        planTable.createTable(createActivitiesArray());
        updateWorkSheet();
        logNewPlan.displaySuccessMessage();
        logNewPlan.clearInput();
    }

    public String checkPlanValidity(JPanel unCastedPanel) {
        String selectedMonth;
        String selectedMonth2;
        Integer selectedDay;
        Integer selectedDay2;
        Integer selectedYear;
        Integer selectedYear2;
        String newActivityTitle;
        if (unCastedPanel instanceof LogNewPlanActivity) {
            LogNewPlanActivity panel = (LogNewPlanActivity) unCastedPanel;
            selectedMonth = panel.getSelectedMonth();
            selectedMonth2 = panel.getSelectedMonth2();
            selectedDay = panel.getSelectedDay();
            selectedDay2 = panel.getSelectedDay2();
            selectedYear = panel.getSelectedYear();
            selectedYear2 = panel.getSelectedYear2();
            newActivityTitle = panel.getTitle().trim();
        } else if (unCastedPanel instanceof EditPlannedActivity) {
            EditPlannedActivity panel = (EditPlannedActivity) unCastedPanel;
            selectedMonth = panel.getSelectedMonth();
            selectedMonth2 = panel.getSelectedMonth2();
            selectedDay = panel.getSelectedDay();
            selectedDay2 = panel.getSelectedDay2();
            selectedYear = panel.getSelectedYear();
            selectedYear2 = panel.getSelectedYear2();
            newActivityTitle = panel.getTitle().trim();
        } else {
            return null;
        }

        StringBuilder errorComponents = new StringBuilder();


        if (newActivityTitle.trim().equals("")) {
            errorComponents.append("<br>No Title of Activity");
        }

        if (unCastedPanel instanceof LogNewPlanActivity) {
            for (int i = 0; i < listOfActivities.size(); i++) {
                if ((newActivityTitle).equalsIgnoreCase(listOfActivities.get(i).getTitleOfActivity())) {
                    errorComponents.append("<br>An Activity Of That Title Already Exists");
                }
            }
        }

        boolean startDateExist = true;

        if (selectedMonth.equals("-")) {
            errorComponents.append("<br>Starting Month Is Not Set");
            startDateExist = false;
        }
        if (selectedDay == null) {
            errorComponents.append("<br>Starting Day Of Month Is Not Set");
            startDateExist = false;
        }
        if (selectedYear == null) {
            errorComponents.append("<br>Starting Year Is Not Set");
            startDateExist = false;
        }

        if (startDateExist){
            try{// check if the date exists and is valid
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
                String[] months = {"-", "January", "February", "March", "April", "May", "June", "July", "August", "September",
                        "October", "November", "December"};
                for (int i = 0; i < months.length; i++){
                    if (months[i].equals(selectedMonth)){
                        String monthStr = ""+i + "";
                        if (i < 10){
                            monthStr = "0"+ i;
                        }
                        LocalDate.parse(selectedYear+ "-" + monthStr + "-" + selectedDay, dateTimeFormatter);
                    }
                }
            }catch(DateTimeParseException e){
                errorComponents.append("<br>" + selectedYear + "-" + selectedMonth + "-" + selectedDay + " Is Not A Real Date");
            }
        }

        if (!(selectedMonth2.equals("-") && selectedDay2 == null && selectedYear2 == null)) { // if not all end date components are left blank
            boolean isMissingComponent = false;
            if (selectedMonth2.equals("-")) {
                errorComponents.append("<br>Ending Month Is Not Set");
                isMissingComponent = true;
            }
            if (selectedDay2 == null) {
                errorComponents.append("<br>Ending Day Of Month Is Not Set");
                isMissingComponent = true;
            }
            if (selectedYear2 == null) {
                errorComponents.append("<br>Ending Year Is Not Set");
                isMissingComponent = true;
            }

            if (!isMissingComponent) {

                try{ // check if the date exists and is valid
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
                    String[] months = {"-", "January", "February", "March", "April", "May", "June", "July", "August", "September",
                            "October", "November", "December"};
                    for (int i = 0; i < months.length; i++){
                        if (months[i].equals(selectedMonth2)){
                            String monthStr = ""+i + "";
                            if (i < 10){
                                monthStr = "0"+ i;
                            }
                            LocalDate.parse(selectedYear2 + "-" + monthStr+ "-" + selectedDay2, dateTimeFormatter);
                        }
                    }
                }catch(DateTimeParseException e){
                    errorComponents.append("<br>" + selectedYear2 + "-" + selectedMonth2 + "-" + selectedDay2 + " Is Not A Real Date");
                }

                if (selectedYear > selectedYear2) {
                    errorComponents.append("<br>End Year Cannot Be Earlier Than Start Date");
                } else if (selectedYear.equals(selectedYear2)) { // if the start and end years are the same
                    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
                            "October", "November", "December"};
                    for (String month : months) {
                        if (month.equals(selectedMonth)) {
                            if (month.equals(selectedMonth2)) { // if start month and end month are the same month
                                if (selectedDay > selectedDay2) {
                                    errorComponents.append("<br>End Day Cannot Be Earlier Than Start Date");
                                }
                            }
                            break;
                        } else if (month.equals(selectedMonth2)) { // if the end month is earlier than the start month (is first in the month list)
                            errorComponents.append("<br>End Month Cannot Be Earlier Than Start Date").append(selectedMonth).append(selectedMonth2);
                        }
                    }
                }
            }
        }

        String errorComponentsString = errorComponents.toString();

        if (!errorComponentsString.equals("")) { // if there are things missing, return the string of missing things
            return errorComponentsString;
        } else {
            return null;
        }

    }

    public void viewUpdateActivity() {
        if (currentSheet == null) return;
        updateActivitySort(0);
        updateActivity.removeSuccessLabel();
        currentPanel.setVisible(false);
        currentPanel = updateActivity;
        updateActivity.setVisible(true);
        this.add(updateActivity, BorderLayout.CENTER);
        this.revalidate();

    }

    public void updateActivitySort(int sortType){ // 0 = all, 1 = incomplete, 2 = complete
        if (sortType == 0){ // display all activities
            String[] nameDateArray = new String[listOfActivities.size() + 1];
            nameDateArray[0] = " ";

            String title;

            for (int i = 1; i < nameDateArray.length; i++) {
                title = listOfActivities.get(i - 1).getTitleOfActivity();
                nameDateArray[i] = listOfActivities.get(i - 1).getDateStart() + " - " +
                        (title);
            }
            updateActivity.updatePanel(nameDateArray);
        } else if (sortType == 1){// sort by incomplete
            int index = 1;
            String[][] activityArray = getCompletedActivitiesArray(false);
            String[] nameDateArray = new String[activityArray.length + 1];
            nameDateArray[0] = " ";
            for (int i = 0; i < listOfActivities.size(); i++){
                Activity activity = listOfActivities.get(i);
                if (activity.getDateCompleted() == null){
                    nameDateArray[index] = activity.getDateStart() + " - " +
                            activity.getTitleOfActivity();
                    index++;
                }
            }
            updateActivity.updatePanel(nameDateArray);

        } else if (sortType == 2){ // sort by complete
            int index = 1;
            String[][] activityArray = getCompletedActivitiesArray(true);
            String[] nameDateArray = new String[activityArray.length + 1];
            nameDateArray[0] = " ";
            for (int i = 0; i < listOfActivities.size(); i++){
                Activity activity = listOfActivities.get(i);
                if (activity.getDateCompleted() != null){
                    nameDateArray[index] = activity.getDateStart() + " - " +
                            activity.getTitleOfActivity();
                    index++;
                }
            }
            updateActivity.updatePanel(nameDateArray);
        }
    }

    public void setCompleteActivity() {
        int index = updateActivity.getSelectedIndex();
        if (index != 0) {
            Calendar today = Calendar.getInstance();
            String name = updateActivity.getSelectedString();
            name = name.substring(name.indexOf(" - ") + 3); // remove the date and dash
            Activity selectedActivity = listOfActivities.get(0);
            for (int i = 0; i < listOfActivities.size(); i++){
                selectedActivity = listOfActivities.get(i);
                if (name.equals(selectedActivity.getTitleOfActivity())){
                    break;
                }
            }            String dateCompletedString = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + today.get(Calendar.DAY_OF_MONTH);
            selectedActivity.setDateCompleted(dateCompletedString);
            updateWorkSheet();
            updateActivity.displaySuccessLabel("<html><h1>Activity <em>" + name + "</em> Completed!</h1></html>");
        }
    }

    public void viewEditActivity() {
        if (currentSheet == null) return;
        int index = updateActivity.getSelectedIndex();
        if (index != 0) {
            String listName = updateActivity.getSelectedString();
            listName = listName.substring(listName.indexOf(" - ") + 3); // remove the date and dash
            Activity chosenActivity = listOfActivities.get(0);
            for (int i = 0; i < listOfActivities.size(); i++){
                chosenActivity = listOfActivities.get(i);
                if (listName.equals(chosenActivity.getTitleOfActivity())){
                    break;
                }
            }
            editActivity.setUpActivity(chosenActivity);
            editActivity.removeSuccessMessage();
            this.add(editActivity, BorderLayout.CENTER);
            updateActivity.removeSuccessLabel();
            currentPanel.setVisible(false);
            currentPanel = editActivity;
            editActivity.setVisible(true);
            this.revalidate();

        }
    }

    public void deleteActivity() {
        int index = updateActivity.getSelectedIndex();


        if (index != 0) {
            String name = updateActivity.getSelectedString();
            name = name.substring(name.indexOf(" - ") + 3); // remove the date and dash
            Activity chosenActivity = listOfActivities.get(0);
            for (int i = 0; i < listOfActivities.size(); i++){
                chosenActivity = listOfActivities.get(i);
                if (name.equals(chosenActivity.getTitleOfActivity())){
                    break;
                }
            }
            int confirmation = JOptionPane.showConfirmDialog(this, "<html>Are you sure you want to delete the activity \"" + chosenActivity.getTitleOfActivity() + "\"?</html>", "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                listOfActivities.delete(listOfActivities.indexOf(chosenActivity)); // delete the activity
                updateWorkSheet();
                viewUpdateActivity(); // act as a refresher for an updated combo box
                updateActivity.displaySuccessLabel("<html><h1>The Activity <em>" + name + "</em> Has Been Deleted.</h1></html>");
            }
        }

    }

    public void setUpdatedActivity() {
        String missingComponents = checkPlanValidity(editActivity);
        if (missingComponents != null) {
            editActivity.displayErrorMessage(missingComponents);
            return;
        }

        // if all inputs are valid

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        String dateStartString = "";
        Integer selectedMonth = 0;
        Integer selectedMonth2 = 0;
        Integer selectedDay = editActivity.getSelectedDay();
        Integer selectedDay2 = editActivity.getSelectedDay2();
        Integer selectedYear = editActivity.getSelectedYear();
        Integer selectedYear2 = editActivity.getSelectedYear2();

        // Convert the Month String to the number value

        for (int i = 0; i < 12; i++) {
            if (months[i].equals(editActivity.getSelectedMonth())) {
                dateStartString = editActivity.getSelectedYear() + "-" + (i + 1) + "-" + editActivity.getSelectedDay().toString();
                selectedMonth = i + 1;
            }
        }

        String dateEndString = null;
        if (!editActivity.getSelectedMonth2().equals("-")) { // if an end date was set
            for (int i = 0; i < 12; i++) {
                if (months[i].equals(editActivity.getSelectedMonth2())) {
                    dateEndString = editActivity.getSelectedYear2() + "-" + (i + 1) + "-" + editActivity.getSelectedDay2().toString();
                    selectedMonth2 = i + 1;
                }
            }
        } else { // otherwise, set the end date as the same as the start date
            dateEndString = editActivity.getSelectedYear() + "-" + selectedMonth + "-" + editActivity.getSelectedDay().toString();
            selectedMonth2 = selectedMonth;
            selectedDay2 = selectedDay;
            selectedYear2 = selectedYear;
        }

        // delete the old activity and replace it with the new updated one

        Activity oldActivity = editActivity.getCurrentActivity();

        // if the activity completion is to be edited
        Calendar today = Calendar.getInstance();
        String todayString = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + today.get(Calendar.DAY_OF_MONTH);
        String completedDate;
        if (editActivity.isComplete()){
            if (oldActivity.getDateCompleted() != null){
                completedDate = oldActivity.getDateCompleted();
            } else {
                completedDate = todayString;
            }
        } else {
            completedDate = null;
        }

        Activity newActivity = new Activity(dateStartString, dateEndString, editActivity.getTitle(), editActivity.getActivityInfo(), oldActivity.getDateCreated(), completedDate);

        listOfActivities.delete(oldActivity);

        Date startDate = new Date(selectedYear,selectedMonth, selectedDay);
        Date todaysDate = new Date(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));

        int priority;

        // if the start date has already passed / is today, the priority is based on end date
        // if the start date is still in the future, the priority is based on start date
        if (todaysDate.compareTo(startDate) <= 0){
            priority = 99999999 - (selectedYear2 * 10000 + selectedMonth2 * 100 + selectedDay2);
        } else {
            priority =  -(selectedYear * 10000 + selectedMonth * 100 + selectedDay);
        }

        listOfActivities.add(newActivity, priority);

        System.out.print("Created new plan");

        updateWorkSheet();

        editActivity.setUpActivity(newActivity); // set the current activity as the new duplicate

        editActivity.displaySuccessMessage();
    }

    public void updateWorkSheet() {
        try {
            int size = listOfActivities.size();

            XSSFRow row;
            XSSFCell cell;

            for (int i = 1; i < size + 1; i++) {
                row = currentSheet.getRow(i);

                if (row == null) {
                    row = currentSheet.createRow(i);
                    row.setHeightInPoints(LoggerTableFormat.RowHeight.activityHeight);
                }
                Activity activity = listOfActivities.get(i - 1);
                String[] args = activity.getActivityArray();
                for (int j = 0; j < columnNames.length; j++) {
                    cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                    }
                    cell.setCellStyle(styleOrder[j]);
                    cell.setCellValue(args[j]);
                }
            }

            if (currentSheet != null) {

                // remove/clear rows from shortened activity lists
                boolean isFinished = false;
                int rowNum = size + 1;
                while (!isFinished) {
                    row = currentSheet.getRow(rowNum);
                    if (row != null) {
                        currentSheet.removeRow(row);
                    } else {
                        isFinished = true;
                    }
                }
            }

            // update the list of schedule names
            XSSFSheet listSheet = workbook.getSheetAt(0);

            for (int i = 0; i < listOfAgendas.size(); i++) {
                row = listSheet.getRow(i + 1);
                if (row == null) {
                    row = listSheet.createRow(i + 1);
                    row.createCell(0);
                }
                row.getCell(0).setCellValue(listOfAgendas.get(i));
            }

            // remove/clear rows from shortened schedule name list
            boolean isFinished = false;
            int rowNum = listOfAgendas.size() + 1;
            while (!isFinished) {
                row = listSheet.getRow(rowNum);
                if (row != null) {
                    listSheet.removeRow(row);
                } else {
                    isFinished = true;
                }
            }

            FileOutputStream fileOutput = new FileOutputStream("MyWorkbook.xlsx");

            workbook.write(fileOutput);

            fileOutput.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void viewManageAgenda() {
        String[] listNames = getCreatedAgendas();
        currentPanel.setVisible(false);
        currentPanel = manageAgenda;
        manageAgenda.setVisible(true);
        manageAgenda.updateComboBox(listNames);
        manageAgenda.setSuccessLabel("");
        this.add(manageAgenda, BorderLayout.CENTER);
        this.revalidate();

    }

    public void openSpecificAgenda() {
        System.out.println("opening new agenda");
        String nameOfAgenda = manageAgenda.getSelectedAgenda();
        if (nameOfAgenda.trim().equals("")) return;
        nameCurrentAgenda = nameOfAgenda.trim();
        titlePanel.updateAgendaName(nameCurrentAgenda);
        currentSheet = workbook.getSheet(nameCurrentAgenda);
        createListOfActivities(nameCurrentAgenda);
        manageAgenda.setSuccessLabel("Opened: " + nameCurrentAgenda);
    }

    public void openSpecificAgenda(String name) {
        System.out.println("opening new agenda");
        if (name.trim().equals("")) return;
        createListOfActivities(name);
        nameCurrentAgenda = name;
        titlePanel.updateAgendaName(nameCurrentAgenda);
        currentSheet = workbook.getSheet(nameCurrentAgenda);
        manageAgenda.setSuccessLabel("Opened: " + nameCurrentAgenda);
    }

    public void deleteSpecificAgenda() {
        System.out.println("deleting agenda");
        String nameOfAgenda = manageAgenda.getSelectedAgenda();
        if (nameOfAgenda.trim().equals("")) return;

        int confirmation = JOptionPane.showConfirmDialog(this, "<html>Are you sure you want to delete the schedule \"" + nameOfAgenda + "\"?</html>", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION)return;

        if (nameOfAgenda.trim().equals(nameCurrentAgenda)) {
            nameCurrentAgenda = "";
            titlePanel.updateAgendaName(nameCurrentAgenda);
            currentSheet = null;
            listOfActivities.clear();
        }

        int index = workbook.getSheetIndex(nameOfAgenda);

        workbook.removeSheetAt(index);

        listOfAgendas.delete(index - 1);

        updateWorkSheet();
        viewManageAgenda();

        manageAgenda.setSuccessLabel("Deleted: " + nameOfAgenda);

    }


    public void viewCreateAgenda() {
        currentPanel.setVisible(false);
        currentPanel = createNewAgenda;
        createNewAgenda.setVisible(true);
        this.add(createNewAgenda, BorderLayout.CENTER);
        this.revalidate();

    }

    public void createAgenda() {

        String newAgendaName = createNewAgenda.getTextField().trim();
        String[] listOfTakenNames = getCreatedAgendas();

        if (newAgendaName.length() > 30) {
            newAgendaName = newAgendaName.substring(0, 30);
        }

        if (!isValidAgendaName(newAgendaName)){
            createNewAgenda.displayErrorMsg();
            return;
        }

        createNewAgenda.newScheduleMsg();

        try {

            FileOutputStream fileOutput = new FileOutputStream("MyWorkbook.xlsx");

            workbook.createSheet(newAgendaName);

            currentSheet = workbook.getSheet(newAgendaName);

            XSSFRow row = currentSheet.getRow(0);

            if (row == null) {
                row = currentSheet.createRow(0);
            }

            row.setHeightInPoints(LoggerTableFormat.RowHeight.headerHeight);

            for (int i = 0; i < columnNames.length; i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(columnNames[i]);
                cell.setCellStyle(cellStyles.getHeaderStyle());
                currentSheet.setColumnWidth(i, LoggerTableFormat.headerWidths[i] * 55);
            }

            listOfAgendas.add(newAgendaName);

            workbook.write(fileOutput);

            openSpecificAgenda(newAgendaName);

            fileOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String[] getCreatedAgendas() {
        String[] listAgendaNames = new String[listOfAgendas.size()];

        for (int i = 0; i < listOfAgendas.size(); i++) {
            listAgendaNames[i] = listOfAgendas.get(i);
        }

        return listAgendaNames;
    }

    public void viewRenameAgenda(){
        String agendaName = manageAgenda.getSelectedAgenda();
        if (agendaName == null || agendaName.equals("")) return;
        currentPanel.setVisible(false);
        currentPanel = renameExistingAgenda;
        renameExistingAgenda.updateScheduleCurrentName(agendaName);
        renameExistingAgenda.refreshComponents();
        renameExistingAgenda.setVisible(true);
        this.add(renameExistingAgenda, BorderLayout.CENTER);
        this.revalidate();
    }

    public void renameAgenda(){
        String oldName = renameExistingAgenda.getPreviousName();
        String newName = renameExistingAgenda.getNewName();

        if (newName.length() > 30){
            newName = newName.substring(0,30);
        }
        if (!isValidAgendaName(newName)){
            renameExistingAgenda.displayErrorMsg();
            return;
        }


        int index = workbook.getSheetIndex(oldName);

        workbook.setSheetName(index,newName);

        listOfAgendas.replace(newName, index - 1);

        updateWorkSheet();

        renameExistingAgenda.refreshComponents();

        renameExistingAgenda.successfulRenameMsg(oldName, newName);

        renameExistingAgenda.updateScheduleCurrentName(newName);

        if (oldName.equals(nameCurrentAgenda)){
            titlePanel.updateAgendaName(newName);
        }


    }


    public boolean isValidAgendaName(String newAgendaName){
        String[] listOfTakenNames = getCreatedAgendas();

        if (newAgendaName.trim().equals("") || newAgendaName.trim().equals(workbook.getSheetAt(0).getSheetName())
                || newAgendaName.toLowerCase().trim().equals("history") || newAgendaName.startsWith("'") || newAgendaName.endsWith("'")) {
            return false;
        }

        String[] forbiddenChars = {"/", "\\", "?", "*", "[", "]"};

        for ( String currentChar : forbiddenChars){
            if (newAgendaName.contains(currentChar)){
                return false;
            }
        }
        for (String listOfTakenName : listOfTakenNames) {
            if (listOfTakenName.trim().equals(newAgendaName)) {
                System.out.println("ERROR NAME ALREADY TAKEN");
                return false;
            }
        }
        return true;
    }

    public String[][] createActivitiesArray() {
        String[][] array = new String[listOfActivities.size()][numColumns];

        for (int i = 0; i < listOfActivities.size(); i++) {
            array[i] = listOfActivities.get(i).getActivityArray();

        }
        return array;
    }

    public void viewCompleteOnly(){
        planTable.createTable(getCompletedActivitiesArray(true));
    }

    public void viewIncompleteOnly(){
        planTable.createTable(getCompletedActivitiesArray(false));
    }

    public String[][] getCompletedActivitiesArray(boolean isCompleted){
        SimpleLinkedList<Activity> linkedList = new SimpleLinkedList<>();

        Activity currentActivity;
        for (int i = 0; i < listOfActivities.size();i++){
            currentActivity = listOfActivities.get(i);
            if (currentActivity.getDateCompleted() != null && isCompleted){
                linkedList.add(currentActivity);
            } else if (currentActivity.getDateCompleted() == null && !isCompleted){
                linkedList.add(currentActivity);
            }
        }

        String[][] array = new String[linkedList.size()][numColumns];

        for (int i = 0; i < array.length; i++){
            array[i] = linkedList.get(i).getActivityArray();
        }

        return array;
    }

}
