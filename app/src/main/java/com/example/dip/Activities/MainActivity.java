package com.example.dip.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.window.SplashScreen;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dip.Classes.DatesListClass;
import com.example.dip.DataBase.DBHelper;
import com.example.dip.Classes.MyCurrencyClass;
import com.example.dip.XML.MyXMLReader;
import com.example.dip.R;
import com.example.dip.XML.XMLHelper;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor userCursor;
    private Button addButtonExc, addButtonInc, listButtonInc, listButtonExc, listButtonCat;
    private TextView sumText, toolBarText;
    private List<MyCurrencyClass> currencyList;
    private Spinner currencySpinner, monthSpinner;
    private PieChart pieChart;
    private List<DatesListClass> monthList;

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyList = new ArrayList<>();
        monthList = new ArrayList<>();
        setContentView(R.layout.main_act);
        addButtonExc = findViewById(R.id.btnAddExc);
        addButtonInc = findViewById(R.id.BtnAddInc);
        listButtonCat = findViewById(R.id.btnAllCat);
        listButtonExc = findViewById(R.id.btnAllExc);
        sumText = findViewById(R.id.Sum);
        currencySpinner = findViewById(R.id.CurrencySpinnerInMain);
        monthSpinner = findViewById(R.id.spinnerMonth);
        toolBarText = findViewById(R.id.ToolbarTextMain);
        pieChart = findViewById(R.id.PieChart);
        DBHelper databaseHelper = new DBHelper(getApplicationContext());
        XMLHelper xmlHelper = new XMLHelper();
        xmlHelper.execute("https://www.cbr-xml-daily.ru/daily_utf8.xml");
        SharedPreferences version = getSharedPreferences("version", 0);
        Integer version1 = version.getInt("version", 1);
        databaseHelper.create_db();
        db = databaseHelper.open();
        if (databaseHelper.update(version1, version)) {
            Intent mStartActivity = new Intent(MainActivity.this, SplashScreen.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this, mPendingIntentId, mStartActivity,
                    PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager mgr = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }
        try {
            String xmlString = xmlHelper.get(10, TimeUnit.SECONDS);
            MyXMLReader myXMLReader = new MyXMLReader(xmlString);
            currencyList = myXMLReader.getCurencyList();
            userCursor = db.rawQuery("SELECT * FROM Currency", null);
            if (userCursor.getCount() == 0) {
                putNewCurseOfCurrency();
            } else {
                updateCurseOfCurrency();
            }
        } catch (ExecutionException | InterruptedException | TimeoutException ex) {
            throw new RuntimeException(ex);
        }
        currencyList.clear();
        currencyList = getCurrencyListFromDB();
        ArrayAdapter adapterForSpinnerCur = new ArrayAdapter(this, R.layout.text_view_for_spinner);
        String[] temp = new String[currencyList.size()];
        for (int i = 0; i < currencyList.size(); i++) {
            temp[i] = currencyList.get(i).getName();
        }
        adapterForSpinnerCur.addAll(temp);
        currencySpinner.setAdapter(adapterForSpinnerCur);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                double roundedNumber = Math.round(getCurrentSum() * 100.0) / 100.0;
                sumText.setText(getResources().getString(R.string.mainTextViewText) + " " + roundedNumber);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currencySpinner.setSelection(0);
                FillChart();
            }
        });
        LocalDate tempDate = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            String Name = tempDate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru", "RUS"));
            String startDate = tempDate.withDayOfMonth(1).toString() + " 00:00:00";
            String endDate = tempDate.withDayOfMonth(tempDate.getMonth().length(tempDate.isLeapYear())).toString() + " 00:00:00";
            monthList.add(new DatesListClass(Name, startDate, endDate));
            tempDate = tempDate.minusMonths(1);
        }
        String[] months = new String[monthList.size()];
        for (int i = 0; i < monthList.size(); i++) {
            months[i] = monthList.get(i).getNameOfMonth();
        }
        ArrayAdapter adapterForSpinnerMonth = new ArrayAdapter(this, R.layout.text_view_for_spinner);
        adapterForSpinnerMonth.addAll(months);
        monthSpinner.setAdapter(adapterForSpinnerMonth);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                double roundedNumber = Math.round(getCurrentSum() * 100.0) / 100.0;
                sumText.setText(getResources().getString(R.string.mainTextViewText) + " " + roundedNumber);
                FillChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                monthSpinner.setSelection(0);
                FillChart();
            }
        });
    }

    /**
     * Обработчик нажатия на кнопку Добавить расход
     *
     * @param view
     */
    public void ClickBtnExc(View view) {
        Intent intent = new Intent(this, IncExcActivity.class);
        intent.putExtra("MoveTypeID", 2);
        intent.putExtra("MoneyID", -1);
        startActivity(intent);
    }

    /**
     * Обработчик нажатия на кнопку Добавить доход
     *
     * @param view
     */
    public void ClickBtnInc(View view) {
        Intent intent = new Intent(this, IncExcActivity.class);
        intent.putExtra("MoveTypeID", 1);
        intent.putExtra("MoneyID", -1);
        startActivity(intent);
    }

    /**
     * Обработчик нажатия на кнопку Все доходы
     *
     * @param view
     */
    public void ClickBtnAllInc(View view) {
        Intent intent = new Intent(this, IncEcxListActivity.class);
        intent.putExtra("MoveTypeID", 1);
        intent.putExtra("StartDate", monthList.get(monthSpinner.getSelectedItemPosition()).getStartDateOfMonth());
        intent.putExtra("EndDate", monthList.get(monthSpinner.getSelectedItemPosition()).getEndDateOfMonth());
        startActivity(intent);
    }

    /**
     * Обработчик нажатия на кнопку Все расходы
     *
     * @param view
     */
    public void ClickBtnAllExc(View view) {
        Intent intent = new Intent(this, IncEcxListActivity.class);
        intent.putExtra("MoveTypeID", 2);
        intent.putExtra("StartDate", monthList.get(monthSpinner.getSelectedItemPosition()).getStartDateOfMonth());
        intent.putExtra("EndDate", monthList.get(monthSpinner.getSelectedItemPosition()).getEndDateOfMonth());
        startActivity(intent);
    }

    public void ClickBtnAllCat(View view) {
        Intent intent = new Intent(this, CatListActivity.class);
        startActivity(intent);
    }

    /**
     * При закрытии приложения
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    /**
     * При возобновлении приложения
     */
    public void onResume() {
        super.onResume();
        sumText.setText(getResources().getString(R.string.mainTextViewText) + " " + (float) Math.round(getCurrentSum() * 100) / 100);
        FillChart();
    }

    /**
     * Функция возвращающая текущие накопления в рублях
     *
     * @return Накопления в рублях
     */
    private Float getCurrentSum() {
        List<Float> listSum = new ArrayList<>();
        Float TempExc;
        Float TempInc;
        for (int i = 0; i < currencySpinner.getCount(); i++) {
            TempInc = getSumInc(i + 1, monthList.get(monthSpinner.getSelectedItemPosition()).getStartDateOfMonth(), monthList.get(monthSpinner.getSelectedItemPosition()).getEndDateOfMonth());
            TempExc = getSumExc(i + 1, monthList.get(monthSpinner.getSelectedItemPosition()).getStartDateOfMonth(), monthList.get(monthSpinner.getSelectedItemPosition()).getEndDateOfMonth());
            listSum.add(TempInc - TempExc);
        }
        Float Sum = 0.0F;
        if (currencySpinner.getSelectedItemId() == 0) {
            for (int i = 0; i < listSum.size(); i++) {
                Sum += listSum.get(i);
            }
        } else {
            Float CurrencyValue = getCourse((int) currencySpinner.getSelectedItemId() + 1);
            for (int i = 0; i < listSum.size(); i++) {
                Sum += listSum.get(i) / CurrencyValue;
            }
        }
        return Sum;
    }

    /**
     * Функция записи новых валют
     */
    private void putNewCurseOfCurrency() {
        for (int i = 0; i < currencyList.size() + 1; i++) {
            if (i == 0) {
                ContentValues cv = new ContentValues();
                cv.put("Currency_Name", "Российский рубль");
                cv.put("Currency_Value", 1);
                db.insert("Currency", null, cv);
            } else {
                ContentValues cv = new ContentValues();
                cv.put("Currency_Name", currencyList.get(i - 1).getName());
                cv.put("Currency_Value", currencyList.get(i - 1).getValue());
                db.insert("Currency", null, cv);
            }
        }
    }

    /**
     * Функция обновления курса валют
     */
    private void updateCurseOfCurrency() {
        ContentValues cv = new ContentValues();
        for (int i = 1; i < currencyList.size(); i++) {
            cv.put("Currency_Value", currencyList.get(i - 1).getValue());
            db.update("Currency", cv, "Currency_ID =" + i + 1, null);
        }
    }

    /**
     * Функция возврата текущего курса валют из базы данных
     *
     * @return Текущие курсы валют из базы данных
     */
    private List<MyCurrencyClass> getCurrencyListFromDB() {
        List<MyCurrencyClass> list = new ArrayList<>();
        userCursor = db.rawQuery("SELECT Currency_Name, Currency_Value FROM Currency", null);
        while (userCursor.moveToNext()) {
            String name = userCursor.getString(0);
            Float value = userCursor.getFloat(1);
            list.add(new MyCurrencyClass(name, value));
        }
        return list;
    }

    /**
     * Функция возвращающая по ID валюты сумму доходов в рублях
     *
     * @param Cur_ID ID валюты
     * @return сумма всех доходов в заданной валюте
     */
    private Float getSumInc(int Cur_ID, String dateOfStartMonth, String dateOfEndMonth) {
        float sum = 0.0F;
        userCursor = db.rawQuery("SELECT Sum(Money.Sum) * Currency.Currency_Value FROM Money INNER JOIN Currency ON Money.Currency_ID = Currency.Currency_ID WHERE Currency.Currency_ID = ? AND Money.MoveType_ID = 1 AND (Money.Date >= ? AND Money.Date <= ?)", new String[]{String.valueOf(Cur_ID), dateOfStartMonth, dateOfEndMonth});
        while (userCursor.moveToNext()) {
            sum = userCursor.getFloat(0);
        }
        return sum;
    }

    /**
     * Функция возвращающая по ID валюты сумму расходов в рублях
     *
     * @param Cur_ID ID валюты
     * @return сумма всех расходов в заданной валюте
     */
    private Float getSumExc(int Cur_ID, String dateOfStartMonth, String dateOfEndMonth) {
        float sum = 0.0F;
        userCursor = db.rawQuery("SELECT Sum(Money.Sum) * Currency.Currency_Value FROM Money INNER JOIN Currency ON Money.Currency_ID = Currency.Currency_ID WHERE Currency.Currency_ID = ? AND Money.MoveType_ID = 2 AND (Money.Date >= ? AND Money.Date <= ?)", new String[]{String.valueOf(Cur_ID), dateOfStartMonth, dateOfEndMonth});
        while (userCursor.moveToNext()) {
            sum = userCursor.getFloat(0);
        }
        return sum;
    }

    /**
     * Функция возврающая курс валюты по её ID
     *
     * @param Cur_ID ID валюты
     * @return Курс валюты
     */
    private Float getCourse(Integer Cur_ID) {
        float Currency_Value = 0.0F;
        userCursor = db.rawQuery("SELECT Currency_Value FROM Currency WHERE Currency_ID = ?", new String[]{String.valueOf(Cur_ID)});
        while (userCursor.moveToNext()) {
            Currency_Value = userCursor.getFloat(0);
        }
        return Currency_Value;
    }

    private void FillChart() {
        pieChart.clearChart();
        pieChart.setTooltipText("sdfsdfsdfsdgsdg");
        Float TempExc = 0F;
        Float TempInc = 0F;
        for (int i = 0; i < currencySpinner.getCount(); i++) {
            TempInc += getSumInc(i + 1, monthList.get(monthSpinner.getSelectedItemPosition()).getStartDateOfMonth(), monthList.get(monthSpinner.getSelectedItemPosition()).getEndDateOfMonth());
            TempExc += getSumExc(i + 1, monthList.get(monthSpinner.getSelectedItemPosition()).getStartDateOfMonth(), monthList.get(monthSpinner.getSelectedItemPosition()).getEndDateOfMonth());
        }
        if (currencySpinner.getSelectedItemId() != 0) {
            Float CurrencyValue = getCourse((int) currencySpinner.getSelectedItemId() + 1);
            TempExc /= CurrencyValue;
            TempInc /= CurrencyValue;
        }
        if (TempInc != 0 && TempExc != 0) {
            pieChart.addPieSlice(
                    new PieModel(
                            "Доходы",
                            Math.round(TempInc),
                            Color.GREEN
                    )
            );
            pieChart.addPieSlice(new PieModel(
                            "Расходы",
                            Math.round(TempExc),
                            Color.RED
                    )
            );
            pieChart.startAnimation();
        } else {
            pieChart.addPieSlice(
                    new PieModel(
                            "Ничего",
                            1,
                            Color.GRAY
                    )
            );
        }
    }
}