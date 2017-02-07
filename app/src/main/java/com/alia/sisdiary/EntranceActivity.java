package com.alia.sisdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EntranceActivity extends AppCompatActivity {
    private static final String TAG = "EntranceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

    }

    public void onComeIn(View view) {
        Log.d(TAG, "Launch click ComeIN",
                new Exception());
        EditText nameView = (EditText) findViewById(R.id.name);
        String childName = nameView.getText().toString();
        Intent intent = new Intent(this, TimetableActivity.class);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, childName);
        startActivity(intent);

    }
}
