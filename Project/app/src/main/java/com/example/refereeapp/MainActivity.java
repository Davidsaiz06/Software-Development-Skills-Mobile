package com.example.refereeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Setup Screen allowing the referee to enter home and away team names.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_HOME_TEAM = "com.example.refereeapp.EXTRA_HOME_TEAM";
    public static final String EXTRA_AWAY_TEAM = "com.example.refereeapp.EXTRA_AWAY_TEAM";

    private EditText etHomeTeam;
    private EditText etAwayTeam;
    private Button btnStartMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etHomeTeam = findViewById(R.id.et_home_team);
        etAwayTeam = findViewById(R.id.et_away_team);
        btnStartMatch = findViewById(R.id.btn_start_match);

        btnStartMatch.setOnClickListener(v -> startMatch());
    }

    private void startMatch() {
        String homeTeam = etHomeTeam.getText().toString().trim();
        String awayTeam = etAwayTeam.getText().toString().trim();

        if (TextUtils.isEmpty(homeTeam)) {
            etHomeTeam.setError(getString(R.string.err_empty_teams));
            etHomeTeam.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(awayTeam)) {
            etAwayTeam.setError(getString(R.string.err_empty_teams));
            etAwayTeam.requestFocus();
            return;
        }

        Intent intent = new Intent(MainActivity.this, MatchActivity.class);
        intent.putExtra(EXTRA_HOME_TEAM, homeTeam);
        intent.putExtra(EXTRA_AWAY_TEAM, awayTeam);
        startActivity(intent);
    }
}
