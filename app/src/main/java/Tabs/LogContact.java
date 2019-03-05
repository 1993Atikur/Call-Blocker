package Tabs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import Adapters.CustomList;
import Adapters.PagerAdapter;
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
    ContentLoader loader;
    ArrayList<UserData> data;
    Cursor cursor;
    ArrayList<UserData> items;
    CustomList customList;
    Holder holder;
    UserData user;
    Contact contact;
    CallLogs callLogs;
    ProgressDialog progressDialog;
    public LogContact(Context context, ArrayList<UserData> items, CustomList customList, Holder holder) {
        this.context = context;
        this.customList = customList;
        this.items = items;
        this.holder = holder;
        cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
        data = new ArrayList<>();
        loader = new ContentLoader();
        loader.execute();

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
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        pagerAdapter.AddTab(contact = new Contact(context, data), "Contact");
        pagerAdapter.AddTab(callLogs=new CallLogs(context), "Call Log");
        viewPager.setAdapter(pagerAdapter);
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
                contact.getSelectedNames(this);
                callLogs.getSelectedNames(this);
                customList.notifyDataSetChanged();
                dismiss();
                break;

        }
    }

    @Override
    public void Selected(String name, String number) {
        UserData userData=new UserData(name,number);
        items.add(userData);
        holder.UserData(userData);
    }


    private class ContentLoader extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            while (cursor.moveToNext() ) {
                data.add(new UserData(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
            }
            return null;
        }
    }


}
