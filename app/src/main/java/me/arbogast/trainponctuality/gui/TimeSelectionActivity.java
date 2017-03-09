package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import me.arbogast.trainponctuality.R;

public class TimeSelectionActivity extends AppCompatActivity {
    private TimePicker dtpTime;
    private Calendar timeSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_selection);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getInt("title"));
        //noinspection ConstantConditions
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, extras.getInt("color"))));

        dtpTime = (TimePicker) findViewById(R.id.timePicker);
        dtpTime.setIs24HourView(true);
        timeSelection = GregorianCalendar.getInstance();
        timeSelection.setTimeInMillis(extras.getLong("date"));
        dtpTime.setCurrentHour(timeSelection.get(Calendar.HOUR_OF_DAY));
        dtpTime.setCurrentMinute(timeSelection.get(Calendar.MINUTE));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timeSelection", timeSelection.getTimeInMillis());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeSelection.setTimeInMillis(savedInstanceState.getLong("timeSelection"));
    }

    public void InputValidateClick(View view) {
        Intent resultIntent = new Intent();
        timeSelection.set(Calendar.HOUR_OF_DAY, dtpTime.getCurrentHour());
        timeSelection.set(Calendar.MINUTE, dtpTime.getCurrentMinute());
        resultIntent.putExtra("date", timeSelection.getTimeInMillis());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
