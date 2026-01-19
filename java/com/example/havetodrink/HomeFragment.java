package com.example.havetodrink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private TextView tvProgress;
    private int currentWater = 0;
    private int monthlyWater = 0; // Tambahkan variabel bulanan
    private final int targetWater = 2000;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (getActivity() != null) {
            sharedPreferences = getActivity().getSharedPreferences("WaterApp", Context.MODE_PRIVATE);
            currentWater = sharedPreferences.getInt("current_water", 0);
            monthlyWater = sharedPreferences.getInt("monthly_water", 0); // Ambil data bulanan
        }

        tvProgress = view.findViewById(R.id.tv_progress_text);
        Button btnTambah = view.findViewById(R.id.btn_tambah_minum_di_gelas);

        if (btnTambah != null) {
            btnTambah.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), TambahMinumActivity.class);
                startActivityForResult(intent, 100);
            });
        }

        updateUI();
        return view;
    }

    private void updateUI() {
        if (tvProgress != null) {
            tvProgress.setText("Hari Ini : " + currentWater + " / " + targetWater + " ml");
        }

        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("current_water", currentWater);
            editor.putInt("monthly_water", monthlyWater); // Simpan data bulanan
            editor.apply();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            int tambahan = data.getIntExtra("jumlah_minum", 0);
            currentWater += tambahan;
            monthlyWater += tambahan; // Tambahkan juga ke total bulanan
            updateUI();
        }
    }
}