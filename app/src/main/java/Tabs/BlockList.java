package Tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.CustomList;
import Database.Holder;
import spark.loop.callblocker.R;


@SuppressLint("ValidFragment")
public class BlockList extends Fragment implements View.OnClickListener {
    View view;
    Context context;
    ListView listView;
    Holder holder;
    Cursor cursor;
    ArrayList<String>Name,Number;
    int var=-1,id;
    public BlockList(Context context) {
        this.context=context;
        BackgroundTask task=new BackgroundTask();
        task.execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.blocklist,container,false);
        FunctionsOfButton();
        LoadList();
        return view;
    }


    public void LoadList(){

        listView=view.findViewById(R.id.blockList);
        CustomList customList=new CustomList(getContext());
        customList.setData(Name,Number);
        listView.setAdapter(customList);

    }
    public void FunctionsOfButton(){
        Button AddNumber=view.findViewById(R.id.addNumber);
        Button ClearList=view.findViewById(R.id.clearList);
        AddNumber.setOnClickListener(this);
        ClearList.setOnClickListener(this);
        CheckBox checkBox=view.findViewById(R.id.checkBox);
        if (var==1){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    holder.updateCheckState(1);
                }else {
                   holder.updateCheckState(0);

                }

            }
        });




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.addNumber:
                Toast.makeText(context, "Processing", Toast.LENGTH_SHORT).show();
                break;
            case R.id.clearList:
                holder.Delete_All_Number();
                Name.clear();
                Number.clear();
                LoadList();
                Toast.makeText(context, "Cleared", Toast.LENGTH_SHORT).show();

                break;


        }
    }

    public class BackgroundTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            holder=new Holder(context);
            Name=new ArrayList<>();
            Number=new ArrayList<>();
            cursor=holder.getData();

            while (cursor.moveToNext()){
                id=cursor.getInt(0);
                if(id==0){
                    var=cursor.getInt(1);
                }else {
                    Name.add(cursor.getString(2));
                    Number.add(cursor.getString(3));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    }

}
