package CalendarPk;

import static CalendarPk.Calendar.findPossibleTimeOfMeeting;

public class Main {
    public static void main(String[] args) {
        String filePathCalendar1 = "Calendars/calendar1.json";
        String filePathCalendar2 = "Calendars/calendar2.json";

        findPossibleTimeOfMeeting(filePathCalendar1, filePathCalendar2, "00:30");
    }
}
