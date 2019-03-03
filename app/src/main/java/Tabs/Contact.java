package Tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import spark.loop.callblocker.R;

@SuppressLint("ValidFragment")
public class Contact extends Fragment {
    Context context;
    View view;
    public Contact(Context context) {
        this.context=context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
            view=inflater.inflate(R.layout.contact,container,false);
            return view;
    }
}
