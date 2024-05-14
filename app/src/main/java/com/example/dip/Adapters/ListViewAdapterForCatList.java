package com.example.dip.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dip.Classes.CatListViewClass;
import com.example.dip.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ListViewAdapterForCatList extends BaseAdapter {
    private final List<CatListViewClass> List;

    public ListViewAdapterForCatList(java.util.List<CatListViewClass> list) {
        List = list;
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        return List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_for_list_view_category, parent, false);
        }
        LinearLayout ll = (LinearLayout) convertView;
        TextView CategoryName = ll.findViewById(R.id.CatNameList);
        CheckBox IncOn = ll.findViewById(R.id.IncCheckList);
        CheckBox ExcOn = ll.findViewById(R.id.ExcCheckList);
        CatListViewClass catLVClass = List.get(position);
        if(Resources.getSystem().getConfiguration().locale.getISO3Language().equals("eng")){
            CategoryName.setText(catLVClass.getCatNameEng());
        }
        else{
            CategoryName.setText(catLVClass.getCatNameRus());
        }
        IncOn.setChecked(catLVClass.getIncOn());
        ExcOn.setChecked(catLVClass.getExcOn());
        return convertView;
    }
}
