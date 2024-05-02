package com.example.dip.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dip.Adapters.ListViewAdapterForCatList;
import com.example.dip.Adapters.ListViewAdapterForIncExcList;
import com.example.dip.Classes.CatListViewClass;
import com.example.dip.Classes.IncExcListViewClass;
import com.example.dip.DataBase.DBHelper;
import com.example.dip.R;

import java.util.ArrayList;
import java.util.List;

public class CatListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Button BackButton, CreateButton;
    private ListView ll;
    private DBHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private List<CatListViewClass> list = new ArrayList<>();
    private ListViewAdapterForCatList arad;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_act_cat);
        BackButton = findViewById(R.id.butBackCat);
        CreateButton = findViewById(R.id.butCreateNewCat);
        ll = findViewById(R.id.llCat);
        databaseHelper = new DBHelper(this);
        db = databaseHelper.open();
        userCursor = db.rawQuery("SELECT Categories.Cat_ID, Categories.Cat_Name, CatAndMoves.Type_ID From Categories INNER JOIN CatAndMoves ON CatAndMoves.Cat_ID = Categories.Cat_ID ORDER BY Categories.Cat_ID;", null);
        String tempName = "";
        Integer tempID = -1;
        Boolean tempIncOn = false;
        Boolean tempExcOn = false;
        while(userCursor.moveToNext())
        {
            if(tempID.equals(-1))
            {
                tempID = userCursor.getInt(0);
            }
            if(tempID.equals(userCursor.getInt(0)))
            {
                tempID = userCursor.getInt(0);
                tempName = userCursor.getString(1);
                if(userCursor.getInt(2) == 1)
                {
                    tempIncOn = true;
                }
                else
                {
                    tempExcOn = true;
                }
                if(userCursor.isLast())
                {
                    if(tempName.equals(userCursor.getString(1)))
                    {
                        tempID = userCursor.getInt(0);
                        if(userCursor.getInt(2) == 1)
                        {
                            tempIncOn = true;
                        }
                        else
                        {
                            tempExcOn = true;
                        }
                    }
                    list.add(new CatListViewClass(tempID,tempName,tempIncOn,tempExcOn));

                }
            }
            else
            {
              list.add(new CatListViewClass(tempID,tempName,tempIncOn,tempExcOn));
              tempID = userCursor.getInt(0);
              tempName = userCursor.getString(1);
              tempIncOn = false;
              tempExcOn = false;
                if(userCursor.getInt(2) == 1)
                {
                    tempIncOn = true;
                }
                else
                {
                    tempExcOn = true;
                }
                if(userCursor.isLast())
                {
                    tempIncOn = false;
                    if(tempName.equals(userCursor.getString(1)))
                    {
                        tempID = userCursor.getInt(0);
                        if(userCursor.getInt(2) == 1)
                        {
                            tempIncOn = true;
                        }
                        else
                        {
                            tempExcOn = true;
                        }
                    }
                    list.add(new CatListViewClass(tempID,tempName,tempIncOn,tempExcOn));
                }
            }
        }
        arad = new ListViewAdapterForCatList(list);
        ll.setOnItemClickListener(this);
        ll.setAdapter(arad);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(this, AddCategoryActivity.class);
        intent.putExtra("CatID",list.get(position).getCatID());
        intent.putExtra("CatName",list.get(position).getCatName());
        intent.putExtra("IncOn",list.get(position).getIncOn());
        intent.putExtra("ExcOn",list.get(position).getExcOn());
        startActivity(intent);
    }

    public void ClickBackBtnCatList(View view)
    {
        onBackPressed();
    }

    public void ClickCreateBtnCatList(View view)
    {
        Intent intent = new Intent(this, AddCategoryActivity.class);
        startActivity(intent);
    }

}