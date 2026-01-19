package com.example.havetodrink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TambahMinumActivity extends AppCompatActivity {

    private EditText inputMl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_minum);

        inputMl = findViewById(R.id.inputMl);
        TextView btnBack = findViewById(R.id.btnBack);
        Button btnSimpan = findViewById(R.id.btnSimpan);

        // Logika tombol kembali
        btnBack.setOnClickListener(v -> finish());

        // Logika tombol cepat (Quick Pick)
        findViewById(R.id.btn100).setOnClickListener(v -> inputMl.setText("100"));
        findViewById(R.id.btn300).setOnClickListener(v -> inputMl.setText("300"));
        findViewById(R.id.btn500).setOnClickListener(v -> inputMl.setText("500"));

        // Logika Simpan
        btnSimpan.setOnClickListener(v -> {
            String value = inputMl.getText().toString();
            if (!value.isEmpty()) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("jumlah_minum", Integer.parseInt(value));
                setResult(Activity.RESULT_OK, returnIntent);
                finish(); // Kembali ke MainActivity
            }
        });
    }
}