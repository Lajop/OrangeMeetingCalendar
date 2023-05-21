package CalendarPk;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Calendar {
    private Period working_hours;
    private List<Period> planned_meeting;

    public Period getWorkingHours() {
        return working_hours;
    }

    public List<Period> getPlannedMeeting() {
        return planned_meeting;
    }

    //Read calendars from json files
    public static Calendar readCalendarFromFile(String filePath) {
        try {
            //Read the JSON file contents
            String json = new String(Files.readAllBytes(Paths.get(filePath)));

            //Parse JSON into Calendar object using Gson
            Gson gson = new Gson();
            return gson.fromJson(json, Calendar.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Period> findPossibleTimeOfMeeting(String filePathCalendar1, String filePathCalendar2, String meetingDuration){

        //Read from json files
        Calendar calendarFirst = Calendar.readCalendarFromFile(filePathCalendar1);
        Calendar calendarSecond = Calendar.readCalendarFromFile(filePathCalendar2);

        //Time formatting that will be use later and already formatting  meetingDurationTime to minutes
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        Duration meetingDurationTime = Duration.ofMinutes(LocalTime.parse(meetingDuration, timeFormatter).toSecondOfDay() / 60);

        //Assigning planned meetings to lists from both calendars
        List<Period> plannedMeetingsOfFirst = calendarFirst.getPlannedMeeting();
        List<Period> plannedMeetingsOfSecond = calendarSecond.getPlannedMeeting();

        //Final return object
        List<Period> possibleMeetings = new ArrayList<>();

        //Calculate the working hours
        LocalTime startOfWorkCal1 = LocalTime.parse(calendarFirst.getWorkingHours().getStart(), timeFormatter);
        LocalTime endOfWorkCal1 = LocalTime.parse(calendarFirst.getWorkingHours().getEnd(), timeFormatter);

        LocalTime startOfWorkCal2 = LocalTime.parse(calendarSecond.getWorkingHours().getStart(), timeFormatter);
        LocalTime endOfWorkCal2 = LocalTime.parse(calendarSecond.getWorkingHours().getEnd(), timeFormatter);

        //Settles possible start of meetings and their end according to working hours from both calendars
        LocalTime possibleMeetingStart = startOfWorkCal1.isAfter(startOfWorkCal2) ? startOfWorkCal1 : startOfWorkCal2;
        LocalTime possibleMeetingEnd = endOfWorkCal1.isBefore(endOfWorkCal2) ? endOfWorkCal1 : endOfWorkCal2;

        //Meeting range according to working hours from both calendars
        Period possibleMeetingRange = new Period(possibleMeetingStart.toString(), possibleMeetingEnd.toString());

        //Find possible meetings within the working hour intersection
        //Loops through possible meeting time. Starts from the time when both persons are already working and ends when at least one of them is not
        //Takes as an iteration point the meeting duration time
        for (LocalTime startTime = possibleMeetingStart; startTime.isBefore(possibleMeetingEnd); startTime = startTime.plus(meetingDurationTime)) {
            //Creates endTime which represent possible ending of current iteration of meeting by adding meeting duration to startTime
            LocalTime endTime = startTime.plus(meetingDurationTime);

            Period possibleMeeting = new Period(startTime.format(timeFormatter), endTime.format(timeFormatter));

            boolean isConflict = false; //initially there is no conflict

            //Checks if there is conflict in the first calendar of new meeting and already planned ones
            for (Period plannedMeeting : plannedMeetingsOfFirst) {
                if (isOverlapping(plannedMeeting, possibleMeeting)) {
                    isConflict = true;
                    break;
                }
            }

            //Checks if there is conflict in the second calendar of new meeting and already planned ones
            if (!isConflict) {
                for (Period plannedMeeting : plannedMeetingsOfSecond) {
                    if (isOverlapping(plannedMeeting, possibleMeeting)) {
                        isConflict = true;
                        break;
                    }
                }
            }

            //If there is no conflict in both first and second calendar then current iteration point is being added to possibleMeetings list
            //Which is final return object
            if (!isConflict) {
                possibleMeetings.add(possibleMeeting);
            }
        }


        //For debugging mainly
        System.out.println("Possible meeting range: " + possibleMeetingRange);
        System.out.println("Planned meetings of calendarFirst: " + plannedMeetingsOfFirst + ", and of calendarSecond:" + plannedMeetingsOfSecond);
        System.out.println("Possible meetings: " + possibleMeetings);

        return possibleMeetings;
    }

    //Check if time periods overlaps each other.
    private static boolean isOverlapping(Period period1, Period period2) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start1 = LocalTime.parse(period1.getStart(), timeFormatter);
        LocalTime end1 = LocalTime.parse(period1.getEnd(), timeFormatter);
        LocalTime start2 = LocalTime.parse(period2.getStart(), timeFormatter);
        LocalTime end2 = LocalTime.parse(period2.getEnd(), timeFormatter);

        //Checks if start of first given Period occurs before end of second period. And if end of first occurs after start of second
        //In other words it checks if one of them is a part of the second one. Returns true if it is. Returns false if they are separable
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}
