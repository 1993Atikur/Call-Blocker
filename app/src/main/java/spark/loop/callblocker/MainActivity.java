package spark.loop.callblocker;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import Adapters.PagerAdapter;
import Tabs.BlockList;
import Tabs.History;

public class MainActivity extends AppCompatActivity {
    Switch switchButton;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchButton=findViewById(R.id.switchbutton);
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewPager);

        PagerAdapter pagerAdapter=new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddTab(new BlockList(MainActivity.this),"Blocked");
        pagerAdapter.AddTab(new History(MainActivity.this),"History");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.block);
        tabLayout.getTabAt(1).setIcon(R.drawable.history);





      //  startService(new Intent(MainActivity.this,BlockerService.class));
        switchButton.setChecked(false);
        switchButton.setText("Disabled");
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    buttonView.setText("Enabled");

                }else {
                    buttonView.setText("Disabled");

                }
            }
        });

    }
}
