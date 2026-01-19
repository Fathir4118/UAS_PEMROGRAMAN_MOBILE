package com.example.havetodrink;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class JadwalActivity extends AppCompatActivity {

    private LinearLayout containerJadwal;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        sharedPreferences = getSharedPreferences("WaterApp", Context.MODE_PRIVATE);
        containerJadwal = findViewById(R.id.containerJadwal);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        findViewById(R.id.btnTambahJadwal).setOnClickListener(v -> {
            startActivity(new Intent(this, TambahJadwalActivity.class));
        });

        // Setup navigasi dikunci agar tidak re-trigger
        setupBottomNavigation();

        // Load data pertama kali
        tampilkanDaftarJadwal();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Pastikan menu yang terpilih benar
        bottomNavigation.setSelectedItemId(R.id.menu_jadwal);
        tampilkanDaftarJadwal();
    }

    private void tampilkanDaftarJadwal() {
        containerJadwal.removeAllViews();
        // Ambil daftar jadwal
        Set<String> setJadwal = sharedPreferences.getStringSet("list_jadwal", new HashSet<>());

        for (String waktu : setJadwal) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_jadwal, containerJadwal, false);
            TextView tvWaktu = itemView.findViewById(R.id.tvWaktu);
            ToggleButton toggleStatus = itemView.findViewById(R.id.toggleStatus);

            tvWaktu.setText(waktu);

            // AMBIL STATUS: Gunakan default false jika tidak ada
            boolean isAktif = sharedPreferences.getBoolean("status_" + waktu, false);

            // KUNCI LISTENER: Agar setChecked tidak memicu event klik
            toggleStatus.setOnCheckedChangeListener(null);
            toggleStatus.setChecked(isAktif);

            // PASANG LISTENER BARU
            toggleStatus.setOnClickListener(v -> {
                boolean isChecked = toggleStatus.isChecked();

                // SIMPAN SEGERA
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("status_" + waktu, isChecked);
                editor.apply(); // Gunakan apply agar tersimpan di background

                if (isChecked) {
                    setAlarm(waktu);
                    Toast.makeText(JadwalActivity.this, "Alarm " + waktu + " Aktif", Toast.LENGTH_SHORT).show();
                } else {
                    cancelAlarm(waktu);
                    Toast.makeText(JadwalActivity.this, "Alarm Nonaktif", Toast.LENGTH_SHORT).show();
                }
            });

            // Klik lama untuk hapus
            itemView.setOnLongClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Hapus Jadwal")
                        .setMessage("Hapus jadwal " + waktu + "?")
                        .setPositiveButton("Hapus", (dialog, which) -> hapusJadwal(waktu))
                        .setNegativeButton("Batal", null)
                        .show();
                return true;
            });

            containerJadwal.addView(itemView);
        }
    }

    private void hapusJadwal(String waktu) {
        cancelAlarm(waktu);
        Set<String> setJadwal = sharedPreferences.getStringSet("list_jadwal", new HashSet<>());
        Set<String> newSet = new HashSet<>(setJadwal);
        newSet.remove(waktu);

        sharedPreferences.edit().putStringSet("list_jadwal", newSet).apply();
        sharedPreferences.edit().remove("status_" + waktu).apply();

        tampilkanDaftarJadwal();
    }

    private void setAlarm(String waktu) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("waktu", waktu);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, waktu.hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        try {
            String[] parts = waktu.split("[: ]");
            int jam = Integer.parseInt(parts[0]);
            int menit = Integer.parseInt(parts[1]);
            String amPm = parts[2];

            if (amPm.equalsIgnoreCase("PM") && jam < 12) jam += 12;
            if (amPm.equalsIgnoreCase("AM") && jam == 12) jam = 0;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, jam);
            calendar.set(Calendar.MINUTE, menit);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelAlarm(String waktu) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, waktu.hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (alarmManager != null) alarmManager.cancel(pendingIntent);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // JIKA MENGKLIK JADWAL SAAT SUDAH DI JADWAL, JANGAN LAKUKAN APAPAUN
            if (id == R.id.menu_jadwal) return false;

            if (id == R.id.menu_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.menu_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
}