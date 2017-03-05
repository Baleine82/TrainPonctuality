package me.arbogast.trainponctuality.gui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;


import me.arbogast.trainponctuality.R;

public class DateSelectionActivity extends AppCompatActivity {
    private DatePicker dtpDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selection);

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getInt("title"));
        //noinspection ConstantConditions
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, extras.getInt("color"))));

        dtpDate = (DatePicker) findViewById(R.id.datePicker);
        dtpDate.updateDate(extras.getInt("year"), extras.getInt("month"), extras.getInt("day"));
    }

    public void InputValidateClick(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("year", dtpDate.getYear() - 1900);
        resultIntent.putExtra("month", dtpDate.getMonth());
        resultIntent.putExtra("day", dtpDate.getDayOfMonth());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
