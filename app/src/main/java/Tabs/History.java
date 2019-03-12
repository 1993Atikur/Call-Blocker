package Tabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import Adapters.HistoryAdapter;
import Data.Helper;
import Data.UserData;
import Database.RejectedCalls;
import spark.loop.callblocker.R;

@SuppressLint("ValidFragment")
public class History extends Fragment implements View.OnClickListener, Helper {
    View view;
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    Context context;
    ArrayList<UserData> data;
    RejectedCalls rejectedCalls;
    Long InitialLength;

    public History(Context context) {
        this.context = context;
        data = new ArrayList<>();
        rejectedCalls = new RejectedCalls(context);
        InitialLength = rejectedCalls.Lastlength();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history, container, false);
        Button ClearAll = view.findViewById(R.id.clearCallHistory);
        ClearAll.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.callHistorylist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryAdapter(context);
        recyclerView.setAdapter(adapter);
        adapter.setData(data);
        rejectedCalls.getCallHistory(this);
        Update();
        return view;
    }

    @Override
    public void CallHistoryData(String name, String number, String date) {
        if (!name.equals("-1") && !number.equals("-1") && !date.equals("-1")) {
            data.add(new UserData(name, number, date));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        rejectedCalls.Delete_All_Number();
        data.clear();
        adapter.notifyDataSetChanged();

    }

    public void Update() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                if (rejectedCalls.Lastlength() != InitialLength) {
                    data.clear();
                    rejectedCalls.getCallHistory(History.this);
                    InitialLength = rejectedCalls.Lastlength();
                }

            }
        }, 1000);

    }

}


