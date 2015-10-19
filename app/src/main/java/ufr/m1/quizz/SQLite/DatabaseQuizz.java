package ufr.m1.quizz.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import ufr.m1.quizz.stockage.questionItem;

/**
 * Created by cedric on 09/10/15.
 */
public class DatabaseQuizz extends SQLiteOpenHelper{

    SQLiteDatabase db;
    private static final String TAG = "SQLite";
    private static final String DATABASE_NAME =  "Quizz.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table questions "
                    + "( _id integer primary key autoincrement, "
                    + "question text not null,"
                    + "reponse text not null) ;";

    public DatabaseQuizz(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        //db.execSQL("DROP TABLE 'questions'");
        db.execSQL(DATABASE_CREATE);
        db.execSQL("INSERT INTO questions (question, reponse) VALUES ('Le diable de Tasmanie vit dans la jungle du Brésil ?','faux')");
        db.execSQL("INSERT INTO questions (question, reponse) VALUES ('La sauterelle saute l équivalent de 200 fois sa taille ?','vrai')");
        db.execSQL("INSERT INTO questions (question, reponse) VALUES ('Les pandas hibernent ?','faux')");
        db.execSQL("INSERT INTO questions (question, reponse) VALUES ('On trouve des dromadaires en liberté en Australie ?','vrai')");
        db.execSQL("INSERT INTO questions (question, reponse) VALUES ('Le papillon monarque vole plus de 4000 km ?','vrai')");
        db.execSQL("INSERT INTO questions (question, reponse) VALUES ('Les gorilles mâles dorment dans les arbres ?','faux')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * fonction permettant l'ajout d'un element dans la BD
     * @param question      la question a ajouter
     * @param reponse       la reponse associer
     * @return      l'id de la question nouvellement ajouter
     */
    public int insertQuestion (String question, String reponse) {
        ContentValues values = new ContentValues();
        values.put("question", question);
        values.put("reponse", reponse);
        int id = (int)db.insert("questions", null, values);
        if (id >1){
            Log.i(TAG, "Insertion de question réussi");
        }else{
            Log.e(TAG, "erreur lors de l'insertion d'une question");
        }
        return id;
    }

    /**
     * Suppression de question
     * @param id_question       id de la question a supprimer
     */
    public void deleteQuestion(int id_question){
        Log.i(TAG, "DELETE from questions WHERE question = " + id_question);
        //db.execSQL("DELETE from questions WHERE _id = " + id);
        db.delete("questions", "_id  =?", new String[]{""+id_question});

    }

    /**
     * cursor de la requette select *
     * @return      le curseur de la requette
     */
    public Cursor getCursor() {
        this.db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM questions",null);
    }

    /**
     * charge toute les questions
     * @param lcs   List dans laquelle on ajoute les elements
     */
    public void chargerLesQuestions(List<questionItem> lcs) {
        Cursor cursor = this.getCursor() ;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String question = cursor.getString(1) ;
            String reponse = cursor.getString(2);
            int id = cursor.getInt(0);
            lcs.add(new questionItem(question,reponse,id));
            cursor.moveToNext();
        }
        cursor.close();
    }
}
