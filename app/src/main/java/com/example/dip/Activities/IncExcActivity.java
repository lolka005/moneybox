package com.example.dip.Activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dip.Classes.MyCurrencyClass;
import com.example.dip.DataBase.DBHelper;
import com.example.dip.Classes.MovesTypesClass;
import com.example.dip.R;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IncExcActivity extends AppCompatActivity {
    private TextView ToolBarText;
    private Button SaveButton, DeleteButton;
    private Spinner spinnerCategory, spinnerCurrency;
    private DBHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private List<MovesTypesClass> sqlAnswer;
    private Integer MoveTypeID, ID;
    private Float Sum;
    private String TypeName, CurrencyName, Date;
    private EditText DateEditText, SumText;
    private String DateText;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        MoveTypeID = extras.getInt("MoveTypeID");
        Sum = extras.getFloat("CurrentSum");
        ID = extras.getInt("MoneyID");
        TypeName = extras.getString("TypeName");
        CurrencyName = extras.getString("CurrencyName");
        Date = extras.getString("Date");
        setContentView(R.layout.add_act);
        SumText = findViewById(R.id.SumText);
        SumText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int dotPos = text.indexOf(".");
                if (dotPos >= 0 && text.length() - dotPos - 1 > 2){
                    s.delete(dotPos + 3, text.length());
                }
            }
        });
        DateEditText = findViewById(R.id.DateText);
        DateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(IncExcActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay)
                    {
                        String tempMonth;
                        String tempDay;
                        if(String.valueOf(mMonth).length() < 2)
                            tempMonth = "0" + String.valueOf(mMonth+1);
                        else
                            tempMonth = String.valueOf(mMonth+1);
                        if(String.valueOf(mDay).length() < 2)
                            tempDay = "0" + String.valueOf(mDay);
                        else
                            tempDay = String.valueOf(mDay);
                        DateEditText.setText(mYear + "-" + tempMonth + "-" + tempDay);
                        DateText = DateEditText.getText().toString() + " 00:00:00";
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        SaveButton = findViewById(R.id.Save);
        DeleteButton = findViewById(R.id.Delete);
        spinnerCategory = findViewById(R.id.categorySpinner);
        spinnerCurrency = findViewById(R.id.CurrencySpinnerInAdd);
        ToolBarText = findViewById(R.id.ToolbarTextInSecond);

        ArrayAdapter adapterForSpinnerCategory = new ArrayAdapter(this, R.layout.text_view_for_spinner);
        ArrayAdapter adapterForSpinnerCurency = new ArrayAdapter(this, R.layout.text_view_for_spinner);
        sqlAnswer = new ArrayList<>();
        databaseHelper = new DBHelper(this);
        db = databaseHelper.open();
        userCursor = db.rawQuery("SELECT Categories.Cat_ID, Cat_Name FROM Categories INNER JOIN CatAndMoves ON Categories.Cat_ID = CatAndMoves.Cat_ID WHERE CatAndMoves.Type_ID = ?", new String[]{String.valueOf(MoveTypeID)});
        while (userCursor.moveToNext()) {
            sqlAnswer.add(new MovesTypesClass(userCursor.getInt(0), userCursor.getString(1)));
        }
        String[] tempNameOfCategory = new String[sqlAnswer.size()];
        for (int i = 0; i < tempNameOfCategory.length; i++) {
            tempNameOfCategory[i] = sqlAnswer.get(i).getType_Name();
        }
        adapterForSpinnerCategory.addAll(tempNameOfCategory);
        spinnerCategory.setAdapter(adapterForSpinnerCategory);
        List<MyCurrencyClass> listOfCurrency = getCurrencyListFromDB();
        String[] tempNameOfCurrency = new String[listOfCurrency.size()];
        for(int i = 0; i < listOfCurrency.size(); i++){
            tempNameOfCurrency[i] = listOfCurrency.get(i).getName();
        }
        adapterForSpinnerCurency.addAll(tempNameOfCurrency);
        spinnerCurrency.setAdapter(adapterForSpinnerCurency);
        SumText.setText(String.valueOf(Sum));
        int needPositionCat = -1;
        int needPositionCur = -1;
        if (TypeName != null) {
            DeleteButton.setVisibility(View.VISIBLE);
            DeleteButton.setEnabled(true);
            for (int i = 0; i < tempNameOfCategory.length; i++) {
                if (tempNameOfCategory[i].equals(TypeName)) {
                    needPositionCat = i;
                    break;
                }
            }
            for (int i = 0; i < tempNameOfCurrency.length; i++) {
                if (tempNameOfCurrency[i].equals(CurrencyName)) {
                    needPositionCur = i;
                    break;
                }
            }
            spinnerCategory.setSelection(needPositionCat);
            spinnerCurrency.setSelection(needPositionCur);
            String[] tempStrinMas = Date.split(" ");
            DateEditText.setText(tempStrinMas[0]);
            DateText = Date;
            if (MoveTypeID == 1)
                ToolBarText.setText("Редактирование дохода");
            else if (MoveTypeID == 2)
                ToolBarText.setText("Редактирование расхода");
        } else {
            spinnerCategory.setSelection(0);
            spinnerCurrency.setSelection(0);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = (int) getResources().getDimension(R.dimen.margin);
            lp.bottomMargin = (int) getResources().getDimension(R.dimen.margin);
            lp.topMargin = (int) getResources().getDimension(R.dimen.margin);
            lp.leftMargin = (int) getResources().getDimension(R.dimen.margin);
            SaveButton.setLayoutParams(lp);
            if (MoveTypeID == 1)
                ToolBarText.setText("Добавление нового дохода");
            else if (MoveTypeID == 2)
                ToolBarText.setText("Добавление нового расхода");
        }
    }

     /**
     *  Событие при нажатии на кнопку сохранить
     * @param view
     */
    public void Save(View view)
    {
        String sSum = SumText.getText().toString();
        String tempDate = DateEditText.getText().toString();
        //проверка на пустое поле
        if (!sSum.matches("")) {
                if (!tempDate.matches("")) {
                    // проверка на то что в поле число больше нуля
                    if (Float.parseFloat(SumText.getText().toString()) > 0) {
                        //id = -1 только тогда, когда идёт создание нового расхода или дохода
                        if (ID == -1) {
                            //запуск процесса сохранения в отдельном потоке
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String temp = spinnerCategory.getSelectedItem().toString();
                                        Integer temp_ID = -1;
                                        for (int i = 0; i < sqlAnswer.size(); i++) {
                                            if (temp.equals(sqlAnswer.get(i).getType_Name())) {
                                                temp_ID = sqlAnswer.get(i).getTypeID();
                                                break;
                                            }
                                        }
                                        ContentValues cv = new ContentValues();
                                        cv.put(DBHelper.COLUMN_SUM, Double.valueOf(SumText.getText().toString()));
                                        cv.put(DBHelper.COLUMN_DATE, DateText);
                                        cv.put(DBHelper.COLUMN_CURRENCY_ID, spinnerCurrency.getSelectedItemId() + 1);
                                        cv.put(DBHelper.COLUMN_CATEGORY_ID, temp_ID);
                                        cv.put(DBHelper.COLUMN_MOVE_TYPE_ID, MoveTypeID);
                                        db.insert(DBHelper.TABLE, null, cv);
                                    } catch (RuntimeException e) {
                                        Log.e("ERROR", e.toString());
                                    }
                                }
                            };
                            Thread thread = new Thread(runnable);
                            thread.start();
                            goHome();
                        } else {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String temp = spinnerCategory.getSelectedItem().toString();
                                        Integer temp_ID = -1;
                                        for (int i = 0; i < sqlAnswer.size(); i++) {
                                            if (temp.equals(sqlAnswer.get(i).getType_Name())) {
                                                temp_ID = sqlAnswer.get(i).getTypeID();
                                                break;
                                            }
                                        }
                                        ContentValues cv = new ContentValues();
                                        cv.put(DBHelper.COLUMN_SUM, Double.valueOf(SumText.getText().toString()));
                                        cv.put(DBHelper.COLUMN_DATE, DateText);
                                        cv.put(DBHelper.COLUMN_CURRENCY_ID, spinnerCurrency.getSelectedItemId() + 1);
                                        cv.put(DBHelper.COLUMN_CATEGORY_ID, temp_ID);
                                        cv.put(DBHelper.COLUMN_MOVE_TYPE_ID, MoveTypeID);
                                        db.update(DBHelper.TABLE, cv, DBHelper.COLUMN_ID + "=" + ID, null);
                                    } catch (RuntimeException e) {
                                        Log.e("ERROR", e.toString());
                                    }
                                }
                            };
                            Thread thread = new Thread(runnable);
                            thread.start();
                            goHome();
                        }
                    } else {
                        Toast toast = Toast.makeText(this, "Поле суммы не может быть нулевым!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(this, "Поле даты не может быть пустым!", Toast.LENGTH_SHORT);
                    toast.show();
                }
        } else {
            Toast toast = Toast.makeText(this, "Поле суммы не может быть пустым!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Событие при нажатии на кнопку Удалить
     * @param view
     */
    public void Delete(View view)
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                db.delete(DBHelper.TABLE, "ID = ?", new String[]{String.valueOf(ID)});
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        goHome();
    }

    /**
     * Функция возврата на активность MainActivity
     */
    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /**
     * Функция возврата текущего курса валют из базы данных
     * @return Текущие курсы валют из базы данных
     */
    private List<MyCurrencyClass> getCurrencyListFromDB()
    {
        List<MyCurrencyClass> list = new ArrayList<>();
        userCursor = db.rawQuery("SELECT Currency_Name, Currency_Value FROM Currency", null);
        while(userCursor.moveToNext()){
            String name = userCursor.getString(0);
            Float value = userCursor.getFloat(1);
            list.add(new MyCurrencyClass(name, value));
        }
        return list;
    }
}