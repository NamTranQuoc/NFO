package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.UserDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;

public class Navigate extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView tvUsername;
    ImageButton btnMenu, btnCart;
    ImageView avatar;
    UserDTO user;
    Menu menu;
    EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mapping
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navView);
        View headerView = navigationView.getHeaderView(0);
        tvUsername = (TextView) headerView.findViewById(R.id.lbUserName);
        btnMenu = (ImageButton) toolbar.findViewById(R.id.btnMenu);
        avatar = (ImageView) headerView.findViewById(R.id.imgAvatar);
        btnCart = (ImageButton) findViewById(R.id.btnCart);
        txtSearch = (EditText) findViewById(R.id.txtSearch);

        //load data
        tvUsername.setText("ADMIN");
        avatar.setImageBitmap(Base64Utils.stringToBitmap(""));

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();

        //even
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        txtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    protected void search() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        intent.putExtra("keyword", txtSearch.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            user = UserDAO.getInstance().getSharedPreferences(Navigate.this);
            tvUsername.setText(user.getName());
            avatar.setImageBitmap(Base64Utils.stringToBitmap(user.getAvatar()));
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.navInformation:
                intent = new Intent(getApplicationContext(), InformationActivity.class);
                break;
            case R.id.navChangePassword:
                intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                break;
            case R.id.Logout:
                SharedPreferences sharedPreferences = getSharedPreferences("user", SignInActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                intent = new Intent(getApplicationContext(), SignInActivity.class);
                break;
            case R.id.navHome:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                break;
            case R.id.navHistoryCart:
                intent = new Intent(getApplicationContext(), HistoryBuyActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}