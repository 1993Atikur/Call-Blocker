package Tabs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.CustomList;
import Data.UserData;
import Database.Holder;
import Database.State;
import spark.loop.callblocker.Permission;
import spark.loop.callblocker.R;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_CALL_LOG;

@SuppressLint("ValidFragment")
public class BlockList extends Fragment implements View.OnClickListener, Permission {
    View view;
    Context context;
    Dialog dialog;
    CustomList customList;
    ListView listView;
    Holder holder;
    State state;
    Cursor cursor;
    Button AddNumber, ClearList, Save, cancel, Choose;
    EditText editname, editnumber;
    ArrayList<UserData> items;
    int var = -1;
    boolean permission;

    public BlockList(Context context) {
        this.context = context;
        items = new ArrayList<>();
        BackgroundTask task = new BackgroundTask();
        task.execute();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.blocklist, container, false);
        listView = view.findViewById(R.id.blockList);
        FunctionsOfButton();
        LoadList();
        return view;
    }


    public void LoadList() {


        customList = new CustomList(context);
        customList.setData(items);
        listView.setAdapter(customList);


    }

    public void FunctionsOfButton() {
        AddNumber = view.findViewById(R.id.addNumber);
        ClearList = view.findViewById(R.id.clearList);
        Choose = view.findViewById(R.id.choose);
        AddNumber.setOnClickListener(this);
        ClearList.setOnClickListener(this);
        Choose.setOnClickListener(this);

        CheckBox checkBox = view.findViewById(R.id.checkBox);
        cursor = state.getCheckState();
        while (cursor.moveToNext()) {
            var = cursor.getInt(0);
        }

        if (var == 1) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    state.updateCheckState(1);
                } else {
                    state.updateCheckState(0);

                }

            }
        });


    }

    public void AddNumber() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.insertnumber);
        Save = dialog.findViewById(R.id.save);
        editname = dialog.findViewById(R.id.editname);
        editnumber = dialog.findViewById(R.id.editnumber);
        cancel = dialog.findViewById(R.id.cancel);
        Save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.show();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addNumber:
                AddNumber();
                break;
            case R.id.clearList:
                items.clear();
                customList.notifyDataSetChanged();
                holder.Delete_All_Number();
                break;
            case R.id.save:
                UserData data = new UserData(editname.getText().toString(), editnumber.getText().toString());
                items.add(data);
                holder.UserData(data);
                customList.notifyDataSetChanged();
                dialog.dismiss();
                break;
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.choose:
                if (permission) {
                    LogContact logContact = new LogContact(context,items,customList,holder);
                    logContact.show(getChildFragmentManager(), "frag");
                } else {
                    requestPermissions(new String[]{READ_CONTACTS, READ_CALL_LOG}, 1);
                    permission=hasPermission();
                }
                break;
        }

    }

    @Override
    public boolean hasPermission() {
        int contacts = ContextCompat.checkSelfPermission(context, READ_CONTACTS);
        int calllog = ContextCompat.checkSelfPermission(context, READ_CALL_LOG);
        return ((contacts == PackageManager.PERMISSION_GRANTED) && (calllog == PackageManager.PERMISSION_GRANTED));
    }



    public class BackgroundTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            permission=hasPermission();
            state = new State(context);
            holder = new Holder(context);
            cursor = holder.getData();
            while (cursor.moveToNext()) {
                items.add(new UserData(cursor.getString(0), cursor.getString(1)));
            }

            return null;
        }


    }


}
