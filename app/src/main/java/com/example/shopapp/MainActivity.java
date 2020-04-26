package com.example.shopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.shopapp.db.Client;
import com.example.shopapp.db.ShopAppDb;
import com.example.shopapp.ui.CartFragment;
import com.example.shopapp.ui.ClientsFragment;
import com.example.shopapp.ui.ProductsFragment;
import com.example.shopapp.ui.SalesFragment;
import com.example.shopapp.ui.ShopFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    public static FragmentManager fragmentManager;
    public static ShopAppDb shoppingAppDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Δημιουργία του fragmentManger
        fragmentManager = getSupportFragmentManager();
        //Φόρτωση της διεπαφής
        setContentView(R.layout.activity_main);
        //Δημιουργία και φόρτωση του toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Δημιουργία και φόρτωση του navigation
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Δημιουργία και φόρτωση του drawer με toggle button στο toolbar
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //Δημιουργία της ΒΔ
        shoppingAppDb = Room.databaseBuilder(getApplicationContext(), ShopAppDb.class, "shopDB").
                allowMainThreadQueries().build();
        //Μόνο για την πρώτη φορά, δημιουργία "ανώνυμου πελάτη"
        if(shoppingAppDb.shopAppDao().getClients().size()==0) {
            Client client = new Client();
            shoppingAppDb.shopAppDao().addClient(client);
        }
        /*Κατά το άνοιγμα της εφαρμογής, μετάβαση στο ShopFragment
        και σήμανση ως επιλεγμένης της αντίστοιχης επίλογης του μενού*/
        if(savedInstanceState==null) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container,
                    new ShopFragment()).addToBackStack(null).commit();
            navigationView.setCheckedItem(R.id.nav_shop);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_products:
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new ProductsFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_shop:
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new ShopFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_basket:
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new CartFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_clients:
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new ClientsFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_sales:
                fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        new SalesFragment()).addToBackStack(null).commit();
                break;
        }
        //Κλείσιμο του μενού
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //Αν πατηθεί το κουμπί επιστροφής, κλείσε το drawer αν είναι ανοιχτό
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
