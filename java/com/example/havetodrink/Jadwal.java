package com.example.havetodrink;

public class Jadwal {
    private String waktu;
    private boolean isStatus;

    public Jadwal(String waktu, boolean isStatus) {
        this.waktu = waktu;
        this.isStatus = isStatus;
    }

    public String getWaktu() { return waktu; }
    public boolean isStatus() { return isStatus; }
    public void setStatus(boolean status) { isStatus = status; }
}