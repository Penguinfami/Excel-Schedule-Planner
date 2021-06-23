public class Activity {

    // dateStart timeStart dateEnd timeEnd titleofActivity
    // activityInfo dateCreated timeCreated dateCompleted timeCompleted
    private String dateStart;
    private String dateEnd;
    private String titleOfActivity;
    private String activityInfo;
    private String dateCreated;
    private String dateCompleted;

    public Activity(String dS, String dE, String tOA, String aI, String dC, String dCo) {
        dateStart = dS;
        dateEnd = dE;
        titleOfActivity = tOA;
        activityInfo = aI;
        dateCreated = dC;
        dateCompleted = dCo;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }


    public String getTitleOfActivity() {
        return titleOfActivity;
    }

    //String dC, String tC, String dCo, String tCo)
    public String getActivityInfo() {
        return activityInfo;
    }

    public String getDateCreated() {
        return dateCreated;
    }


    public String getDateCompleted() {
        return dateCompleted;
    }


    public String[] getActivityArray() {
        return new String[]{dateStart, dateEnd, titleOfActivity, activityInfo, dateCreated, dateCompleted};
    }

    public void setDateStart(String str) {
        dateStart = str;
    }

    public void setDateEnd(String str) {
        dateEnd = str;
    }


    public void setDateCreated(String str) {
        dateCreated = str;
    }

    public void setDateCompleted(String str) {
        dateCompleted = str;
    }


}
