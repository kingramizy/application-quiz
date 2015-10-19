package ufr.m1.quizz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;


import java.util.ArrayList;

import ufr.m1.quizz.Adapter.ListeQuestionAdapter;
import ufr.m1.quizz.SQLite.DatabaseQuizz;
import ufr.m1.quizz.stockage.questionItem;

/**
 * Created by cedric on 11/10/15.
 */
public class GestionActivity extends ActionBarActivity {

    private static final String TAG = "GestionActivity";

    private DatabaseQuizz myDb;

    private ListView lvQuestion;
    private ListeQuestionAdapter adapter;
    private ArrayList<questionItem> data;

    private Button ajouter;
    private EditText saisiQuestion;
    private RadioButton rButVrai, rButFaux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion);
        Log.i(TAG, "OnCreate");
        data = new ArrayList<>();
        myDb = new DatabaseQuizz(this);

        //garder le clavier fermer
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        lvQuestion = (ListView) findViewById(R.id.lvQuestions);
        getListeQuestion();
        adapter = new ListeQuestionAdapter(this,data);
        lvQuestion.setAdapter(adapter);
        //Ajout d'un listener sur les ellement de la liste
        lvQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                demandeSuppression(position);
            }
        });

        saisiQuestion = (EditText)findViewById(R.id.question_saisie);

        rButFaux = (RadioButton)findViewById(R.id.radioButton_faux);
        rButVrai = (RadioButton)findViewById(R.id.radioButton_vrai);
        ajouter = (Button)findViewById(R.id.bt_ajouter);
        //Listener sur le boutton ajouter
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nouvelle_question = saisiQuestion.getText().toString();
                String nouvelle_reponse = "vrai";
                if (rButFaux.isActivated()){
                    nouvelle_reponse = "faux";
                }else if (rButVrai.isActivated()){
                    nouvelle_reponse = "vrai";
                }
                //Ajout de la question dans la base et dans l'array utiliser par l'adapter
                int id = myDb.insertQuestion(nouvelle_question, nouvelle_reponse);
                saisiQuestion.setText("");
                data.add(new questionItem(nouvelle_question, nouvelle_reponse,id));
                reloadListView();
            }
        });

    }

    /**
     * récupération des question dans la base
     */
    private void getListeQuestion(){
        myDb.chargerLesQuestions(data);
    }

    /**
     * rechargement de la listView
     */
    private void reloadListView(){
        adapter.notifyDataSetChanged();
    }

    /**
     * Alert dialog permettant de prevenir l'utilisateur avant suppression
     * @param idQuestion       l'id de la question a supprimmer
     */
    private void demandeSuppression(final int idQuestion){
            AlertDialog.Builder builder = new AlertDialog.Builder(GestionActivity.this);

            // titre de la boite de dialogue
            builder.setTitle(getResources().getString(R.string.alert_delete_titre));

            builder.setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    myDb.deleteQuestion(data.get(idQuestion).getId());
                    //db.execSQL("DELETE FROM questions WHERE _id = "+id);
                    data.remove(idQuestion);
                    reloadListView();
                }
            });

            builder.setNegativeButton(getResources().getString(R.string.btn_annul), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // removes the dialog from the screen
                }
            });

            builder.show();
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
