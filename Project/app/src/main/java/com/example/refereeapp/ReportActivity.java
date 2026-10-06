package com.example.refereeapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    private enum FilterType { ALL, GOALS, CARDS }

    private TextView tvFinalScoreHeader;
    private TextView tvMatchStats;
    private TextView tvEmptyEvents;
    private ListView lvMatchEvents;

    private Button btnFilterAll;
    private Button btnFilterGoals;
    private Button btnFilterCards;

    private final List<MatchEvent> masterEventList = new ArrayList<>();
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        initViews();

        Intent intent = getIntent();
        if (intent != null) {
            String homeTeam = intent.getStringExtra(MatchActivity.EXTRA_HOME_TEAM);
            String awayTeam = intent.getStringExtra(MatchActivity.EXTRA_AWAY_TEAM);
            int homeScore = intent.getIntExtra(MatchActivity.EXTRA_HOME_SCORE, 0);
            int awayScore = intent.getIntExtra(MatchActivity.EXTRA_AWAY_SCORE, 0);

            if (homeTeam == null) homeTeam = "Home Team";
            if (awayTeam == null) awayTeam = "Away Team";

            String headerText = homeTeam + "  " + homeScore + " - " + awayScore + "  " + awayTeam;
            tvFinalScoreHeader.setText(headerText);

            @SuppressWarnings("unchecked")
            ArrayList<MatchEvent> receivedList = (ArrayList<MatchEvent>) intent.getSerializableExtra(MatchActivity.EXTRA_EVENT_LIST);

            if (receivedList != null) {
                masterEventList.addAll(receivedList);
            }

            calculateAndDisplayStats();

            adapter = new EventAdapter(this, masterEventList);
            lvMatchEvents.setAdapter(adapter);

            filterEvents(FilterType.ALL);
        }

        btnFilterAll.setOnClickListener(v -> filterEvents(FilterType.ALL));
        btnFilterGoals.setOnClickListener(v -> filterEvents(FilterType.GOALS));
        btnFilterCards.setOnClickListener(v -> filterEvents(FilterType.CARDS));

        Button btnNewMatch = findViewById(R.id.btn_new_match);
        btnNewMatch.setOnClickListener(v -> {
            Intent mainIntent = new Intent(ReportActivity.this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        });
    }

    private void initViews() {
        tvFinalScoreHeader = findViewById(R.id.tv_final_score_header);
        tvMatchStats = findViewById(R.id.tv_match_stats);
        tvEmptyEvents = findViewById(R.id.tv_empty_events);
        lvMatchEvents = findViewById(R.id.lv_match_events);

        btnFilterAll = findViewById(R.id.btn_filter_all);
        btnFilterGoals = findViewById(R.id.btn_filter_goals);
        btnFilterCards = findViewById(R.id.btn_filter_cards);
    }

    private void calculateAndDisplayStats() {
        int totalGoals = 0;
        int totalYellows = 0;
        int totalReds = 0;

        for (MatchEvent event : masterEventList) {
            if (event.getEventType() == MatchEvent.EventType.GOAL) {
                totalGoals++;
            } else if (event.getEventType() == MatchEvent.EventType.YELLOW_CARD) {
                totalYellows++;
            } else if (event.getEventType() == MatchEvent.EventType.RED_CARD) {
                totalReds++;
            }
        }

        String statsText = String.format(Locale.getDefault(),
                getString(R.string.stats_format), totalGoals, totalYellows, totalReds);
        tvMatchStats.setText(statsText);
    }

    private void filterEvents(FilterType filterType) {
        List<MatchEvent> filteredList = new ArrayList<>();

        if (filterType == FilterType.ALL) {
            filteredList.addAll(masterEventList);
        } else if (filterType == FilterType.GOALS) {
            for (MatchEvent event : masterEventList) {
                if (event.getEventType() == MatchEvent.EventType.GOAL) {
                    filteredList.add(event);
                }
            }
        } else if (filterType == FilterType.CARDS) {
            for (MatchEvent event : masterEventList) {
                if (event.getEventType() == MatchEvent.EventType.YELLOW_CARD ||
                        event.getEventType() == MatchEvent.EventType.RED_CARD) {
                    filteredList.add(event);
                }
            }
        }

        if (adapter != null) {
            adapter.updateList(filteredList);
        }

        if (filteredList.isEmpty()) {
            tvEmptyEvents.setVisibility(View.VISIBLE);
            lvMatchEvents.setVisibility(View.GONE);
        } else {
            tvEmptyEvents.setVisibility(View.GONE);
            lvMatchEvents.setVisibility(View.VISIBLE);
        }

        updateFilterButtonStyles(filterType);
    }

    private void updateFilterButtonStyles(FilterType selectedFilter) {
        int activeColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);

        btnFilterAll.setBackgroundTintList(ColorStateList.valueOf(
                selectedFilter == FilterType.ALL ? activeColor : inactiveColor));
        btnFilterGoals.setBackgroundTintList(ColorStateList.valueOf(
                selectedFilter == FilterType.GOALS ? activeColor : inactiveColor));
        btnFilterCards.setBackgroundTintList(ColorStateList.valueOf(
                selectedFilter == FilterType.CARDS ? activeColor : inactiveColor));
    }
}
