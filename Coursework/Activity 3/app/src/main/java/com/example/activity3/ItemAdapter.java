package com.example.activity3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final String[] items;
    private final String[] prices;
    private final String[] descriptions;

    public ItemAdapter(Context context, String[] items, String[] prices, String[] descriptions) {
        this.items = items;
        this.prices = prices;
        this.descriptions = descriptions;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.my_list_view_detail, parent, false);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        TextView priceTextView = view.findViewById(R.id.priceTextView);

        nameTextView.setText(items[position]);
        descriptionTextView.setText(descriptions[position]);
        priceTextView.setText(prices[position]);

        return view;
    }
}