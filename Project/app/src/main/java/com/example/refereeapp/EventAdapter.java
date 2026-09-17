package com.example.refereeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Custom ItemAdapter extending BaseAdapter to render match events using LayoutInflater.
 */
public class EventAdapter extends BaseAdapter {

    private final List<MatchEvent> eventList;
    private final LayoutInflater inflater;

    public EventAdapter(Context context, List<MatchEvent> eventList) {
        this.eventList = eventList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return eventList != null ? eventList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return eventList != null ? eventList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        ImageView imgIcon;
        TextView tvMinute;
        TextView tvDescription;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.event_item_row, parent, false);
            holder = new ViewHolder();
            holder.imgIcon = rowView.findViewById(R.id.img_event_icon);
            holder.tvMinute = rowView.findViewById(R.id.tv_event_minute);
            holder.tvDescription = rowView.findViewById(R.id.tv_event_description);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        MatchEvent event = (MatchEvent) getItem(position);
        if (event != null) {
            String minuteText = event.getMinute() + "'";
            holder.tvMinute.setText(minuteText);
            holder.tvDescription.setText(event.getDescription());

            if (event.getEventType() != null) {
                switch (event.getEventType()) {
                    case GOAL:
                        holder.imgIcon.setImageResource(R.drawable.ic_goal);
                        break;
                    case YELLOW_CARD:
                        holder.imgIcon.setImageResource(R.drawable.ic_yellow_card);
                        break;
                    case RED_CARD:
                        holder.imgIcon.setImageResource(R.drawable.ic_red_card);
                        break;
                }
            }
        }

        return rowView;
    }
}
