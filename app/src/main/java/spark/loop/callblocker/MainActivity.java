package spark.loop.callblocker;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import Adapters.PagerAdapter;
import Data.Permission;
import Database.State;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity implements Permission, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    Switch switchButton;
    TabLayout tabLayout;
    ViewPager viewPager;
    Button Information;
    Intent intent;
    State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = new State(MainActivity.this);
        setContentView(R.layout.activity_main);
        switchButton = findViewById(R.id.switchbutton);
        switchButton.setOnCheckedChangeListener(this);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        Information = findViewById(R.id.informaion);
        Information.setOnClickListener(this);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.block);
        tabLayout.getTabAt(1).setIcon(R.drawable.history);
        intent = new Intent(MainActivity.this, BlockerService.class);
        if (state.isRunning()) {
            switchButton.setChecked(true);
            switchButton.setText("Enabled ");
            startService(intent);
        } else {
            switchButton.setChecked(false);
            switchButton.setText("Disabled");
        }

    }


    @Override
    public boolean hasPermission1() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
    }


    @Override
    public boolean hasPermission2() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (hasPermission1() & hasPermission2()) {
                Enable();

            } else if (!hasPermission1() & hasPermission2()) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_PHONE_STATE}, 1);
            } else if (hasPermission1() & !hasPermission2()) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{CALL_PHONE}, 2);
            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_PHONE_STATE, CALL_PHONE}, 3);
            }

        } else {
            switchButton.setChecked(false);
            switchButton.setText("Disabled");
            state.updateRunState(0);
            stopService(intent);

        }
    }


    @Override
    public void onClick(View v) {
    }

    public void Enable() {

        switchButton.setChecked(true);
        switchButton.setText("Enabled ");
        startService(intent);
        state.updateRunState(1);
    }

    public void Disable() {
        switchButton.setChecked(false);
        switchButton.setText("Disabled");
        state.updateRunState(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Enable();
                else
                    Disable();
                break;
            case 2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Enable();
                else
                    Disable();
                break;
            case 3:
                boolean Phonestate = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean Callstate = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (Phonestate && Callstate) {
                    Enable();
                } else {
                    Disable();
                }
                break;
        }
    }
}
