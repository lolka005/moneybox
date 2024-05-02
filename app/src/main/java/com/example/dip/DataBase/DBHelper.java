package com.example.dip.DataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper
{
    private static String DB_PATH;
    private static String DB_NAME = "Diplom.db";
    private static final int SCHEMA = 3;
    public static final String TABLE = "Money";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_MOVE_TYPE_ID = "MoveType_ID";
    public static final String COLUMN_SUM = "Sum";
    public static final String COLUMN_CATEGORY_ID = "Cat_ID";
    public static final String COLUMN_CURRENCY_ID = "Currency_ID";
    private Context myContext;

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, SCHEMA);
        myContext = context;
        DB_PATH = context.getFilesDir().getPath() + "/" + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    /**
     * Обновление БД в случае изменения версии
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */


    /**
     * Создание локальной БД по структуре из файла
     */
    public void create_db(){

        File file = new File(DB_PATH);
        if (!file.exists())
        {
            //получаем локальную бд как поток
            try(InputStream myInput = myContext.getAssets().open(DB_NAME);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }

    /**
     * Открытие подключения к БД
     * @return
     * @throws SQLException
     */
    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void update(SQLiteDatabase db, int oldVersion, SharedPreferences version)
    {
        if(oldVersion < SCHEMA)
        {
            myContext.deleteDatabase(DB_PATH);
            SharedPreferences.Editor mEditor = version.edit();
            mEditor.putInt("version", SCHEMA).commit();
            create_db();
        }
    }
}