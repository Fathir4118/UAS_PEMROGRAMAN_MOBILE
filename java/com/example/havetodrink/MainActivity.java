package com.example.havetodrink;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        // Set halaman pertama yang muncul adalah HomeFragment
        loadFragment(new HomeFragment());

        // Logika Navigasi Bawah
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.menu_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.menu_jadwal) {
                // Jika ingin tetap menggunakan Activity untuk Jadwal:
                startActivity(new Intent(MainActivity.this, JadwalActivity.class));
                return true;
            } else if (id == R.id.menu_profile) {
                // Jika ingin tetap menggunakan Activity untuk Profil:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    // Fungsi untuk menukar isi halaman di tengah
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Menangkap data ml dari TambahMinumActivity dan dikirim ke HomeFragment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Teruskan data ke Fragment yang sedang aktif agar angka ml bertambah
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}