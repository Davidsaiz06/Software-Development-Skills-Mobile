package com.example.refereeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Final Match Report Screen presenting the final match score,
 * summary statistics, and chronological event log.
 */
public class ReportActivity extends AppCompatActivity {

    private TextView tvFinalScoreHeader;
    private TextView tvMatchStats;
    private TextView tvEmptyEvents;
    private ListView lvMatchEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tvFinalScoreHeader = findViewById(R.id.tv_final_score_header);
        tvMatchStats = findViewById(R.id.tv_match_stats);
        tvEmptyEvents = findViewById(R.id.tv_empty_events);
        lvMatchEvents = findViewById(R.id.lv_match_events);
        Button btnNewMatch = findViewById(R.id.btn_new_match);

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
            ArrayList<MatchEvent> eventList = (ArrayList<MatchEvent>) intent.getSerializableExtra(MatchActivity.EXTRA_EVENT_LIST);

            int totalGoals = 0;
            int totalYellows = 0;
            int totalReds = 0;

            if (eventList != null) {
                for (MatchEvent event : eventList) {
                    if (event.getEventType() == MatchEvent.EventType.GOAL) {
                        totalGoals++;
                    } else if (event.getEventType() == MatchEvent.EventType.YELLOW_CARD) {
                        totalYellows++;
                    } else if (event.getEventType() == MatchEvent.EventType.RED_CARD) {
                        totalReds++;
                    }
                }
            }

            String statsText = String.format(Locale.getDefault(),
                    getString(R.string.stats_format), totalGoals, totalYellows, totalReds);
            tvMatchStats.setText(statsText);

            if (eventList == null || eventList.isEmpty()) {
                tvEmptyEvents.setVisibility(View.VISIBLE);
                lvMatchEvents.setVisibility(View.GONE);
            } else {
                tvEmptyEvents.setVisibility(View.GONE);
                lvMatchEvents.setVisibility(View.VISIBLE);

                EventAdapter adapter = new EventAdapter(this, eventList);
                lvMatchEvents.setAdapter(adapter);
            }
        }

        btnNewMatch.setOnClickListener(v -> {
            Intent mainIntent = new Intent(ReportActivity.this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        });
    }
}
