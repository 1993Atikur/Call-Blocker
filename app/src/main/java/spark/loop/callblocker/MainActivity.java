package spark.loop.callblocker;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import Adapters.PagerAdapter;
import Database.Holder;
import Tabs.BlockList;
import Tabs.History;

public class MainActivity extends AppCompatActivity  {
    Switch switchButton;
    TabLayout tabLayout;
    ViewPager viewPager;
    Button Information;
    Intent intent;
    Holder holder;
    Cursor cursor;
    int var=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        holder=new Holder(this);
        cursor=holder.getRunState();
        while (cursor.moveToNext()){
            var=cursor.getInt(0);
        }


        setContentView(R.layout.activity_main);
        switchButton=findViewById(R.id.switchbutton);
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewPager);
        Information=findViewById(R.id.informaion);

        PagerAdapter pagerAdapter=new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddTab(new BlockList(MainActivity.this),"Blocked");
        pagerAdapter.AddTab(new History(MainActivity.this),"History");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.block);
        tabLayout.getTabAt(1).setIcon(R.drawable.history);


        intent=new Intent(MainActivity.this,BlockerService.class);


        if (var==0){
            switchButton.setChecked(false);
            switchButton.setText("Disabled");
        }else if(var==1)
        {
            switchButton.setChecked(true);
            switchButton.setText("Enabled ");
        }else{

            holder.initials();
        }


        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    buttonView.setText("Enabled ");
                    holder.updateRunState(1);
                    startService(intent);


                }else {
                    holder.updateRunState(0);
                    buttonView.setText("Disabled");
                    stopService(intent);
                }
            }
        });


        Information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Processing", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
