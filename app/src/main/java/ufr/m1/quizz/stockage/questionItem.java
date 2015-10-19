package ufr.m1.quizz.stockage;

/**
 * Created by cedric on 16/10/15.
 */
public class questionItem {
    private String question, reponse;
    private int id;

    //permet de stocker tt les elements d'une question, la question, la reponse et l'id
    public questionItem(String question, String reponse, int id) {
        this.question = question;
        this.reponse = reponse;
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
