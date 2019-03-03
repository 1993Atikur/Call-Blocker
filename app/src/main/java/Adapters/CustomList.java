package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

import Database.Holder;
import UserDataModel.UserData;
import spark.loop.callblocker.R;

public class CustomList extends BaseAdapter {
    TextView NAME,NUMBER;
    Button Delete;
    Context context;
    Holder holder;
    ArrayList<UserData>item;
    public CustomList( Context context) {
        this.context=context;
        holder=new Holder(context);

    }

    public void setData(ArrayList<UserData>item){
        this.item=item;
    }

    @Override
    public int getCount() {
        return item.size();
    }


    @Override
    public Object getItem(int position) {
        return item.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.singleblock,parent,false);
        }
        try{
            NAME=convertView.findViewById(R.id.Name);
            NUMBER=convertView.findViewById(R.id.Number);
            Delete=convertView.findViewById(R.id.delete);
            NAME.setText(item.get(position).getName());
            NUMBER.setText(item.get(position).getNumber());
            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.Delete_Single_Number(item.get(position).getNumber(),item.get(position).getName());
                    item.remove(position);
                    notifyDataSetChanged();

                }
            });

        }catch (Exception e){}



    return convertView;
    }

}
