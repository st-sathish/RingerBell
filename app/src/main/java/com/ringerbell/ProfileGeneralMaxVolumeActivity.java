package com.ringerbell;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileGeneralMaxVolumeActivity extends AppCompatActivity {

    private EditText message;

    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_general_max_volume);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences("RINGER_BELL", Context.MODE_PRIVATE);
        message = (EditText) findViewById(R.id.silent_killer_et);

        final Button save = (Button) findViewById(R.id.action_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("KEY_MESSAGE", message.getText().toString());
                editor.apply();
                message.setText("");
                Toast.makeText(ProfileGeneralMaxVolumeActivity.this, "Successfully Saved", Toast.LENGTH_LONG).show();
            }
        });

    }

}
