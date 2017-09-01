package com.rdm.notificacompromisso.view;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.model.Compromisso;
import com.rdm.notificacompromisso.presenter.db.CompromissosProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    public static String TAG = "CalendarFragment";
    private OnFragmentInteractionListener mListener;

    public CalendarFragment() {
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        showEventsCalendario(view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showEventsCalendario(View view) {
        final CompactCalendarView compactCalendar = (CompactCalendarView) view.findViewById(R.id.calendar_app);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendar.setUseThreeLetterAbbreviation(false);

        loadEvents(compactCalendar);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                if (events.size() == 0) {
                    return;
                }

                Intent intentShowEventos =  pegarIntentShowEvent(events.size());
                Compromisso compromisso = (Compromisso) events.get(0).getData();
                intentShowEventos.putExtra(getString(R.string.action_notification_show), compromisso);
                startActivity(intentShowEventos);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });
    }

    protected Intent pegarIntentShowEvent(int controle){
        if (controle == 1) {
            return new Intent(getContext(), EventosActivity.class);
        } else {
            return new Intent(getContext(), MultiEventosActivity.class);
        }
    }

    protected void loadEvents(CompactCalendarView compactCalendar){
        Cursor cursor = getActivity().managedQuery(CompromissosProvider.CONTENT_URI, null, null, null, CompromissosProvider.DIA);
        getActivity().startManagingCursor(cursor);
        if (cursor.moveToFirst()) {
            do {
                Compromisso compromisso = CompromissosProvider.compromissoFrom(cursor);
                Calendar calendar = Calendar.getInstance();
                calendar.set(compromisso.getAno(), compromisso.getMes(), compromisso.getDia());
                calendar.set(Calendar.HOUR_OF_DAY, compromisso.getHora());
                calendar.set(Calendar.MINUTE, compromisso.getMinuto());

                //String str = "Jun 13 2003 23:11:52.454 UTC";
                SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS", Locale.getDefault());
                String strEpochDate = df.format(calendar.getTime());

                try {
                    java.util.Date dateEpoch = df.parse(strEpochDate);
                    long epoch = dateEpoch.getTime();
                    Event ev1 = new Event(R.color.colorAccent, epoch, compromisso);
                    compactCalendar.addEvent(ev1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        getActivity().stopManagingCursor(cursor);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
