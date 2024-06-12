package com.project.project.acommon;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.project.project.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private final String TAG = this.getClass().getSimpleName();



    private Unbinder unbinderknife;

    static String name = "";

    private int CAMERA_REQUEST = 100;

    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_navigation_activity);

        unbinderknife = ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinderknife.unbind();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        drawer.closeDrawers();

//        if(menuItem.getItemId() == R.id.nav_message){
//            Utils.showToast(getApplicationContext(), "Message clicked");
//        }

        return false;
    }
}
