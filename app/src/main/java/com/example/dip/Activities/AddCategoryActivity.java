package com.example.dip.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dip.DataBase.DBHelper;
import com.example.dip.R;

public class AddCategoryActivity extends AppCompatActivity {
    private Integer CatID;
    private String CatName;
    private Boolean IncOn, ExcOn;
    private Button SaveButton, DeleteButton;
    private EditText NewCategoryName;
    private CheckBox IncCheckBox, ExcCheckBox;
    private TextView ToolBarText;
    private SQLiteDatabase db;
    private Cursor userCursor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper databaseHelper = new DBHelper(this);
        db = databaseHelper.open();
        setContentView(R.layout.add_category);
        SaveButton = findViewById(R.id.SaveCatBtn);
        DeleteButton = findViewById(R.id.DeleteCatBtn);
        NewCategoryName = findViewById(R.id.EditTextNameCategory);
        IncCheckBox = findViewById(R.id.CheckBoxInc);
        ExcCheckBox = findViewById(R.id.CheckBoxExc);
        ToolBarText = findViewById(R.id.ToolbarTextNewCat);
        Bundle extras = getIntent().getExtras();
        try {
            CatID = extras.getInt("CatID");
            CatName = extras.getString("CatName");
            IncOn = extras.getBoolean("IncOn");
            ExcOn = extras.getBoolean("ExcOn");
            DeleteButton.setVisibility(View.VISIBLE);
            DeleteButton.setEnabled(true);
            IncCheckBox.setChecked(IncOn);
            ExcCheckBox.setChecked(ExcOn);
            NewCategoryName.setText(CatName);
            ToolBarText.setText("Редактирование категории");
        } catch (NullPointerException e) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.rightMargin = (int) getResources().getDimension(R.dimen.margin);
            lp.bottomMargin = (int) getResources().getDimension(R.dimen.margin);
            lp.topMargin = (int) getResources().getDimension(R.dimen.margin);
            lp.leftMargin = (int) getResources().getDimension(R.dimen.margin);
            ToolBarText.setText("Создание новой категории");
            SaveButton.setLayoutParams(lp);
        }
    }

    public void ClickSaveCatBtn(View view) {
        if (!NewCategoryName.getText().equals("")) {
            if (IncCheckBox.isChecked() || ExcCheckBox.isChecked()) {
                if (CatName == null || CatID == null || IncOn == null || ExcOn == null) {
                    if (getIDOfNewCategorie(NewCategoryName.getText().toString()) == -1) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                ContentValues cv1 = new ContentValues();
                                cv1.put("Cat_Name", NewCategoryName.getText().toString());
                                db.insert("Categories", null, cv1);
                                if (IncCheckBox.isChecked()) {
                                    ContentValues cv2 = new ContentValues();
                                    cv2.put("Cat_ID", getIDOfNewCategorie(NewCategoryName.getText().toString()));
                                    cv2.put("Cat_Name", NewCategoryName.getText().toString());
                                    db.insert("Categories", null, cv2);
                                    ContentValues cv3 = new ContentValues();
                                    cv3.put("Cat_ID", getIDOfNewCategorie(NewCategoryName.getText().toString()));
                                    cv3.put("Type_ID", 1);
                                    db.insert("CatAndMoves", null, cv3);
                                }
                                if (ExcCheckBox.isChecked()) {
                                    ContentValues cv2 = new ContentValues();
                                    cv2.put("Cat_ID", getIDOfNewCategorie(NewCategoryName.getText().toString()));
                                    cv2.put("Cat_Name", NewCategoryName.getText().toString());
                                    db.insert("CatAndMoves", null, cv2);
                                    ContentValues cv3 = new ContentValues();
                                    cv3.put("Cat_ID", getIDOfNewCategorie(NewCategoryName.getText().toString()));
                                    cv3.put("Type_ID", 2);
                                    db.insert("CatAndMoves", null, cv3);
                                }
                            }
                        };
                        Thread thread = new Thread(runnable);
                        thread.start();
                        goHome();
                    } else {
                        Toast toast = Toast.makeText(AddCategoryActivity.this, "Категория с таким наименованием уже существует!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            ContentValues cv1 = new ContentValues();
                            cv1.put("Cat_Name", NewCategoryName.getText().toString());
                            db.update("Categories", cv1, "Cat_ID = ?", new String[]{String.valueOf(CatID)});
                            if (IncCheckBox.isChecked()) {
                                userCursor = db.rawQuery("SELECT COUNT(*) FROM CatAndMoves WHERE Cat_ID = ? AND Type_ID = 1", new String[]{String.valueOf(CatID)});
                                while (userCursor.moveToNext()) {
                                    if (userCursor.getInt(0) == 0) {
                                        ContentValues cv2 = new ContentValues();
                                        cv2.put("Cat_ID", CatID);
                                        cv2.put("Type_ID", 1);
                                        db.insert("CatAndMoves", null, cv2);
                                    }
                                }
                            } else {
                                userCursor = db.rawQuery("SELECT COUNT(*) FROM CatAndMoves WHERE Cat_ID = ? AND Type_ID = 1", new String[]{String.valueOf(CatID)});
                                while (userCursor.moveToNext()) {
                                    if (userCursor.getInt(0) != 0) {
                                        db.delete("CatAndMoves", "Cat_ID = ? AND Type_ID = 1", new String[]{String.valueOf(CatID)});
                                    }
                                }
                            }
                            if (ExcCheckBox.isChecked()) {
                                userCursor = db.rawQuery("SELECT COUNT(*) FROM CatAndMoves WHERE Cat_ID = ? AND Type_ID = 2", new String[]{String.valueOf(CatID)});
                                while (userCursor.moveToNext()) {
                                    if (userCursor.getInt(0) == 0) {
                                        ContentValues cv3 = new ContentValues();
                                        cv3.put("Cat_ID", CatID);
                                        cv3.put("Type_ID", 2);
                                        db.insert("CatAndMoves", null, cv3);
                                    }
                                }
                            } else {
                                userCursor = db.rawQuery("SELECT COUNT(*) FROM CatAndMoves WHERE Cat_ID = ? AND Type_ID = 2", new String[]{String.valueOf(CatID)});
                                while (userCursor.moveToNext()) {
                                    if (userCursor.getInt(0) != 0) {
                                        db.delete("CatAndMoves", "Cat_ID = ? AND Type_ID = 2", new String[]{String.valueOf(CatID)});
                                    }
                                }
                            }
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();
                    goHome();
                }
            } else {
                Toast toast = Toast.makeText(this, "Категория должна иметь тип движения!", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(this, "Поле названия категории не может быть пустым!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void ClickDeleteCatBtn(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                db.delete("Categories", "Cat_ID = ?", new String[]{String.valueOf(CatID)});
                db.delete("CatAndMoves", "Cat_ID = ?", new String[]{String.valueOf(CatID)});
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

    private Integer getIDOfNewCategorie(String CatName) {
        int ID = -1;
        userCursor = db.rawQuery("SELECT Cat_ID FROM Categories WHERE Cat_Name = ?", new String[]{CatName});
        while (userCursor.moveToNext()) {
            ID = userCursor.getInt(0);
        }
        return ID;
    }
}