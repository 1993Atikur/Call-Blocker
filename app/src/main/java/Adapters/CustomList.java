package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import spark.loop.callblocker.R;

public class CustomList extends BaseAdapter {
    TextView NAME,NUMBER;
    Context context;
    ArrayList<String>Name,Number;
    public CustomList( Context context) {
        this.context=context;
    }

    public void setData(ArrayList<String>Name,ArrayList<String>Number){

        this.Name=Name;
       this.Number=Number;
    }

    @Override
    public int getCount() {
        return Name.size();
    }

    @Override
    public Object getItem(int position) {
        return Name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.singleblock,parent,false);
        }
        NAME=convertView.findViewById(R.id.Name);
        NUMBER=convertView.findViewById(R.id.Number);
        NAME.setText(Name.get(position));
        NUMBER.setText(Number.get(position));

    return convertView;
    }

}
