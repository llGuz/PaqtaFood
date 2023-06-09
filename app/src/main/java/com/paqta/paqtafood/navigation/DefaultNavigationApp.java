package com.paqta.paqtafood.navigation;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paqta.paqtafood.R;
import com.paqta.paqtafood.ui.user.cart.CartFragment;
import com.paqta.paqtafood.ui.user.favorite.FavoriteFragment;
import com.paqta.paqtafood.ui.user.home.HomeFragment;
import com.paqta.paqtafood.ui.user.login.Login;
import com.paqta.paqtafood.ui.user.menu.MenuFragment;
import com.paqta.paqtafood.ui.user.menu_detail.MenuDetailFragment;
import com.paqta.paqtafood.ui.user.offers.OffersFragment;
import com.paqta.paqtafood.ui.components.profile.ProfileFragment;
import com.paqta.paqtafood.ui.user.search.SearchFragment;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;


public class DefaultNavigationApp extends AppCompatActivity {

    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    MaterialToolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String PUBLISH_KEY = "pk_test_51NHfg9EfcKxNGCdSWG6saQG9uV7RfXRi8pdUZfN5qwRMIDH28yfFRbhS9mwLyNxDQLhxQIeKgYsqS6ilRey8pW1R00P5Nh0ODJ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_navigation_app);
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        PaymentConfiguration.init(this, PUBLISH_KEY);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.topAppBar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        replaceFragment(new HomeFragment());

        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.userName);
        TextView email = headerView.findViewById(R.id.userEmail);

        username.setText(mUser.getDisplayName());
        email.setText(mUser.getEmail());

        toolbar.setOnMenuItemClickListener(item -> {
            int search;
            search = R.id.search;

            if (item.getItemId() == search) {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_bottom_home) {
                toolbar.setTitle("Menú");
                replaceFragment(new MenuFragment());
            } else if (item.getItemId() == R.id.nav_bottom_favorite) {
                toolbar.setTitle("Favoritos");
                replaceFragment(new FavoriteFragment());
            } else if (item.getItemId() == R.id.nav_bottom_search) {
                toolbar.setTitle("Busquemos tu Platillo");
                replaceFragment(new SearchFragment());
            } else if (item.getItemId() == R.id.nav_bottom_menu_detail) {
                toolbar.setTitle("Menú");
                replaceFragment(new MenuDetailFragment());
            } else if (item.getItemId() == R.id.nav_bottom_cart) {
                toolbar.setTitle("Carrito");
                replaceFragment(new CartFragment());
            }

            return true;
        });


        navigationView.setNavigationItemSelectedListener(item -> {
            int offers, combos, perfil, cart, exit, qr;
            offers = R.id.nav_sidebar_offers;
            combos = R.id.nav_sidebar_combos;
            perfil = R.id.nav_sidebar_profile;
            cart = R.id.nav_sidebar_cart;
            exit = R.id.nav_sidebar_exit;
            qr = R.id.nav_sidebar_qr;

            if (item.getItemId() == offers) {
                replaceFragment(new OffersFragment());
            } else if (item.getItemId() == combos) {
                Toast.makeText(this, "Combos", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == perfil) {
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId() == cart) {
                replaceFragment(new CartFragment());
            } else if (item.getItemId() == exit) {
                mAuth.signOut();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                Toast.makeText(this, "Cerrar sesion", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == qr) {
                replaceFragment(new MenuFragment());
            }

            return true;
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Metodo que se encargara de remplazar el layout del contenido por el que nosotros asignemos
     *
     * @param fragment, contenido
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}