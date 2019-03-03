package Tabs;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;

import Adapters.PagerAdapter;
import spark.loop.callblocker.R;

@SuppressLint("ValidFragment")
public class LogContact extends DialogFragment implements View.OnClickListener {
    View view;
    TabLayout tabLayout;
    ViewPager viewPager;
    Context context;
    Button Cancel,Okay;

    public LogContact(Context context) {
    this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view=inflater.inflate(R.layout.phonestorage,container,false);
        setCancelable(false);
        tabLayout=view.findViewById(R.id.tablayoutCall);
        viewPager=view.findViewById(R.id.contactpager);
        Cancel=view.findViewById(R.id.cancels);
        Okay=view.findViewById(R.id.okay);
        Cancel.setOnClickListener(this);
        Okay.setOnClickListener(this);
        PagerAdapter pagerAdapter=new PagerAdapter(getChildFragmentManager());
        pagerAdapter.AddTab(new Contact(context),"Contact");
        pagerAdapter.AddTab(new CallLog(context),"Call Log");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancels:
                dismiss();
                break;
            case R.id.okay:
                break;

        }
    }
}
