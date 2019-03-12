package Tabs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.CustomList;
import Data.Permission;
import Data.User;
import Data.UserData;
import Database.Holder;
import Database.State;
import spark.loop.callblocker.R;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;

@SuppressLint("ValidFragment")
public class BlockList extends Fragment implements View.OnClickListener, Permission, User, CompoundButton.OnCheckedChangeListener {
    Context context;
    Dialog dialog;
    CustomList customList;
    Holder holder;
    State state;
    EditText editname, editnumber;
    ArrayList<UserData> items;
    BackgroundTask task;

    public BlockList(Context context) {
        this.context = context;
        items = new ArrayList<>();
        task = new BackgroundTask();
        task.execute();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blocklist, container, false);
        ListView listView = view.findViewById(R.id.blockList);
        Button AddNumber = view.findViewById(R.id.addNumber);
        Button ClearList = view.findViewById(R.id.clearList);
        CheckBox checkBox = view.findViewById(R.id.checkBox);
        Button Choose = view.findViewById(R.id.choose);
        AddNumber.setOnClickListener(this);
        ClearList.setOnClickListener(this);
        Choose.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(this);

        if (state.isChecked()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        customList.setData(items);
        listView.setAdapter(customList);
        return view;
    }


    public void AddNumber() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.insertnumber);
        Button Save = dialog.findViewById(R.id.save);
        editname = dialog.findViewById(R.id.editname);
        editnumber = dialog.findViewById(R.id.editnumber);
        Button cancel = dialog.findViewById(R.id.cancel);
        Save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.show();


    }

    @Override
    public void Details(String name, String number, String incomingNumber) {
        if (!name.equals("-1") & !number.equals("-1")) {
            items.add(new UserData(name, number));
            customList.notifyDataSetChanged();
        }


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
                String ed1, ed2;
                ed1 = editname.getText().toString().trim();
                ed2 = editnumber.getText().toString().trim();
                if (!ed2.isEmpty()) {
                    if (ed1.isEmpty()) {
                        ed1 = "Unkonwn";
                    }
                    UserData data = new UserData(ed1, ed2);
                    items.add(data);
                    customList.notifyDataSetChanged();
                    holder.UserData(data);
                    dialog.dismiss();
                }


                break;
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.choose:
                if (hasPermission1() & hasPermission2()) {
                    Call();
                } else if (!hasPermission1() & hasPermission2()) {
                    requestPermissions(new String[]{READ_CONTACTS}, 3);

                } else if (hasPermission1() & !hasPermission2()) {
                    requestPermissions(new String[]{READ_CALL_LOG}, 2);

                } else{
                requestPermissions(new String[]{READ_CONTACTS, READ_CALL_LOG}, 3);

                }

                break;
        }

    }

    public void Call() {
        LogContact logContact = new LogContact(context, items, customList, holder);
        logContact.show(getChildFragmentManager(), "frag");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Call();
                else
                    Toast.makeText(context, "Allow Contact Permission", Toast.LENGTH_SHORT).show();
                    break;
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Call();
                else
                    Toast.makeText(context, "Allow Phone Calls Permission", Toast.LENGTH_SHORT).show();
                break;

            case 3:
                boolean contacts = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean calllog = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (contacts && calllog) {
                    Call();
                }
                else
                    Toast.makeText(context, "Permissions Required", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public boolean hasPermission1() {
        return (ContextCompat.checkSelfPermission(context, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);

    }

    @Override
    public boolean hasPermission2() {
        return (ContextCompat.checkSelfPermission(context, READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            state.updateCheckState(1);
        } else {
            state.updateCheckState(0);

        }

    }


    public class BackgroundTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            customList = new CustomList(context);
            state = new State(context);
            holder = new Holder(context);
            holder.getData(BlockList.this, "");
            return null;
        }

    }


}
