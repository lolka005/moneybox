package com.example.dip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dip.Classes.CatListViewClass;
import com.example.dip.Classes.IncExcListViewClass;
import com.example.dip.R;

import java.util.ArrayList;
import java.util.List;


public class ListViewAdapterForCatList extends BaseAdapter
{
    private List<CatListViewClass> List = new ArrayList<CatListViewClass>();
    public ListViewAdapterForCatList(java.util.List<CatListViewClass> list)
    {
        List = list;
    }

    @Override
    public int getCount()
    {
        return List.size();
    }

    @Override
    public Object getItem(int position)
    {
        return List.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Context context = parent.getContext();
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_for_list_view_category, parent, false);
        }
        LinearLayout ll = (LinearLayout) convertView;
        TextView CategoryName = ll.findViewById(R.id.CatNameList);
        CheckBox IncOn = ll.findViewById(R.id.IncCheckList);
        CheckBox ExcOn = ll.findViewById(R.id.ExcCheckList);
        CatListViewClass catLVClass = List.get(position);
        CategoryName.setText(catLVClass.getCatName());
        IncOn.setChecked(catLVClass.getIncOn());
        ExcOn.setChecked(catLVClass.getExcOn());
        return convertView;
    }
}
