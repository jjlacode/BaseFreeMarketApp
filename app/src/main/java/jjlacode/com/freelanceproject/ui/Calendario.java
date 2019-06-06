package jjlacode.com.freelanceproject.ui;

import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.FragmentBase;

public class Calendario extends FragmentBase {


    @Override
    protected void setLayout() {

        layout = R.layout.calendario;

    }

    @Override
    protected void setInicio() {

        final OneCalendarView calendarView = view.findViewById(R.id.oneCalendar);

        calendarView.setOneCalendarClickListener(new OneCalendarView.OneCalendarClickListener() {
            @Override
            public void dateOnClick(Day day, int position) {




            }

            @Override
            public void dateOnLongClick(Day day, int position) {

            }
        });

        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {
            @Override
            public void prevMonth() {


            }

            @Override
            public void nextMonth() {


            }
        });

        calendarView.setMonthYear(4,2019);
        calendarView.addDaySelected(20);



    }
}
