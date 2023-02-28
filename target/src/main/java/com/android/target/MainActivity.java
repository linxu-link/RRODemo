package com.android.target;

import android.content.Context;
import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.target.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private OverlayInfo overlayInfo;
    private IOverlayManager overlayManager;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("TAG", "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "onResume: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Action Done!!", Toast.LENGTH_SHORT).show();


                try {
                    Log.e("TAG", "onClick: " + overlayInfo);
                    int userId = UserHandle.myUserId();
                    Log.e("TAG", "onClick: " + overlayInfo.isEnabled());
                    if (overlayInfo.isEnabled()){
                        overlayManager.setEnabled(overlayInfo.packageName, false, userId);
                    }else {
                        overlayManager.setEnabledExclusive(overlayInfo.packageName, true, userId);
                    }

                } catch (Exception e) {
                    Log.e("TAG", "onClick: " + e.toString());
                }
            }
        });

        try {
            overlayManager = IOverlayManager.Stub.asInterface(ServiceManager.getService(Context.OVERLAY_SERVICE));
            int userId = UserHandle.myUserId();
            Map<String, List<OverlayInfo>> allOverlays = overlayManager.getAllOverlays(userId);
            for (String key : allOverlays.keySet()) {
                Log.e("TAG1", key + "---" + allOverlays.get(key));
                overlayInfo = allOverlays.get("com.android.target").get(0);
            }
        } catch (Exception exception) {
            Log.e("TAG", "onCreate: "+exception );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}