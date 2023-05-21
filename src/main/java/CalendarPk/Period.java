package CalendarPk;

public class Period {
    private String start;
    private String end;

    public Period(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "["+start + "," +end+"]";
    }
}
