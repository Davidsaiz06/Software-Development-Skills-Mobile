package com.example.refereeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Live Match Control screen displaying scoreboard, live stopwatch timer,
 * incident logging quick action controls, and undo capability.
 */
public class MatchActivity extends AppCompatActivity {

    public static final String EXTRA_HOME_TEAM = MainActivity.EXTRA_HOME_TEAM;
    public static final String EXTRA_AWAY_TEAM = MainActivity.EXTRA_AWAY_TEAM;
    public static final String EXTRA_HOME_SCORE = "com.example.refereeapp.EXTRA_HOME_SCORE";
    public static final String EXTRA_AWAY_SCORE = "com.example.refereeapp.EXTRA_AWAY_SCORE";
    public static final String EXTRA_EVENT_LIST = "com.example.refereeapp.EXTRA_EVENT_LIST";

    private String homeTeamName;
    private String awayTeamName;
    private int homeScore = 0;
    private int awayScore = 0;

    private final ArrayList<MatchEvent> eventList = new ArrayList<>();

    private TextView tvHomeScore;
    private TextView tvAwayScore;
    private TextView tvMatchTimer;
    private Button btnTimerToggle;

    private EditText etEventMinute;
    private EditText etPlayerDetails;
    private RadioGroup rgTeamSelection;

    private Button btnHomeGoal;
    private Button btnAwayGoal;

    // Live Stopwatch Timer members
    private int secondsElapsed = 0;
    private boolean isTimerRunning = false;
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTimerRunning) {
                secondsElapsed++;
                int mins = secondsElapsed / 60;
                int secs = secondsElapsed % 60;
                String timeStr = String.format(Locale.getDefault(), "%02d:%02d", mins, secs);
                tvMatchTimer.setText(timeStr);
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Intent intent = getIntent();
        if (intent != null) {
            homeTeamName = intent.getStringExtra(EXTRA_HOME_TEAM);
            awayTeamName = intent.getStringExtra(EXTRA_AWAY_TEAM);
        }

        if (homeTeamName == null) homeTeamName = "Home Team";
        if (awayTeamName == null) awayTeamName = "Away Team";

        initViews();
        setupListeners();
        updateScoreboard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }

    private void initViews() {
        TextView tvHomeTeamName = findViewById(R.id.tv_home_team_name);
        TextView tvAwayTeamName = findViewById(R.id.tv_away_team_name);
        tvHomeScore = findViewById(R.id.tv_home_score);
        tvAwayScore = findViewById(R.id.tv_away_score);
        tvMatchTimer = findViewById(R.id.tv_match_timer);
        btnTimerToggle = findViewById(R.id.btn_timer_toggle);

        etEventMinute = findViewById(R.id.et_event_minute);
        etPlayerDetails = findViewById(R.id.et_player_details);
        rgTeamSelection = findViewById(R.id.rg_team_selection);

        btnHomeGoal = findViewById(R.id.btn_home_goal);
        btnAwayGoal = findViewById(R.id.btn_away_goal);
        Button btnYellowCard = findViewById(R.id.btn_yellow_card);
        Button btnRedCard = findViewById(R.id.btn_red_card);
        Button btnUndoLast = findViewById(R.id.btn_undo_last);
        Button btnFinishMatch = findViewById(R.id.btn_finish_match);

        tvHomeTeamName.setText(homeTeamName);
        tvAwayTeamName.setText(awayTeamName);

        String homeGoalBtnText = "+1 " + homeTeamName + " " + getString(R.string.label_goal);
        String awayGoalBtnText = "+1 " + awayTeamName + " " + getString(R.string.label_goal);
        btnHomeGoal.setText(homeGoalBtnText);
        btnAwayGoal.setText(awayGoalBtnText);

        btnTimerToggle.setOnClickListener(v -> toggleTimer());
        btnYellowCard.setOnClickListener(v -> recordCard(MatchEvent.EventType.YELLOW_CARD));
        btnRedCard.setOnClickListener(v -> recordCard(MatchEvent.EventType.RED_CARD));
        btnUndoLast.setOnClickListener(v -> undoLastEvent());
        btnFinishMatch.setOnClickListener(v -> finishMatch());
    }

    private void setupListeners() {
        btnHomeGoal.setOnClickListener(v -> recordGoal(true));
        btnAwayGoal.setOnClickListener(v -> recordGoal(false));
    }

    private void toggleTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            btnTimerToggle.setText(getString(R.string.btn_start_timer));
            timerHandler.removeCallbacks(timerRunnable);
        } else {
            isTimerRunning = true;
            btnTimerToggle.setText(getString(R.string.btn_pause_timer));
            timerHandler.post(timerRunnable);
        }
    }

    private int parseMinute() {
        String minuteStr = etEventMinute.getText().toString().trim();

        // If minute field is empty, automatically pre-fill using live timer!
        if (TextUtils.isEmpty(minuteStr)) {
            int currentMin = (secondsElapsed / 60) + 1;
            etEventMinute.setText(String.valueOf(currentMin));
            return currentMin;
        }

        try {
            int min = Integer.parseInt(minuteStr);
            if (min < 0 || min > 130) {
                etEventMinute.setError(getString(R.string.err_invalid_minute));
                etEventMinute.requestFocus();
                return -1;
            }
            return min;
        } catch (NumberFormatException e) {
            etEventMinute.setError(getString(R.string.err_invalid_minute));
            etEventMinute.requestFocus();
            return -1;
        }
    }

    private String getSelectedTeam() {
        int checkedId = rgTeamSelection.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_away_team) {
            return awayTeamName;
        } else {
            return homeTeamName;
        }
    }

    private void recordGoal(boolean isHome) {
        int minute = parseMinute();
        if (minute < 0) return;

        String teamName = isHome ? homeTeamName : awayTeamName;
        if (isHome) {
            homeScore++;
        } else {
            awayScore++;
        }
        updateScoreboard();

        String playerInfo = etPlayerDetails.getText().toString().trim();
        String desc = teamName + " - Goal";
        if (!TextUtils.isEmpty(playerInfo)) {
            desc += " (" + playerInfo + ")";
        }

        eventList.add(new MatchEvent(minute, desc, MatchEvent.EventType.GOAL));
        Toast.makeText(this, desc + " (" + minute + "')", Toast.LENGTH_SHORT).show();
        clearInputFields();
    }

    private void recordCard(MatchEvent.EventType cardType) {
        int minute = parseMinute();
        if (minute < 0) return;

        String targetTeam = getSelectedTeam();
        String playerInfo = etPlayerDetails.getText().toString().trim();

        String cardName = (cardType == MatchEvent.EventType.YELLOW_CARD) ? "Yellow Card" : "Red Card";
        String desc = targetTeam + " - " + cardName;
        if (!TextUtils.isEmpty(playerInfo)) {
            desc += " (" + playerInfo + ")";
        }

        eventList.add(new MatchEvent(minute, desc, cardType));
        Toast.makeText(this, desc + " (" + minute + "')", Toast.LENGTH_SHORT).show();
        clearInputFields();
    }

    private void undoLastEvent() {
        if (eventList.isEmpty()) {
            Toast.makeText(this, R.string.msg_nothing_to_undo, Toast.LENGTH_SHORT).show();
            return;
        }

        MatchEvent lastEvent = eventList.remove(eventList.size() - 1);

        if (lastEvent.getEventType() == MatchEvent.EventType.GOAL) {
            if (lastEvent.getDescription().startsWith(homeTeamName) && homeScore > 0) {
                homeScore--;
            } else if (lastEvent.getDescription().startsWith(awayTeamName) && awayScore > 0) {
                awayScore--;
            }
            updateScoreboard();
        }

        String msg = String.format(getString(R.string.msg_undo_success), lastEvent.getDescription());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void clearInputFields() {
        etEventMinute.setText("");
        etPlayerDetails.setText("");
        etEventMinute.clearFocus();
        etPlayerDetails.clearFocus();
    }

    private void updateScoreboard() {
        tvHomeScore.setText(String.valueOf(homeScore));
        tvAwayScore.setText(String.valueOf(awayScore));
    }

    private void finishMatch() {
        Intent intent = new Intent(MatchActivity.this, ReportActivity.class);
        intent.putExtra(EXTRA_HOME_TEAM, homeTeamName);
        intent.putExtra(EXTRA_AWAY_TEAM, awayTeamName);
        intent.putExtra(EXTRA_HOME_SCORE, homeScore);
        intent.putExtra(EXTRA_AWAY_SCORE, awayScore);
        intent.putExtra(EXTRA_EVENT_LIST, eventList);
        startActivity(intent);
    }
}
