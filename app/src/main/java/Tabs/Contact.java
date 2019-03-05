package Tabs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import Adapters.CustomRecyclerView;
import Data.SelectedListener;
import Data.UserData;
import spark.loop.callblocker.R;

@SuppressLint("ValidFragment")
public class Contact extends Fragment {
    Context context;
    Cursor cursor;
    View view;
    ArrayList<UserData> data;
    RecyclerView recyclerView;
    ContentLoader loader;
    CustomRecyclerView adapter;

    public Contact(Context context, ArrayList<UserData> data) {
        this.context = context;
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contact, container, false);
        recyclerView = view.findViewById(R.id.contactList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CustomRecyclerView(context);

        if (data.size() == 0) {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
            loader = new ContentLoader();
            loader.execute();
        } else {
            adapter.getUser(data);
            adapter.notifyDataSetChanged();
        }
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void getSelectedNames(SelectedListener listener) {

        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.booleanArray.get(i)) {
                listener.Selected(data.get(i).getName(), data.get(i).getNumber());
            }

        }

    }

    private class ContentLoader extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            while (cursor.moveToNext()) {
                data.add(new UserData(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));

            }
            return null;
        }
    }


}
