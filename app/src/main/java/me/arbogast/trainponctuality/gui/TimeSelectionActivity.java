package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import me.arbogast.trainponctuality.R;

public class TimeSelectionActivity extends AppCompatActivity {
    private TimePicker dtpTime;

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
        dtpTime.setCurrentHour(extras.getInt("hour"));
        dtpTime.setCurrentMinute(extras.getInt("minute"));
    }

    public void InputValidateClick(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("hour", dtpTime.getCurrentHour());
        resultIntent.putExtra("minute", dtpTime.getCurrentMinute());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
