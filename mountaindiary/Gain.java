package com.example.mountaindiary;

public class Gain {
    private static  int globalIDgain;
    private int Id;
    private int peakId;
    private String peakStringId;
    private String dateWinter;
    private String dateSummer;
    private float coordX;
    private float coordY;
    private String uri0;
    private String uri1;
    private String uri2;
    private String uri3;
    private String uri4;
    private String memory;
    private int locationOrFoto;

    Gain(int peakId, String peakStringId, String dataWinter, String dateSummer, float coordX, float coordY, String uri0, String uri1, String uri2, String uri3, String uri4, int locationOrFoto){
        globalIDgain++;
        this.Id = globalIDgain;
        this.peakId = peakId;
        this.peakStringId = peakStringId;
        this.dateSummer = dateSummer;
        this.dateWinter = dataWinter;
        this.coordX = coordX;
        this.coordY = coordY;
        this.uri0 = uri0;
        this.uri1 = uri1;
        this.uri2 = uri2;
        this.uri3 = uri3;
        this.uri4 = uri4;
        this.memory = " ";
        this.locationOrFoto = locationOrFoto;

    }

    Gain(Gain gain){
        this.Id = gain.getId();
        this.peakId = gain.getPeakId();
        this.peakStringId = gain.getPeakStringId();
        this.dateSummer = gain.getDateSummer();
        this.dateWinter = gain.getDateWinter();
        this.coordX = gain.getCoordX();
        this.coordY = gain.getCoordY();
        this.uri0 = gain.getUri0();
        this.uri1 = gain.getUri1();
        this.uri2 = gain.getUri2();
        this.uri3 = gain.getUri3();
        this.uri4 = gain.getUri4();
        this.memory = gain.getMemory();
        this.locationOrFoto = gain.getLocationOrFoto();
    }

    public String getDateSummer() {
        return dateSummer;
    }

    public void setDateSummer(String dateSummer) {
        this.dateSummer = dateSummer;
    }

    public String getDateWinter() {
        return dateWinter;
    }

    public void setDateWinter(String dateWinter) {
        this.dateWinter = dateWinter;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public float getCoordX() {
        return coordX;
    }

    public void setCoordX(float cordX) {
        this.coordX = cordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void setCoordY(float cordY) {
        this.coordY = cordY;
    }

    public int getPeakId() {
        return peakId;
    }

    public void setPeakId(int peakId) {
        this.peakId = peakId;
    }

    public String getPeakStringId() {
        return peakStringId;
    }

    public void setPeakStringId(String peakStringId) {
        this.peakStringId = peakStringId;
    }

    public String getMemory() {
        return memory;
    }

    public void setmemory(String memory) {
        this.memory = memory;
    }

    public String getUri0() {
        return uri0;
    }

    public void setUri0(String uri0) {
        this.uri0 = uri0;
    }

    public String getUri1() {
        return uri1;
    }

    public void setUri1(String uri1) {
        this.uri1 = uri1;
    }

    public String getUri2() {
        return uri2;
    }

    public void setUri2(String uri2) {
        this.uri2 = uri2;
    }

    public String getUri3() {
        return uri3;
    }

    public void setUri3(String uri3) {
        this.uri3 = uri3;
    }

    public String getUri4() {
        return uri4;
    }

    public void setUri4(String uri4) {
        this.uri4 = uri4;
    }

    public int getLocationOrFoto() {
        return locationOrFoto;
    }

    public void setLocationOrFoto(int locationOrFoto) {
        this.locationOrFoto = locationOrFoto;
    }
}
