package com.semmache.danyl.withingshy.model;

/**
 * Created by Danyl on 29/08/16.
 */
public class UserReport {
    private String lastUpdate;
    private int dia;
    private int diaType;
    private int sys;
    private int sysType;
    private int bpm;
    private int bpmType;
    private String timeZone;
    private String result;

    public UserReport(String lastUpdate, int dia, int diaType, int sys, int sysType, int bpm, int bpmType, String timeZone, String result) {
        this.lastUpdate = lastUpdate;
        this.dia = dia;
        this.diaType = diaType;
        this.sys = sys;
        this.sysType = sysType;
        this.bpm = bpm;
        this.bpmType = bpmType;
        this.timeZone = timeZone;
        this.result = result;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public int getDia() {
        return dia;
    }

    public int getDiaType() {
        return diaType;
    }

    public int getSys() {
        return sys;
    }

    public int getSysType() {
        return sysType;
    }

    public int getBpm() {
        return bpm;
    }

    public int getBpmType() {
        return bpmType;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String result() {
        return result;
    }
}
