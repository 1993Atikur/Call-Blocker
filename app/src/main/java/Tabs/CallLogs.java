package Tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Adapters.LogAdapter;
import Data.SelectedListener;
import Data.UserData;
import spark.loop.callblocker.R;

@SuppressLint("ValidFragment")
public class CallLogs extends Fragment {
    ArrayList<UserData> data;
    Context context;
    View view;
    RecyclerView recyclerView;
    Cursor cursor;
    LogAdapter adapter;
    ContentLoader loader;

    public CallLogs(Context context) {
        this.context = context;
        data = new ArrayList<>();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loader=new ContentLoader();
        loader.execute();
        view = inflater.inflate(R.layout.calllog, container, false);
        recyclerView=view.findViewById(R.id.callLog);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new LogAdapter(context);
        recyclerView.setAdapter(adapter);
        adapter.getUser(data);
        adapter.notifyDataSetChanged();

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
            cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
           int i=0;
            while (cursor.moveToNext() && i<500) {
                data.add(new UserData(cursor.getString(24), cursor.getString(14), cursor.getInt(12), cursor.getLong(18)));
                i++;
            }

            return null;
        }
    }


}
       /*
       recieved -type:1
       outgoing -type:2
       missed   -type:3
       M=cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);//24
       N=cursor.getColumnIndex(CallLog.Calls.NUMBER);//14
       O=cursor.getColumnIndex(CallLog.Calls.TYPE);//12
       P=cursor.getColumnIndex(CallLog.Calls.DATE);//18*/
