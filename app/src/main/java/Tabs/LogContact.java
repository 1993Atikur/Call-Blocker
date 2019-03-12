package Tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;

import Adapters.CustomList;
import Adapters.Phonebook;
import Data.SelectedListener;
import Data.UserData;
import Database.Holder;
import spark.loop.callblocker.R;

@SuppressLint("ValidFragment")
public class LogContact extends DialogFragment implements View.OnClickListener, SelectedListener {
    View view;
    TabLayout tabLayout;
    ViewPager viewPager;
    Context context;
    Button Cancel, Okay;
    ArrayList<UserData> items;
    CustomList customList;
    Holder holder;
    Phonebook phonebook;

    public LogContact(Context context, ArrayList<UserData> items, CustomList customList, Holder holder) {
        this.context = context;
        this.customList = customList;
        this.items = items;
        this.holder = holder;


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.phonestorage, container, false);
        tabLayout = view.findViewById(R.id.tablayoutCall);
        viewPager = view.findViewById(R.id.contactpager);
        Cancel = view.findViewById(R.id.cancels);
        Okay = view.findViewById(R.id.okay);
        phonebook = new Phonebook(getChildFragmentManager(), context);
        viewPager.setAdapter(phonebook);
        tabLayout.setupWithViewPager(viewPager);
        Cancel.setOnClickListener(this);
        Okay.setOnClickListener(this);
        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancels:
                dismiss();
                break;

            case R.id.okay:
                phonebook.ObjectContact().getSelectedNames(this);
                phonebook.ObjectCallLogs().getSelectedNames(this);
                customList.notifyDataSetChanged();
                dismiss();
                break;

        }
    }

    @Override
    public void Selected(String name, String number) {
        UserData userData = new UserData(name, number.replaceAll("[()\\s-]", ""));
        items.add(userData);
        holder.UserData(userData);
        customList.notifyDataSetChanged();
    }


}
