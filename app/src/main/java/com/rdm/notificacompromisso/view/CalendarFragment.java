package com.rdm.notificacompromisso.view;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.presenter.db.CompromissosProvider;
import com.rdm.notificacompromisso.presenter.utils.DialogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

/**
 * Temporário
 */
private void showEventsCalendario(View view){
    final CompactCalendarView compactCalendar = (CompactCalendarView) view.findViewById(R.id.calendar_app);
    compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
    compactCalendar.setUseThreeLetterAbbreviation(true);

    String URL = "content://" + CompromissosProvider.PROVIDER_NAME;
    Uri compromissos = Uri.parse(URL);

    Cursor cursor = getActivity().managedQuery(compromissos, null, null, null, CompromissosProvider.DIA);

    if (cursor.moveToFirst()) {
        do{
            String informacao =  cursor.getString(cursor.getColumnIndex(CompromissosProvider.DESCRICAO));
            int dia = cursor.getInt(cursor.getColumnIndex(CompromissosProvider.DIA));
            int mes = cursor.getInt(cursor.getColumnIndex(CompromissosProvider.MES));
            int ano = cursor.getInt(cursor.getColumnIndex(CompromissosProvider.ANO));
            int hora = cursor.getInt(cursor.getColumnIndex(CompromissosProvider.HORA));


            Calendar calendar = Calendar.getInstance();
            calendar.set(ano, mes-1, dia);
            calendar.set(Calendar.HOUR_OF_DAY, hora);

            //String str = "Jun 13 2003 23:11:52.454 UTC";
            SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS", Locale.getDefault());
            String strEpochDate = df.format(calendar.getTime());

            try {
                java.util.Date dateEpoch = df.parse(strEpochDate);
                long epoch = dateEpoch.getTime();
                Event ev1 = new Event(android.R.color.holo_blue_bright, epoch, informacao);
                compactCalendar.addEvent(ev1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } while (cursor.moveToNext());
    }
    cursor.close();

    compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
        @Override
        public void onDayClick(Date dateClicked) {
            List<Event> events = compactCalendar.getEvents(dateClicked);
            DialogUtils.showEventos(getContext(), events);
        }

        @Override
        public void onMonthScroll(Date firstDayOfNewMonth) {

        }
    });
}

 }
