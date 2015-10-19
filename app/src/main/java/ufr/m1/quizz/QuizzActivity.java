package ufr.m1.quizz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ufr.m1.quizz.SQLite.DatabaseQuizz;
import ufr.m1.quizz.stockage.questionItem;


public class QuizzActivity extends ActionBarActivity implements View.OnClickListener, LocationListener{

    Button vrai, faux, suivante, reponse;
    TextView question;
    ImageView image;
    int compteurQuestion = 0;
    Boolean reponseVue;

    public static double latitude;
    public static double longitude;

    private static final String SAVE_QUIZZ = "sauvegarde_quizz";
    private static final String TAG = "QUIZZ";

    private static final int CAMERA_REQUEST = 1888;
    private static final int REPONSE_REQUEST = 11;

    private DatabaseQuizz myDb;

    private ArrayList<questionItem> lesQuestions;
    private String reponseCourante = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        reponseVue = false;

        //recupération de la liste des question dans la base
        myDb = new DatabaseQuizz(this);
        lesQuestions = new ArrayList<>();
        myDb.chargerLesQuestions(lesQuestions);


        vrai = (Button)findViewById(R.id.vrai);
        faux = (Button)findViewById(R.id.faux);
        suivante = (Button)findViewById(R.id.suivante);
        reponse = (Button)findViewById(R.id.Vreponse);
        question = (TextView)findViewById(R.id.question);
        image = (ImageView)findViewById(R.id.image);

        //ajout des listener
        vrai.setOnClickListener(this);
        faux.setOnClickListener(this);
        suivante.setOnClickListener(this);
        reponse.setOnClickListener(this);

        //récupération de la sauvegarde
        /*if (savedInstanceState != null){
            Log.i(TAG, "restoration sauvegarde");

            compteurQuestion = savedInstanceState.getInt(SAVE_QUIZZ,1);
        }*/

        Log.i(TAG, "onCreate");

        actualiseQuestion();

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "restoration sauvegarde");

        compteurQuestion = savedInstanceState.getInt(SAVE_QUIZZ);
        Log.i(TAG, String.valueOf(compteurQuestion));
    }

    /**
     * Modifie le TextView avec la question correspondant au compteurQuestion
     * si celui-ci est suppérieur au nombre de question il esr repassé a 0
     * On enregistre aussi la bonne reponse dans reponseCourante
     */
    private void actualiseQuestion(){
        if (lesQuestions.size()<=compteurQuestion){
            compteurQuestion = 0;
        }
        question.setText(lesQuestions.get(compteurQuestion).getQuestion());
        reponseCourante = lesQuestions.get(compteurQuestion).getReponse();
    }


    /**
     * Fonction permettant de savoir si l'utilisateur a bien répondu ou non
     * si il a bien repondu, il passe a la question suivante,
     * sinon il reste sur la question
     * si il a vu la reponse alors il est passé a la question suivante mais un message (Toast) indique qu'il a vu la reponse
     * @param reponse
     */
    private void aRepondu(String reponse){

        if (reponse.equalsIgnoreCase(reponseCourante) && reponseVue == false){
            Toast.makeText(this, getResources().getString(R.string.correct), Toast.LENGTH_SHORT).show();
            compteurQuestion++;
            actualiseQuestion();
            reponseVue = false;
        }else if (reponse.equalsIgnoreCase(reponseCourante) && reponseVue == true){
            Toast.makeText(this, getResources().getString(R.string.reponseVue), Toast.LENGTH_SHORT).show();
            compteurQuestion++;
            actualiseQuestion();
            reponseVue = false;
        } else{
            Toast.makeText(this, getResources().getString(R.string.incorrect), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.suivante:
                    compteurQuestion++;
                    actualiseQuestion();
                break;
            case R.id.Vreponse:
                //Aller voir la reponse
                Intent goToReponse = new Intent(this, ReponseActivity.class);
                goToReponse.putExtra("reponse", reponseCourante);
                startActivityForResult(goToReponse, REPONSE_REQUEST);
                break;
            case R.id.vrai:
                    aRepondu("vrai");
                break;
            case R.id.faux:
                    aRepondu("faux");
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_photo){
            //ouvrir l'appareil photo
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }else if (id == R.id.action_localise){
            //ouvrir google map
            Uri uriLocation = Uri.parse("geo:" + new Double(latitude).toString() + "," + new Double(longitude).toString() + "?z=10");
            Intent intent = new Intent(Intent.ACTION_VIEW, uriLocation);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //recuperation de resultat des intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);//récupération de l'image
        }else if (requestCode == REPONSE_REQUEST && resultCode == RESULT_OK){
            Log.i(TAG, "récupération variable acti 2");
            reponseVue = data.getBooleanExtra("data", false);//recuperation du booleen permettant de savoir si la reponse a ete vu ou non
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "Sauvegarde");
        Log.i(TAG, String.valueOf(compteurQuestion));
        outState.putInt(SAVE_QUIZZ, compteurQuestion);
    }


    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getLongitude();
        latitude=loc.getLatitude();
        longitude=loc.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
