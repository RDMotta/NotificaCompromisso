package com.rdm.notificacompromisso.presenter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rdm.notificacompromisso.R;

/**
 * Created by Robson Da Motta on 08/08/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private Context mContext;

    public DbHelper(Context context) {
        super(context, context.getResources().getString(R.string.db_name), null, context.getResources().getInteger(R.integer.db_version));
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String [] tables = mContext.getResources().getStringArray(R.array.create_tables);

        for (String tabela: tables ) {
            sqLiteDatabase.execSQL(tabela);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //-- nada
    }
}
