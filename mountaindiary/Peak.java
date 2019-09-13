package com.example.mountaindiary;

import java.util.Comparator;

class Peak {
    private static int globalIDPeak;
    private int Id;
    private int rangeId;
    private String title;
    private String chain;
    private String range;
    private float xCord;
    private float yCord;
    private int height;
    private String description;
    private int gainId;

    Peak(int rangeId, String title, String chain, String range, int  height, String description, float xCord, float yCord) {
        globalIDPeak++;
        Id = globalIDPeak;
        this.rangeId = rangeId;
        this.title = title;
        this.range = range;
        this.chain = chain;
        this.height = height;
        this.description = description;
        this.xCord = xCord;
        this.yCord = yCord;
        this.gainId = -1;
    }

    Peak(Peak peak){
        globalIDPeak++;
        Id = globalIDPeak;
        this.rangeId = peak.rangeId;
        this.title = peak.title;
        this.range = peak.range;
        this.chain = peak.chain;
        this.height = peak.height;
        this.description = peak.description;
        this.xCord = peak.xCord;
        this.yCord = peak.yCord;
        this.gainId = peak.gainId;
    }

    public float getyCord() {
        return yCord;
    }

    public void setyCord(float yCord) {
        this.yCord = yCord;
    }

    public float getxCord() {
        return xCord;
    }

    public void setxCord(float xCord) {
        this.xCord = xCord;
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

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGainId() {
        return gainId;
    }

    public void setGainId(int gainId){
        this.gainId = gainId;
    }

    public static Comparator<Peak> gainIdComparator = new Comparator<Peak>() {

        public int compare(Peak s1, Peak s2) {
            int g1 = s1.getRangeId();
            int g2 = s2.getRangeId();

            return g1-g2;
        }};

    public static Comparator<Peak> heightdComparator = new Comparator<Peak>() {

        public int compare(Peak s1, Peak s2) {

            int rollno1 = s1.getHeight();
            int rollno2 = s2.getHeight();

            return rollno1-rollno2;
        }};

    public static Comparator<Peak> gainIdANDheightdComparator = new Comparator<Peak>() {

        public int compare(Peak p1, Peak p2) {

            String s1 = String.valueOf(p1.getRangeId())+" "+String.valueOf(p1.getHeight());
            String s2 = String.valueOf(p2.getRangeId())+" "+String.valueOf(p2.getHeight());

            return s1.compareTo(s2);
        }};
}
