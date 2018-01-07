package com.knstudios.guyknaan.algebranew;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import android.support.v7.app.AppCompatActivity;


public class SolutionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        final Context ctx = getApplication();


        Intent intent = getIntent();

        String[] message = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);

        final ArrayList<String> equationSolved = new ArrayList<>(Arrays.asList(message));

        //Log.d("DX","equationSolved:" + equationSolved.toString());

        final TextView output = (TextView) findViewById(R.id.solution);

        //AssetManager am = ctx.getApplicationContext().getAssets();

        //Typeface typeface = Typeface.createFromAsset(am, "fonts/Nutso2.otf");

        //output.setTypeface(typeface);


        final StringBuilder equationSolved_str = new StringBuilder();


        new CountDownTimer(1000 * equationSolved.size(), 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
            }

            public void onTick(long millisUntilFinished) {
                int  i = (int) (equationSolved.size() - millisUntilFinished/1000)-1;
                equationSolved_str.append(equationSolved.get(i) + "\n\n");
                output.setText(equationSolved_str.toString());
            }
        }.start();
        //for (int i = 0; i < equationSolved.size(); i++) {








        //output.setText("x + x = 1/2");

        //output.setFontFeatureSettings("afrc");
    }
}


