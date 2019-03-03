package spark.loop.callblocker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import Adapters.PagerAdapter;
import Database.State;
import Tabs.BlockList;
import Tabs.History;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity implements Permission {
    Switch switchButton;
    TabLayout tabLayout;
    ViewPager viewPager;
    Button Information;
    Intent intent;
    State state;
    Cursor cursor;
    int var = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = new State(MainActivity.this);
        var = value();
        BackGroundTask task = new BackGroundTask();
        setContentView(R.layout.activity_main);
        switchButton = findViewById(R.id.switchbutton);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        Information = findViewById(R.id.informaion);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddTab(new BlockList(MainActivity.this), "Blocked");
        pagerAdapter.AddTab(new History(MainActivity.this), "History");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.block);
        tabLayout.getTabAt(1).setIcon(R.drawable.history);


        intent = new Intent(MainActivity.this, BlockerService.class);


        if (var == 0) {
            switchButton.setChecked(false);
            switchButton.setText("Disabled");
        } else if (var == 1) {
            switchButton.setChecked(true);
            switchButton.setText("Enabled ");
        } else {
            task.execute();
        }


        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    if (hasPermission()) {
                        buttonView.setText("Enabled ");
                        state.updateRunState(1);
                        startService(intent);
                    } else {
                        //Request For Permission
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_PHONE_STATE, CALL_PHONE}, 1);

                        buttonView.setText("Disabled");
                        buttonView.setChecked(false);
                    }

                } else {
                    state.updateRunState(0);
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

    private int value() {
        cursor = state.getRunState();
        int v = -1;
        while (cursor.moveToNext()) {
            v = cursor.getInt(0);
        }
        return v;
    }

    @Override
    public boolean hasPermission() {
        int read_phone_state = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int call_phone = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        return ((read_phone_state == PackageManager.PERMISSION_GRANTED) && (call_phone == PackageManager.PERMISSION_GRANTED));
    }

    public class BackGroundTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            state.initials();

            return null;
        }
    }



}
