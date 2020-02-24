package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WriteReview extends AppCompatActivity {

    TextView wr_commenter_id;
    Spinner wr_score;
    EditText wr_comment;

    String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review);

        wr_comment = (EditText)findViewById(R.id.wr_comment);
        wr_commenter_id = (TextView)findViewById(R.id.wr_commenter_id);
        wr_score = (Spinner)findViewById(R.id.wr_score);

        /*평점 스피너*/
        wr_score.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                score = (String) parent.getItemAtPosition(position);
                ((TextView)parent.getChildAt(0)).setTextColor(Color.YELLOW);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
