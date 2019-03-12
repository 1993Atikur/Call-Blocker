package Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import Tabs.BlockList;
import Tabs.History;

public class PagerAdapter extends FragmentStatePagerAdapter {

    String Title[]={"Blocked","History"};
    ArrayList<Fragment> uFragment = new ArrayList<>();
    Context context;

    public PagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;


    }


    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                BlockList blockList = new BlockList(context);
                return blockList;
            default:
                History history=new History(context);
                return history;
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

}
