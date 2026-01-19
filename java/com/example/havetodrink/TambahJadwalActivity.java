package com.example.havetodrink;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Set;

public class TambahJadwalActivity extends AppCompatActivity {

    private EditText inputJam;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_jadwal);

        sharedPreferences = getSharedPreferences("WaterApp", Context.MODE_PRIVATE);
        inputJam = findViewById(R.id.inputJam);

        // Tombol Cepat
        findViewById(R.id.btn07am).setOnClickListener(v -> inputJam.setText("07:00 AM"));
        findViewById(R.id.btn12am).setOnClickListener(v -> inputJam.setText("12:00 PM"));
        findViewById(R.id.btn05pm).setOnClickListener(v -> inputJam.setText("05:00 PM"));
        findViewById(R.id.btn09pm).setOnClickListener(v -> inputJam.setText("09:00 PM"));

        findViewById(R.id.btnSimpanJadwal).setOnClickListener(v -> {
            String waktu = inputJam.getText().toString();
            if (!waktu.isEmpty()) {
                simpanJadwal(waktu);
                finish();
            } else {
                Toast.makeText(this, "Pilih jam dulu!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnBackJadwal).setOnClickListener(v -> finish());
    }

    private void simpanJadwal(String waktu) {
        // Ambil daftar jadwal lama (jika ada)
        Set<String> setJadwal = sharedPreferences.getStringSet("list_jadwal", new HashSet<>());

        // Buat copy baru agar bisa dimodifikasi
        Set<String> newSetJadwal = new HashSet<>(setJadwal);
        newSetJadwal.add(waktu);

        // Simpan kembali
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("list_jadwal", newSetJadwal);
        editor.apply();

        Toast.makeText(this, "Jadwal " + waktu + " Ditambahkan", Toast.LENGTH_SHORT).show();
    }
}