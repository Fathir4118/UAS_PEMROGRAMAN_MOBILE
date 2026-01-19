package com.example.havetodrink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private EditText editUserName;
    private ProgressBar progressHarian, progressBulanan;
    private TextView tvDetailHarian, tvDetailBulanan;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("WaterApp", Context.MODE_PRIVATE);

        editUserName = findViewById(R.id.editUserName);
        progressHarian = findViewById(R.id.progressHarian);
        tvDetailHarian = findViewById(R.id.tvDetailHarian);
        progressBulanan = findViewById(R.id.progressBulanan);
        tvDetailBulanan = findViewById(R.id.tvDetailBulanan);
        Button btnSimpan = findViewById(R.id.btnSimpanProfil);

        // Muat Data Tersimpan
        String savedName = sharedPreferences.getString("user_name", "User");
        int currentWater = sharedPreferences.getInt("current_water", 0);
        int monthlyWater = sharedPreferences.getInt("monthly_water", 0);

        // Tampilkan ke UI
        editUserName.setText(savedName);
        progressHarian.setProgress(currentWater);
        tvDetailHarian.setText(currentWater + " / 2000 ml");

        progressBulanan.setProgress(monthlyWater);
        tvDetailBulanan.setText(monthlyWater + " / 10000 ml");

        btnSimpan.setOnClickListener(v -> {
            String newName = editUserName.getText().toString();
            if(!newName.isEmpty()){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_name", newName);
                editor.apply();
                Toast.makeText(this, "Nama disimpan!", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigasi Bawah
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setSelectedItemId(R.id.menu_profile);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish(); return true;
            } else if (id == R.id.menu_jadwal) {
                startActivity(new Intent(ProfileActivity.this, JadwalActivity.class));
                finish(); return true;
            }
            return true;
        });
    }
}