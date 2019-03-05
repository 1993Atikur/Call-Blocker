package Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import Tabs.Contact;

public class Phonebook extends FragmentStatePagerAdapter {
    Context context;
    Contact contact
    public Phonebook(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int i) {

                switch (i){
                    case 0


                }

    }

    @Override
    public int getCount() {
        return 2;
    }
}
