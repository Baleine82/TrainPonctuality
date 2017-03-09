package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import me.arbogast.trainponctuality.R;

public class DateSelectionActivity extends AppCompatActivity {
    private DatePicker dtpDate;
    private Calendar dateSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selection);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getInt("title"));
        //noinspection ConstantConditions
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, extras.getInt("color"))));

        dtpDate = (DatePicker) findViewById(R.id.datePicker);
        dateSelection = GregorianCalendar.getInstance();
        dateSelection.setTimeInMillis(extras.getLong("date"));
        dtpDate.updateDate(dateSelection.get(Calendar.YEAR), dateSelection.get(Calendar.MONTH), dateSelection.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("dateSelection", dateSelection.getTimeInMillis());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dateSelection.setTimeInMillis(savedInstanceState.getLong("dateSelection"));
    }

    public void InputValidateClick(View view) {
        Intent resultIntent = new Intent();
        dateSelection.set(Calendar.YEAR, dtpDate.getYear());
        dateSelection.set(Calendar.MONTH, dtpDate.getMonth());
        dateSelection.set(Calendar.DAY_OF_MONTH, dtpDate.getDayOfMonth());
        resultIntent.putExtra("date", dateSelection.getTimeInMillis());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
