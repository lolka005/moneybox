package com.example.dip.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dip.DataBase.DBHelper;
import com.example.dip.Classes.IncExcListViewClass;
import com.example.dip.Adapters.ListViewAdapterForIncExcList;
import com.example.dip.R;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IncEcxListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private ListView ll;
    private TextView toolBarText;
    private Cursor userCursor;
    private final List<IncExcListViewClass> list = new ArrayList<>();
    private Integer MoveTypeExtra;
    private String StartDate, EndDate;

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.list_act_inc_exc);
        MoveTypeExtra = extras.getInt("MoveTypeID");
        StartDate = extras.getString("StartDate");
        EndDate = extras.getString("EndDate");
        ll = findViewById(R.id.ListView);
        toolBarText = findViewById(R.id.ToolbarList);
        DBHelper databaseHelper = new DBHelper(this);
        SQLiteDatabase db = databaseHelper.open();
        userCursor = db.rawQuery("SELECT Money.ID, Money.Sum, Categories.Cat_Name_Rus,Categories.Cat_Name_Eng, Currency.Currency_Name, Money.Date  FROM Money INNER JOIN Categories ON Categories.Cat_ID = Money.Cat_ID INNER JOIN Currency ON Currency.Currency_ID = Money.Currency_ID WHERE Money.MoveType_ID = ? AND (Money.Date >= ? AND Money.Date <= ?)", new String[]{String.valueOf(MoveTypeExtra), StartDate, EndDate});
        while (userCursor.moveToNext()) {
            Integer id = userCursor.getInt(0);
            Float sum = userCursor.getFloat(1);
            String type_name_rus = userCursor.getString(2);
            String type_name_eng = userCursor.getString(3);
            String currency_name = userCursor.getString(4);
            String date = userCursor.getString(5);
            list.add(new IncExcListViewClass(id, type_name_rus,type_name_eng, sum, currency_name, date));
        }
        ListViewAdapterForIncExcList arad = new ListViewAdapterForIncExcList(list);
        ll.setOnItemClickListener(this);
        ll.setAdapter(arad);
        if (MoveTypeExtra == 1) {
            toolBarText.setText(getResources().getString(R.string.IncListToolBar));
        } else if (MoveTypeExtra == 2) {
            toolBarText.setText(getResources().getString(R.string.ExcListToolBar));
        }
    }

    /**
     * Обработчик нажатия на элемент списка
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, IncExcActivity.class);
        intent.putExtra("MoveTypeID", MoveTypeExtra);
        if(Resources.getSystem().getConfiguration().locale.getISO3Language().equals("eng")){
            intent.putExtra("TypeName", list.get(position).getCategoryNameEng());
        }
        else{
            intent.putExtra("TypeName", list.get(position).getCategoryNameRus());
        }
        intent.putExtra("CurrentSum", list.get(position).getSum());
        intent.putExtra("MoneyID", list.get(position).getID());
        intent.putExtra("CurrencyName", list.get(position).getCurrencyName());
        intent.putExtra("Date", list.get(position).getDate());
        startActivity(intent);
    }

    /**
     * Обработчки нажатия на кнопкку "Назад"
     *
     * @param view
     */
    public void ClickBackBtnIncExcList(View view) {
        onBackPressed();
    }
}