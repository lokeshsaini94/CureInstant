package com.cureinstant.cureinstant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.cureinstant.cureinstant.R;

public class NewQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton closeNewQuestion;
    private EditText questionTitle;
    private EditText questionDesc;
    private Switch questionDescSwitch;
    private Button questionButton;
    private boolean isSwitchChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        questionTitle = (EditText) findViewById(R.id.question_title);
        questionDesc = (EditText) findViewById(R.id.question_desc);
        questionDescSwitch = (Switch) findViewById(R.id.question_desc_switch);
        questionButton = (Button) findViewById(R.id.question_button);
        closeNewQuestion = (ImageButton) findViewById(R.id.close_new_question);

        closeNewQuestion.setOnClickListener(this);
        questionButton.setOnClickListener(this);

        questionDescSwitch.setChecked(false);
        isSwitchChecked = false;
        questionDescSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    questionDesc.setVisibility(View.VISIBLE);
                    isSwitchChecked = true;
                } else {
                    questionDesc.setVisibility(View.GONE);
                    isSwitchChecked = false;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_button:
                String title = questionTitle.getText().toString();
                String desc = questionDesc.getText().toString();

                if (title.isEmpty()) {
                    questionTitle.setError(getString(R.string.error_field_required));
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("title", title);
                    returnIntent.putExtra("desc", desc);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                break;
            case R.id.close_new_question:
                finish();
                break;
        }
    }
}
