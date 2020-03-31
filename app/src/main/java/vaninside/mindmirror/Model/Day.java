package vaninside.mindmirror.Model;

import java.util.Calendar;

import vaninside.mindmirror.DateUtil;


public class Day extends ViewModel {
    String day;
    String fullDay;
    String year;
    String month;
    long milliseconds;

    public Day() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFullDay() { return fullDay; }

    public String getYear() { return year; }

    public String getMonth() { return month; }

    public long getMilliseconds() { return milliseconds; }

    // TODO : day에 달력일값넣기
    public void setCalendar(Calendar calendar){

        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);
        fullDay = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.NEW_DAY_FORMAT);
        year = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.YEAR_FORMAT);
        month = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.MONTH_FORMAT);
        milliseconds = calendar.getTimeInMillis();

    }

}
