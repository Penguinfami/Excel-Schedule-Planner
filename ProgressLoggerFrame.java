
import javax.swing.JFrame;
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

import java.awt.Color;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Calendar;
import java.text.SimpleDateFormat;

import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.model.*;

public class ProgressLoggerFrame extends JFrame {

    private String[] columnNames = LoggerTableFormat.headerTitles;

    // is this a git update? this is recent update

    private LoggerButtonListener buttonListener = new LoggerButtonListener(this);
    private TitlePanel titlePanel = new TitlePanel();
    private ProgressLoggerOptions options = new ProgressLoggerOptions(buttonListener);
    private LogNewPlanActivity logNewPlan = new LogNewPlanActivity(buttonListener);
    private ProgressPlanTable planTable = new ProgressPlanTable();


    private UpdatePlannedActivity updateActivity = new UpdatePlannedActivity(buttonListener);

    private EditPlannedActivity editActivity = new EditPlannedActivity(buttonListener);

    private ManageAgenda manageAgenda = new ManageAgenda(buttonListener);

    private CreateNewAgenda createNewAgenda = new CreateNewAgenda(buttonListener);

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
        cellStyles = new LoggerCellStyles(workbook);
        styleOrder = cellStyles.getStyleOrder();
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(options, BorderLayout.SOUTH);
        viewManageAgenda();
        this.setVisible(true);
        this.requestFocusInWindow();

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
                            } else if (cell.getCellType() == CellType.NUMERIC) { // if the user set a title as a number, convert it to a string
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
                    String[] date = args[0].split("-"); // using start date

                    int priority = 99999999 - (Integer.parseInt(date[0]) * 10000 + Integer.parseInt(date[1]) * 100 + Integer.parseInt(date[2]));
                    listOfActivities.add(new Activity(args[0], args[1], args[2], args[3], args[4], args[5]), priority);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void viewPlan() {
        if (currentSheet == null) return;
        this.add(planTable, BorderLayout.CENTER);
        planTable.createTable(createActivitiesArray());
        planTable.setVisible(true);
        manageAgenda.setVisible(false);
        logNewPlan.setVisible(false);
        updateActivity.setVisible(false);
        createNewAgenda.setVisible(false);
        editActivity.setVisible(false);

    }

    public void logNewActivity() {
        if (currentSheet == null) return;
        this.add(logNewPlan, BorderLayout.CENTER);
        logNewPlan.removeSuccessMessage();
        manageAgenda.setVisible(false);
        planTable.setVisible(false);
        logNewPlan.setVisible(true);
        updateActivity.setVisible(false);
        createNewAgenda.setVisible(false);
        editActivity.setVisible(false);

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

        int priority = 99999999 - (selectedYear * 10000 + selectedMonth * 100 + selectedDay); // earlier dates have higher priority

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

        if (selectedMonth.equals("-")) {
            errorComponents.append("<br>Starting Month Is Not Set");
        }
        if (selectedDay == null) {
            errorComponents.append("<br>Starting Day Of Month Is Not Set");
        }
        if (selectedYear == null) {
            errorComponents.append("<br>Starting Year Is Not Set");
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
                            errorComponents.append("<br>End Month Cannot Be Earlier Than Start Date" + selectedMonth + selectedMonth2);
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
        String[] activityNamesDate = new String[listOfActivities.size() + 1];
        activityNamesDate[0] = " ";

        String title;

        for (int i = 1; i < activityNamesDate.length; i++) {
            title = listOfActivities.get(i - 1).getTitleOfActivity();
            activityNamesDate[i] = listOfActivities.get(i - 1).getDateStart() + " - " +
                    (title);

        }

        updateActivity.updatePanel(activityNamesDate);
        this.add(updateActivity, BorderLayout.CENTER);
        updateActivity.removeSuccessLabel();
        updateActivity.setVisible(true);
        manageAgenda.setVisible(false);
        planTable.setVisible(false);
        logNewPlan.setVisible(false);
        createNewAgenda.setVisible(false);
        editActivity.setVisible(false);


    }

    public void setCompleteActivity() {
        int index = updateActivity.getSelectedIndex();
        if (index != 0) {
            Calendar today = Calendar.getInstance();
            Activity selectedActivity = listOfActivities.get(index - 1);
            String dateCompletedString = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + today.get(Calendar.DAY_OF_MONTH);
            selectedActivity.setDateCompleted(dateCompletedString);
            //planTable.createTable(createActivitiesArray());
            updateWorkSheet();
            String name = selectedActivity.getTitleOfActivity();
            updateActivity.displaySuccessLabel("<html><h1>Activity <em>" + name + "</em> Completed!</h1></html>");
        }
    }

    public void viewEditActivity() {
        if (currentSheet == null) return;
        int index = updateActivity.getSelectedIndex();
        if (index != 0) {
            Activity chosenActivity = listOfActivities.get(index - 1);
            editActivity.setUpActivity(chosenActivity);
            editActivity.removeSuccessMessage();
            this.add(editActivity, BorderLayout.CENTER);
            updateActivity.removeSuccessLabel();
            updateActivity.setVisible(false);
            manageAgenda.setVisible(false);
            planTable.setVisible(false);
            logNewPlan.setVisible(false);
            createNewAgenda.setVisible(false);
            editActivity.setVisible(true);
        }
    }

    public void deleteActivity() {
        int index = updateActivity.getSelectedIndex();

        if (index != 0) {
            String name = listOfActivities.get(index - 1).getTitleOfActivity();
            listOfActivities.delete(index - 1); // delete the activity
            planTable.createTable(createActivitiesArray());
            updateWorkSheet();
            viewUpdateActivity(); // act as a refresher for an updated combo box
            updateActivity.displaySuccessLabel("<html><h1>The Activity <em>" + name + "</em> Has Been Deleted.</h1></html>");
        }

    }

    public void setUpdatedActivity() {
        String missingComponents = checkPlanValidity(editActivity);
        if (missingComponents != null) {
            editActivity.displayErrorMessage(missingComponents);
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

        Activity newActivity = new Activity(dateStartString, dateEndString, editActivity.getTitle(), editActivity.getActivityInfo(), oldActivity.getDateCreated(), oldActivity.getDateCompleted());

        listOfActivities.delete(oldActivity);

        int priority = 99999999 - (selectedYear * 10000 + selectedMonth * 100 + selectedDay); // earlier dates have higher priority

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

        this.add(manageAgenda, BorderLayout.CENTER);
        manageAgenda.setVisible(true);
        manageAgenda.updateComboBox(listNames);
        createNewAgenda.setVisible(false);
        updateActivity.setVisible(false);
        planTable.setVisible(false);
        logNewPlan.setVisible(false);
        editActivity.setVisible(false);
        manageAgenda.setSuccessLabel("");


    }

    public void openSpecificAgenda() {
        System.out.println("opening new agenda");
        String nameOfAgenda = manageAgenda.getSelectedAgenda();
        if (nameOfAgenda.trim().equals("")) return;
        //planTable.createTable(createActivitiesArray());
        nameCurrentAgenda = nameOfAgenda.trim();
        titlePanel.updateAgendaName(nameCurrentAgenda);
        currentSheet = workbook.getSheet(nameCurrentAgenda);
        createListOfActivities(nameCurrentAgenda);
        manageAgenda.setSuccessLabel("Opened: " + nameCurrentAgenda);
    }

    public void openSpecificAgenda(String name) {
        System.out.println("opening new agenda");
        String nameOfAgenda = name;
        if (nameOfAgenda.trim().equals("")) return;
        createListOfActivities(nameOfAgenda);
        //planTable.createTable(createActivitiesArray());
        nameCurrentAgenda = nameOfAgenda;
        titlePanel.updateAgendaName(nameCurrentAgenda);
        currentSheet = workbook.getSheet(nameCurrentAgenda);
        manageAgenda.setSuccessLabel("Opened: " + nameCurrentAgenda);
    }

    public void deleteSpecificAgenda() {
        System.out.println("deleting agenda");
        String nameOfAgenda = manageAgenda.getSelectedAgenda();
        if (nameOfAgenda.trim().equals("")) return;

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
        this.add(createNewAgenda, BorderLayout.CENTER);
        createNewAgenda.setVisible(true);
        createNewAgenda.removeErrorMsg();
        manageAgenda.setVisible(false);
        updateActivity.setVisible(false);
        planTable.setVisible(false);
        logNewPlan.setVisible(false);
        editActivity.setVisible(false);

    }

    public void createAgenda() {

        String newAgendaName = createNewAgenda.getTextField().trim();
        String[] listOfTakenNames = getCreatedAgendas();

        if (newAgendaName.trim().equals("") || newAgendaName.trim().equals(workbook.getSheetAt(0).getSheetName())) {
            createNewAgenda.displayErrorMsg();
            return;
        }

        if (newAgendaName.length() > 20) {
            newAgendaName = newAgendaName.substring(0, 20);
        }
        for (String listOfTakenName : listOfTakenNames) {
            if (listOfTakenName.trim().equals(newAgendaName)) {
                createNewAgenda.displayErrorMsg();
                System.out.println("ERROR NAME ALREADY TAKEN");
                return;
            }
        }

        createNewAgenda.removeErrorMsg();
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

    public String[][] createActivitiesArray() {
        String[][] array = new String[listOfActivities.size()][numColumns];


        for (int i = 0; i < listOfActivities.size(); i++) {
            array[i] = listOfActivities.get(i).getActivityArray();

        }

        return array;
    }

}
