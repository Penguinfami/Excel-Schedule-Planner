public class Date {
    private int year;
    private int month;
    private int day;

    Date(String dateString) {
        String[] dateArray = dateString.split("-");
        year = Integer.parseInt(dateArray[0]);
        month = Integer.parseInt(dateArray[1]);
        day = Integer.parseInt(dateArray[2]);
    }

    Date(int[] intArray) {
        year = intArray[0];
        month = intArray[1];
        day = intArray[2];
    }

    Date(int y, int m, int d) {
        year = y;
        month = m;
        day = d;
    }


    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    // if the date param d is after this date, return 1
    // if the date param d is before this date, return -1;
    // if they are the same date, return 0;
    public int compareTo(Date d) {
        if (getYear() < d.getYear()) {
            return 1;
        } else if (getYear() == d.getYear()) {
            if (getMonth() < d.getMonth()) {
                return 1;
            } else if (getMonth() == d.getMonth()) {
                if (getDay() < d.getDay()) {
                    return 1;
                } else if (getDay() == d.getDay()) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
