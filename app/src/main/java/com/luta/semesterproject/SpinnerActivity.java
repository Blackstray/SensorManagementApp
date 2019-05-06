package com.luta.semesterproject;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        switch (position){
            case 0:
                SensorsActivity.readDatabase();
                break;
            default:
                SensorsActivity.readDatabase(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
