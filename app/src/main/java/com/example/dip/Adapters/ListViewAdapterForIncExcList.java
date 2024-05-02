package com.example.dip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dip.Classes.IncExcListViewClass;
import com.example.dip.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapterForIncExcList extends BaseAdapter
{
    private List<IncExcListViewClass> List = new ArrayList<IncExcListViewClass>();

    public ListViewAdapterForIncExcList(List<IncExcListViewClass> list)
    {
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
        return List.get(position).getID();
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
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_for_list_view_inc_exc, parent, false);
        }
        LinearLayout ll = (LinearLayout) convertView;
        TextView CategoryText = ll.findViewById(R.id.CategoryText);
        TextView SumText = ll.findViewById(R.id.SumTextInListView);
        TextView CurrencyText = ll.findViewById(R.id.CurrencyText);
        IncExcListViewClass lvClass = List.get(position);
        CategoryText.setText(lvClass.getCategoryName());
        SumText.setText(String.valueOf(lvClass.getSum()));
        CurrencyText.setText(String.valueOf(lvClass.getCurrencyName()));
        return convertView;
    }
}