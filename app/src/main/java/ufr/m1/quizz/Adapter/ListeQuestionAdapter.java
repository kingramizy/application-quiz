package ufr.m1.quizz.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ufr.m1.quizz.R;
import ufr.m1.quizz.stockage.questionItem;

/**
 * Created by cedric on 11/10/15.
 */
public class ListeQuestionAdapter extends BaseAdapter {

    private Activity activity;//activite qui appel l'adapter
    private ArrayList<questionItem> items;//liste des elements a afficher

    //constructeur
    public ListeQuestionAdapter(Activity activity, ArrayList<questionItem> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //chargelent du layout d'un element du la listView
            convertView = inflater.inflate(R.layout.listview_question_item, null);
            holder = new ViewHolder();

            holder.question = (TextView) convertView.findViewById(R.id.item_question);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.question.setText(items.get(position).getQuestion());

        return convertView;
    }


    private class ViewHolder{
        TextView question;
    }
}