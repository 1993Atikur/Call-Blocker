package Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import Data.UserData;
import Tabs.CallLogs;
import Tabs.Contact;

public class Phonebook extends FragmentStatePagerAdapter {
    Context context;
    Contact contact;
    CallLogs callLogs;
    ContentLoader loader;
    ArrayList<UserData> data;
    Cursor cursor;
    String []Title={"Contact","Call Logs"};

    public Phonebook (FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
        data = new ArrayList<>();
        loader = new ContentLoader();
        loader.execute();
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                contact = new Contact(context, data);
                return contact;
            default:
                callLogs=new CallLogs(context);
                return callLogs;


        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Title[position];
    }

    public Contact ObjectContact(){
        return contact;
    }
    public CallLogs ObjectCallLogs(){
        return callLogs;
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
