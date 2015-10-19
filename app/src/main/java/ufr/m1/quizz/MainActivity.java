package ufr.m1.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by cedric on 11/10/15.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private Button gerer, jouer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");

        gerer = (Button) findViewById(R.id.gerer);
        jouer = (Button) findViewById(R.id.jouer);

        //Ajout des listener
        gerer.setOnClickListener(this);
        jouer.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick");
        Intent i;
        switch (v.getId()){
            case R.id.gerer:
                //aller vers la gestion des questions
                i = new Intent(this, GestionActivity.class);
                startActivity(i);
                break;
            case R.id.jouer:
                //aller jouer
                i = new Intent(this, QuizzActivity.class);
                startActivity(i);
                break;
        }
    }






    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }


}
