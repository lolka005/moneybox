package com.example.dip.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dip.Adapters.ListViewAdapterForCatList;
import com.example.dip.Classes.CatListViewClass;
import com.example.dip.DataBase.DBHelper;
import com.example.dip.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CatListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Button BackButton, CreateButton;
    private ListView ll;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private final List<CatListViewClass> list = new ArrayList<>();
    private ListViewAdapterForCatList arad;

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
        setContentView(R.layout.list_act_cat);
        BackButton = findViewById(R.id.butBackCat);
        CreateButton = findViewById(R.id.butCreateNewCat);
        ll = findViewById(R.id.llCat);
        DBHelper databaseHelper = new DBHelper(this);
        db = databaseHelper.open();
        userCursor = db.rawQuery("SELECT Categories.Cat_ID, Categories.Cat_Name_Rus,Categories.Cat_Name_Eng, CatAndMoves.Type_ID From Categories INNER JOIN CatAndMoves ON CatAndMoves.Cat_ID = Categories.Cat_ID ORDER BY Categories.Cat_ID;", null);
        String tempNameRus = "";
        String tempNameEng = "";
        Integer tempID = -1;
        boolean tempIncOn = false;
        boolean tempExcOn = false;
        while (userCursor.moveToNext()) {
            if (tempID.equals(-1)) {
                tempID = userCursor.getInt(0);
            }
            if (tempID.equals(userCursor.getInt(0))) {
                tempID = userCursor.getInt(0);
                tempNameRus = userCursor.getString(1);
                tempNameEng = userCursor.getString(2);
                if (userCursor.getInt(3) == 1) {
                    tempIncOn = true;
                } else {
                    tempExcOn = true;
                }
                if (userCursor.isLast()) {
                    if (tempNameRus.equals(userCursor.getString(1)) || tempNameEng.equals(userCursor.getString(2))) {
                        tempID = userCursor.getInt(0);
                        if (userCursor.getInt(3) == 1) {
                            tempIncOn = true;
                        } else {
                            tempExcOn = true;
                        }
                    }
                    list.add(new CatListViewClass(tempID, tempNameRus,tempNameEng, tempIncOn, tempExcOn));

                }
            } else {
                list.add(new CatListViewClass(tempID, tempNameRus,tempNameEng, tempIncOn, tempExcOn));
                tempID = userCursor.getInt(0);
                tempNameRus = userCursor.getString(1);
                tempNameEng = userCursor.getString(2);
                tempIncOn = false;
                tempExcOn = false;
                if (userCursor.getInt(3) == 1) {
                    tempIncOn = true;
                } else {
                    tempExcOn = true;
                }
                if (userCursor.isLast()) {
                    tempIncOn = false;
                    if (tempNameRus.equals(userCursor.getString(1)) || tempNameEng.equals(userCursor.getString(2))) {
                        tempID = userCursor.getInt(0);
                        if (userCursor.getInt(3) == 1) {
                            tempIncOn = true;
                        } else {
                            tempExcOn = true;
                        }
                    }
                    list.add(new CatListViewClass(tempID, tempNameRus,tempNameEng, tempIncOn, tempExcOn));
                }
            }
        }
        arad = new ListViewAdapterForCatList(list);
        ll.setOnItemClickListener(this);
        ll.setAdapter(arad);
    }

    /**
     * Функция нажатия на элемент списка. Открывает нужную категорию для удаления/редактирования
     * @param parent The AdapterView where the click happened.
     * @param view The view within the AdapterView that was clicked (this
     *            will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AddCategoryActivity.class);
        intent.putExtra("CatID", list.get(position).getCatID());
        intent.putExtra("IncOn", list.get(position).getIncOn());
        intent.putExtra("ExcOn", list.get(position).getExcOn());
        if(Resources.getSystem().getConfiguration().locale.getISO3Language().equals("eng")){
            intent.putExtra("CatName", list.get(position).getCatNameEng());
        }
        else{
            intent.putExtra("CatName", list.get(position).getCatNameRus());
        }
        startActivity(intent);
    }

    /**
     *Функция нажатия на кнопку "Назад"
     * @param view
     */
    public void ClickBackBtnCatList(View view) {
        onBackPressed();
    }

    /**
     * Функция нажатия на кнопку "Создать новую категорию"
     * @param view
     */
    public void ClickCreateBtnCatList(View view) {
        Intent intent = new Intent(this, AddCategoryActivity.class);
        startActivity(intent);
    }

}