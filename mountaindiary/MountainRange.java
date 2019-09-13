package com.example.mountaindiary;

class MountainRange {
    private static  int globalID;
    private int Id;
    private String title;
    private int noPeaks;
    private int noPeaksAcquired;

    MountainRange(String title, int noPeaks, int noPeaksAcquired) {
        globalID ++;
        Id = globalID-1;
        this.title = title;
        this.noPeaks = noPeaks;
        this.noPeaksAcquired = noPeaksAcquired;
    }

    public int getNoPeaksAcquired() {
        return noPeaksAcquired;
    }

    public void setNoPeaksAcquired(int noPeaksAcquired) {
        this.noPeaksAcquired = noPeaksAcquired;
    }

    public int getNoPeaks() {
        return noPeaks;
    }

    public void setNoPeaks(int noPeaks) {
        this.noPeaks = noPeaks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
