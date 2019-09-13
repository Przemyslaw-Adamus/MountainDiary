package com.example.mountaindiary;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;


public class GlobalData {
    //------------------------------------------------------------------------------------------------------
    private static ArrayList<MountainRange> mountainRangeList;
    private static ArrayList<Peak> peaksList;
    private static ArrayList<Gain> gainsList;
    private static Context context;
    private static PeaksDBAdapter peaksDBAdapter;
    private static GainsDBAdapter gainsDBAdapter;
    private static PersonalDataDBAdapter personalDataDBAdapter;
    private static String name;
    private static String surname;
    private static String numberSOS;

    //------------------------------------------------------------------------------------------------------
    GlobalData(Context context) {
        this.context = context;
    }

    GlobalData() {
    }

    //------------------------------------------------------------------------------------------------------

    public void create(Context context) {
        mountainRangeList = new ArrayList();
        peaksList = new ArrayList();
        gainsList = new ArrayList();
        peaksDBAdapter = new PeaksDBAdapter(context);
        gainsDBAdapter = new GainsDBAdapter(context);
        personalDataDBAdapter = new PersonalDataDBAdapter(context);
        peaksList.clear();
        gainsList.clear();
        mountainRangeList.clear();
        initPeaks();
        initLoadPeaks(context);
        initOtherPeaks();
        LoadGains(context);
        initRange();
        initPersonalData();
    }

    //_____________________________ SETS & GETS____________________________

    public void setPersonalData(String name, String surname, String number) {
        this.name = name;
        this.surname = surname;
        this.numberSOS = number;
        personalDataDBAdapter.open();
        personalDataDBAdapter.insertEntry(name, surname, number);
        personalDataDBAdapter.close();
    }

    public ArrayList<MountainRange> getMountainRangeList() {
        return mountainRangeList;
    }

    private void setMountainRangeList(ArrayList<MountainRange> mountainRangeList) {
        this.mountainRangeList = mountainRangeList;
    }

    public ArrayList<Gain> getGainsList() {
        return gainsList;
    }

    private void setGainsList(ArrayList<Gain> gainsList) {
        this.gainsList = gainsList;
    }

    public ArrayList<Peak> getPeaksList() {
        return peaksList;
    }

    private void setPeaksList(ArrayList<Peak> peaksList) {
        this.peaksList = peaksList;
    }

    public static PeaksDBAdapter getPeaksDBAdapter() {
        return peaksDBAdapter;
    }

    public static void setPeaksDBAdapter(PeaksDBAdapter peaksDBAdapter) {
        GlobalData.peaksDBAdapter = peaksDBAdapter;
    }

    public static String getDignity() {
        return name + " " + surname;
    }

    public static String getSurname() {
        return surname;
    }

    public static void setSurname(String surname) {
        GlobalData.surname = surname;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        GlobalData.name = name;
    }

    public static String getNumberSOS() {
        return numberSOS;
    }

    public static void setNumberSOS(String numberSOS) {
        GlobalData.numberSOS = numberSOS;
    }

    // ____________________________ ADD & DELETE____________________________

    public void addMountainRange(MountainRange mountainRangeList) {
        this.mountainRangeList.add(mountainRangeList);
    }

    private void deleteMountainRange(MountainRange mountainRangeList) {
        this.mountainRangeList.remove(mountainRangeList);
    }

    public void addPeak(Peak peak) {
        peaksDBAdapter.open();
        peaksDBAdapter.insertEntry(peak.getRangeId(), peak.getTitle(), peak.getRange(), peak.getChain(), peak.getHeight(), peak.getDescription(), peak.getxCord(), peak.getyCord(), peak.getGainId());
        peaksDBAdapter.close();
        peak.setId(peaksList.size() + 1);
        peaksList.add(peak);
        updateRange();
    }

    public void addGain(Gain gain) {
        gainsDBAdapter.open();
        gainsDBAdapter.insertEntry(gain.getPeakId(), gain.getPeakStringId(), gain.getDateWinter(), gain.getDateSummer(), gain.getCoordX(), gain.getCoordY(), gain.getUri0().toString(), gain.getUri1().toString(), gain.getUri2().toString(), gain.getUri3().toString(), gain.getUri4().toString(), gain.getMemory(), gain.getLocationOrFoto());
        gainsDBAdapter.close();
        gainsList.add(gain);
        getThemAll(gain.getPeakStringId(), gain.getId());
        updateRange();
    }

    public void deletePeak(Peak peak) {
        this.peaksList.remove(peak);
    }

    public void updateGain(int index, String image0, String image1, String image2, String image3, String image4, String memory) {
        gainsDBAdapter.open();
        gainsDBAdapter.updateEntry(Integer.toString(index), image0, image1, image2, image3, image4, memory);
        gainsDBAdapter.close();

        gainsList.get(index - 1).setmemory(memory);
        gainsList.get(index - 1).setUri0(image0);
        gainsList.get(index - 1).setUri1(image1);
        gainsList.get(index - 1).setUri2(image2);
        gainsList.get(index - 1).setUri3(image3);
        gainsList.get(index - 1).setUri4(image4);
    }

    public void deleteGain(int position) {
        gainsDBAdapter.open();
        int id = gainsList.get(position).getId();
        gainsDBAdapter.deleteEntry(Integer.toString(id));
        gainsDBAdapter.close();
        gainsList.remove(position);
    }

    private void updateRange() {
        for (int i = 0; i < mountainRangeList.size(); i++) {
            mountainRangeList.get(i).setNoPeaks(getQuantity(i));
            mountainRangeList.get(i).setNoPeaksAcquired(lootedQuantity(i));
        }
    }

    // ____________________________ INIT____________________________

    private void initRange() {

        addMountainRange(new MountainRange("Tatry Wysokie", getQuantity(0), lootedQuantity(0)));
        addMountainRange(new MountainRange("Tatry Zachodnie", getQuantity(1), lootedQuantity(1)));
        addMountainRange(new MountainRange("Wielka Korona Tatr", getQuantity(2), lootedQuantity(2)));
        addMountainRange(new MountainRange("Turystyczna Korona Tatr", getQuantity(3), lootedQuantity(3)));
        addMountainRange(new MountainRange("Korona Polski", getQuantity(4), lootedQuantity(4)));
        addMountainRange(new MountainRange("Korona Polski - suplement", getQuantity(5), lootedQuantity(5)));
        addMountainRange(new MountainRange("Druga Korona Polski", getQuantity(6), lootedQuantity(6)));
        addMountainRange(new MountainRange("Orla Perć", getQuantity(7), lootedQuantity(7)));
        addMountainRange(new MountainRange("Korona Bieszczad", getQuantity(8), lootedQuantity(8)));
        addMountainRange(new MountainRange("Korona Sudet", getQuantity(9), lootedQuantity(9)));
        addMountainRange(new MountainRange("Górskie Jeziora i Wodospady", getQuantity(10), lootedQuantity(10)));
        addMountainRange(new MountainRange("Korona Ziemi Wojnickiej", getQuantity(11), lootedQuantity(11)));
        addMountainRange(new MountainRange("Karpaty - wszystkie", getQuantity(12), lootedQuantity(12)));
        addMountainRange(new MountainRange("Sudety - wszystkie", getQuantity(13), lootedQuantity(13)));
        addMountainRange(new MountainRange("Góry Świętokrzyskie - wszystkie", getQuantity(14), lootedQuantity(14)));
        addMountainRange(new MountainRange("Inne", getQuantity(15), lootedQuantity(15)));
    }

    private void initPeaks() {

        //Korona Polski
        this.peaksList.add(new Peak(4, "Łysica", "Góry Świętokrzyskie", "Łysogóry", 612, context.getResources().getString(R.string.desc), 50.891617f, 20.896820f));
        this.peaksList.add(new Peak(4, "Ślęża", "Sudety", "Masyw Ślęży", 718, context.getResources().getString(R.string.desc), 50.865190f, 16.707720f));
        this.peaksList.add(new Peak(4, "Skopiec", "Sudety", "Góry Kaczawskie", 724, context.getResources().getString(R.string.desc), 50.944581f, 15.884970f));
        this.peaksList.add(new Peak(4, "Kłodzka Góra", "Sudety", "Góry Bardzkie", 765, context.getResources().getString(R.string.desc), 50.431377f, 16.764933f));
        this.peaksList.add(new Peak(4, "Chełmiec", "Sudety", "Góry Wałbrzyskie", 869, context.getResources().getString(R.string.desc), 50.779619f, 16.210728f));
        this.peaksList.add(new Peak(4, "Biskupia Kopa", "Sudety", "Góry Opawskie", 889, context.getResources().getString(R.string.desc), 50.256744f, 17.428822f));
        this.peaksList.add(new Peak(4, "Lubomir", "Karpaty", "Beskid Makowski", 912, context.getResources().getString(R.string.desc), 49.766849f, 20.059372f));
        this.peaksList.add(new Peak(4, "Szczeliniec Wielki", "Sudety", "Góry Stołowe", 919, context.getResources().getString(R.string.desc), 50.485439f, 16.339304f));
        this.peaksList.add(new Peak(4, "Czupel", "Karpaty", "Beskid Mały", 934, context.getResources().getString(R.string.desc), 49.767888f, 19.160978f));
        this.peaksList.add(new Peak(4, "Waligóra", "Sudety", "Góry Kamienne", 936, context.getResources().getString(R.string.desc), 50.680848f, 16.278117f));
        this.peaksList.add(new Peak(4, "Skalnik", "Sudety", "Rudawy Janowickie", 945, context.getResources().getString(R.string.desc), 50.808352f, 15.900458f));
        this.peaksList.add(new Peak(4, "Jagodna", "Sudety", "Góry Bystrzyckie", 977, context.getResources().getString(R.string.desc), 50.252403f, 16.564819f));
        this.peaksList.add(new Peak(4, "Kowadło", "Sudety", "Góry Złote", 989, context.getResources().getString(R.string.desc), 50.264395f, 17.013273f));
        this.peaksList.add(new Peak(4, "Lackowa", "Karpaty", "Beskid Niski", 997, context.getResources().getString(R.string.desc), 49.426486f, 21.102560f));
        this.peaksList.add(new Peak(4, "Wielka Sowa", "Sudety", "Góry Sowie", 1015, context.getResources().getString(R.string.desc), 50.680185f, 16.485500f));
        this.peaksList.add(new Peak(4, "Wysoka", "Karpaty", "Pieniny", 1050, context.getResources().getString(R.string.desc), 49.380368f, 20.555393f));
        this.peaksList.add(new Peak(4, "Orlica", "Sudety", "Góry Orlickie", 1084, context.getResources().getString(R.string.desc), 50.353245f, 16.360713f));
        this.peaksList.add(new Peak(4, "Rudawiec", "Sudety", "Góry Bialskie", 1112, context.getResources().getString(R.string.desc), 50.244821f, 16.976495f));
        this.peaksList.add(new Peak(4, "Wysoka Kopa", "Sudety", "Góry Izerskie", 1126, context.getResources().getString(R.string.desc), 50.8499981f, 15.4019348f));
        this.peaksList.add(new Peak(4, "Mogielica", "Karpaty", "Beskid Wyspowy", 1170, context.getResources().getString(R.string.desc), 49.655231f, 20.276813f));
        this.peaksList.add(new Peak(4, "Skrzyczne", "Karpaty", "Beskid Śląski", 1257, context.getResources().getString(R.string.desc), 49.684301f, 19.031802f));
        this.peaksList.add(new Peak(4, "Radziejowa", "Karpaty", "Beskid Sądecki", 1262, context.getResources().getString(R.string.desc), 49.449222f, 20.604204f));
        this.peaksList.add(new Peak(4, "Turbacz", "Karpaty", "Gorce", 1310, context.getResources().getString(R.string.desc), 49.542911f, 20.111386f));
        this.peaksList.add(new Peak(4, "Tarnica", "Karpaty", "Bieszczady", 1346, context.getResources().getString(R.string.desc), 49.074574f, 22.726148f));
        this.peaksList.add(new Peak(4, "Śnieżnik", "Sudety", "Masyw Śnieżnika", 1425, context.getResources().getString(R.string.desc), 50.207505f, 16.847320f));
        this.peaksList.add(new Peak(4, "Śnieżka", "Sudety", "Karkonosze", 1602, context.getResources().getString(R.string.desc), 50.736103f, 15.739742f));
        this.peaksList.add(new Peak(4, "Babia Góra - Diablak", "Karpaty", "Beskid Żywiecki", 1725, context.getResources().getString(R.string.desc), 49.573117f, 19.529515f));
        this.peaksList.add(new Peak(4, "Rysy", "Karpaty", "Tatry", 2499, context.getResources().getString(R.string.desc), 49.179740f, 20.088072f));

        //Wielka Korona Tatr
        this.peaksList.add(new Peak(2, "Gerlach", "Karpaty", "Tatry", 2655, " ", 49.1643768f, 20.1248435f));
        this.peaksList.add(new Peak(2, "Łomica", "Karpaty", "Tatry", 2634, " ", 49.1952999f, 20.2043919f));
        this.peaksList.add(new Peak(2, "Lodowy Szczyt", "Karpaty", "Tatry", 2627, " ", 49.1988879f, 20.1759674f));
        this.peaksList.add(new Peak(2, "Durny Szczyt", "Karpaty", "Tatry", 2623, " ", 49.1977772f, 20.1990232f));
        this.peaksList.add(new Peak(2, "Wysoka", "Karpaty", "Tatry", 2560, " ", 49.1727771f, 20.0856892f));
        this.peaksList.add(new Peak(2, "Kieżmarski Szczyt", "Karpaty", "Tatry", 2558, " ", 49.1988881f, 20.2112452f));
        this.peaksList.add(new Peak(2, "Kończysta", "Karpaty", "Tatry", 2540, " ", 49.1572213f, 20.105134f));
        this.peaksList.add(new Peak(2, "Baranie Rogi", "Karpaty", "Tatry", 2526, " ", 49.2019431f, 20.1881892f));
        this.peaksList.add(new Peak(2, "Rysy", "Karpaty", "Tatry", 2503, " ", 49.1795471f, 20.0793092f));
        this.peaksList.add(new Peak(2, "Krywań", "Karpaty", "Tatry", 2494, " ", 49.1625445f, 19.9911611f));
        this.peaksList.add(new Peak(2, "Staroleśny Szczyt", "Karpaty", "Tatry", 2476, " ", 49.1711951f, 20.1457637f));
        this.peaksList.add(new Peak(2, "Ganek", "Karpaty", "Tatry", 2465, " ", 49.1743321f, 20.0951062f));
        this.peaksList.add(new Peak(2, "Sławkowski Szczyt", "Karpaty", "Tatry", 2452, " ", 49.1661102f, 20.1756895f));
        this.peaksList.add(new Peak(2, "Pośrednia Grań", "Karpaty", "Tatry", 2440, " ", 49.1866661f, 20.1851342f));

        // Korona polski - suplement
        this.peaksList.add(new Peak(5, "Rudawiec", "Sudety", "Góry Bialskie", 1102, context.getResources().getString(R.string.rudawiec_desc), 50.244821f, 16.9764950f));
        this.peaksList.add(new Peak(5, "Kowadło", "Sudety", "Góry Złote", 989, context.getResources().getString(R.string.kowadlo_desc), 50.264395f, 17.013273f));
        this.peaksList.add(new Peak(5, "Lubomirt", "Karpaty", "Beskid Makowski", 904, context.getResources().getString(R.string.lubomirt_desc), 49.766849f, 20.059372f));
        this.peaksList.add(new Peak(5, "Chełmiec", "Sudety", "Góry Wałbrzyskie", 851, context.getResources().getString(R.string.chelmiec_desc), 50.779619f, 16.210728f));
        this.peaksList.add(new Peak(5, "Kłodzka Góra", "Sudety", "Góry Bardzkie", 763, context.getResources().getString(R.string.klodzka_desc), 50.4513164f, 16.7530689f));

        //Turystyczna Korona Tatr
        this.peaksList.add(new Peak(3, "Rysy", "Karpaty", "Tatry", 2503, context.getResources().getString(R.string.desc), 49.179740f, 20.088072f));
        this.peaksList.add(new Peak(3, "Rysy", "Karpaty", "Tatry", 2499, context.getResources().getString(R.string.desc), 49.179740f, 20.088072f));
        this.peaksList.add(new Peak(3, "Krywań", "Karpaty", "Tatry", 2494, context.getResources().getString(R.string.desc), 49.1625445f, 19.9911611f));
        this.peaksList.add(new Peak(3, "Sławkowski Szczyt", "Karpaty", "Tatry", 2452, context.getResources().getString(R.string.desc), 49.1661102f, 20.1756895f));
        this.peaksList.add(new Peak(3, "Mała Wysoka", "Karpaty", "Tatry", 2429, context.getResources().getString(R.string.desc), 49.1747211f, 20.1370782f));
        this.peaksList.add(new Peak(3, "Lodowa Przełęcz", "Karpaty", "Tatry", 2372, context.getResources().getString(R.string.desc), 49.1929579f, 20.1808295f));
        this.peaksList.add(new Peak(3, "Koprowy Wierch", "Karpaty", "Tatry", 2367, context.getResources().getString(R.string.desc), 49.1827055f, 20.039818f));
        this.peaksList.add(new Peak(3, "Czerwona Ławka", "Karpaty", "Tatry", 2352, context.getResources().getString(R.string.desc), 49.1883693f, 20.1831319f));
        this.peaksList.add(new Peak(3, "Bystry Przechód", "Karpaty", "Tatry", 2314, context.getResources().getString(R.string.desc), 49.1673868f, 20.0205553f));
        this.peaksList.add(new Peak(3, "Przełęcz pod Chłopkiem", "Karpaty", "Tatry", 2307, context.getResources().getString(R.string.desc), 49.1836225f, 20.0631772f));
        this.peaksList.add(new Peak(3, "Świnica", "Karpaty", "Tatry", 2301, context.getResources().getString(R.string.desc), 49.2194068f, 20.0005269f));
        this.peaksList.add(new Peak(3, "Kozi Wierch", "Karpaty", "Tatry", 2291, context.getResources().getString(R.string.desc), 49.2184108f, 20.0201461f));
        this.peaksList.add(new Peak(3, "Rohatka", "Karpaty", "Tatry", 2288, context.getResources().getString(R.string.desc), 49.177054f, 20.1427419f));
        this.peaksList.add(new Peak(3, "Kozie Czuby", "Karpaty", "Tatry", 2266, context.getResources().getString(R.string.desc), 49.2189102f, 20.0181948f));
        this.peaksList.add(new Peak(3, "Bystra", "Karpaty", "Tatry", 2248, context.getResources().getString(R.string.desc), 49.1890059f, 19.8337433f));
        this.peaksList.add(new Peak(3, "Zadni Granat", "Karpaty", "Tatry", 2240, context.getResources().getString(R.string.desc), 49.2244215f, 20.0237202f));
        this.peaksList.add(new Peak(3, "Pośredni Granat", "Karpaty", "Tatry", 2234, context.getResources().getString(R.string.desc), 49.2257246f, 20.0241494f));
        this.peaksList.add(new Peak(3, "Jagnięcy Szczyt", "Karpaty", "Tatry", 2229, context.getResources().getString(R.string.desc), 49.2199018f, 20.1995552f));
        this.peaksList.add(new Peak(3, "Mały Kozi Wierch", "Karpaty", "Tatry", 2228, context.getResources().getString(R.string.desc), 49.2189032f, 20.0112963f));
        this.peaksList.add(new Peak(3, "Skrajny Granat", "Karpaty", "Tatry", 2225, context.getResources().getString(R.string.desc), 49.227028f, 20.0245785f));
        this.peaksList.add(new Peak(3, "Raczkowa Czuba", "Karpaty", "Tatry", 2194, context.getResources().getString(R.string.desc), 49.1938879f, 19.7943007f));
        this.peaksList.add(new Peak(3, "Baraniec", "Karpaty", "Tatry", 2184, context.getResources().getString(R.string.desc), 49.1741657f, 19.7337452f));
        this.peaksList.add(new Peak(3, "Banówka", "Karpaty", "Tatry", 2178, context.getResources().getString(R.string.desc), 49.1983325f, 19.7031896f));
        this.peaksList.add(new Peak(3, "Starorobociański Wierch", "Karpaty", "Tatry", 2176, context.getResources().getString(R.string.desc), 49.1997088f, 19.8110961f));
        this.peaksList.add(new Peak(3, "Szpiglasowy Wierch", "Karpaty", "Tatry", 2172, context.getResources().getString(R.string.desc), 49.1972211f, 20.0312452f));
        this.peaksList.add(new Peak(3, "Pachola", "Karpaty", "Tatry", 2166, context.getResources().getString(R.string.desc), 49.2011739f, 19.6939909f));
        this.peaksList.add(new Peak(3, "Hruba Kopa", "Karpaty", "Tatry", 2163, context.getResources().getString(R.string.desc), 49.1996807f, 19.7146922f));
        this.peaksList.add(new Peak(3, "Błyszcz", "Karpaty", "Tatry", 2159, context.getResources().getString(R.string.desc), 49.193511f, 19.8328113f));
        this.peaksList.add(new Peak(3, "Kościelec", "Karpaty", "Tatry", 2155, context.getResources().getString(R.string.desc), 49.2253989f, 20.0058191f));
        this.peaksList.add(new Peak(3, "Trzy Kopy", "Karpaty", "Tatry", 2150, context.getResources().getString(R.string.desc), 49.1996932f, 19.7207172f));
        this.peaksList.add(new Peak(3, "Wyżni Przysłop", "Karpaty", "Tatry", 2145, context.getResources().getString(R.string.desc), 49.1927771f, 19.7009672f));
        this.peaksList.add(new Peak(3, "Jarząbczy Wierch", "Karpaty", "Tatry", 2137, context.getResources().getString(R.string.desc), 49.1983067f, 19.786334f));
        this.peaksList.add(new Peak(3, "Rohacz Płaczliwy", "Karpaty", "Tatry", 2126, context.getResources().getString(R.string.desc), 49.196652f, 19.7400712f));
        this.peaksList.add(new Peak(3, "Krzesanica", "Karpaty", "Tatry", 2122, context.getResources().getString(R.string.desc), 49.2324894f, 19.9032997f));
        this.peaksList.add(new Peak(3, "Skrajne Solisko", "Karpaty", "Tatry", 2117, context.getResources().getString(R.string.desc), 49.1469431f, 20.0276342f));
        this.peaksList.add(new Peak(3, "Małołączniak", "Karpaty", "Tatry", 2096, context.getResources().getString(R.string.desc), 49.2357995f, 19.9105953f));
        this.peaksList.add(new Peak(3, "Ciemniak", "Karpaty", "Tatry", 2096, context.getResources().getString(R.string.desc), 49.2309432f, 19.8945512f));
        this.peaksList.add(new Peak(3, "Rohacz Ostry", "Karpaty", "Tatry", 2088, context.getResources().getString(R.string.desc), 49.1999926f, 19.7454356f));
        this.peaksList.add(new Peak(3, "Spalona Kopa", "Karpaty", "Tatry", 2083, context.getResources().getString(R.string.desc), 49.2072212f, 19.6978282f));
        this.peaksList.add(new Peak(3, "Smrek", "Karpaty", "Tatry", 2089, context.getResources().getString(R.string.desc), 49.1827772f, 19.7393011f));
        this.peaksList.add(new Peak(3, "Wołowiec", "Karpaty", "Tatry", 2064, context.getResources().getString(R.string.desc), 49.2075139f, 19.754337f));
        this.peaksList.add(new Peak(3, "Salatyn", "Karpaty", "Tatry", 2050, context.getResources().getString(R.string.desc), 49.2135454f, 19.6774576f));
        this.peaksList.add(new Peak(3, "Rakuska Czuba", "Karpaty", "Tatry", 2037, context.getResources().getString(R.string.desc), 49.2044432f, 20.2276342f));
        this.peaksList.add(new Peak(3, "Wrota Chałubińskiego", "Karpaty", "Tatry", 2022, context.getResources().getString(R.string.desc), 49.1917552f, 20.0428412f));
        this.peaksList.add(new Peak(3, "Beskid", "Karpaty", "Tatry", 2012, context.getResources().getString(R.string.desc), 49.230986f, 19.9923418f));
        this.peaksList.add(new Peak(3, "Kopa Kondracka", "Karpaty", "Tatry", 2005, context.getResources().getString(R.string.desc), 49.2362548f, 19.9234378f));
        this.peaksList.add(new Peak(3, "Kończysty Wierch", "Karpaty", "Tatry", 2002, context.getResources().getString(R.string.desc), 49.2056252f, 19.7987794f));
        this.peaksList.add(new Peak(3, "Kasprowy Wierch", "Karpaty", "Tatry", 1987, context.getResources().getString(R.string.desc), 49.2321633f, 19.9730432f));
        this.peaksList.add(new Peak(3, "Osterwa", "Karpaty", "Tatry", 1984, context.getResources().getString(R.string.desc), 49.1498321f, 20.0812312f));
        this.peaksList.add(new Peak(3, "Brestowa", "Karpaty", "Tatry", 1934, context.getResources().getString(R.string.desc), 49.224821f, 19.6708488f));
        this.peaksList.add(new Peak(3, "Giewont", "Karpaty", "Tatry", 1895, context.getResources().getString(R.string.desc), 49.2510013f, 19.9252803f));
        this.peaksList.add(new Peak(3, "Rakoń", "Karpaty", "Tatry", 1879, context.getResources().getString(R.string.desc), 49.2159985f, 19.7496856f));
        this.peaksList.add(new Peak(3, "Świstowa Czuba", "Karpaty", "Tatry", 1763, context.getResources().getString(R.string.desc), 49.2163777f, 20.0552473f));
        this.peaksList.add(new Peak(3, "Ornak", "Karpaty", "Tatry", 1854, context.getResources().getString(R.string.desc), 49.2189978f, 19.8244428f));
        this.peaksList.add(new Peak(3, "Siwy Wierch", "Karpaty", "Tatry", 1805, context.getResources().getString(R.string.desc), 49.2122213f, 19.6331897f));
        this.peaksList.add(new Peak(3, "Trzydniowiański Wierch", "Karpaty", "Tatry", 1758, context.getResources().getString(R.string.desc), 49.2190249f, 19.8015448f));
        this.peaksList.add(new Peak(3, "Grześ", "Karpaty", "Tatry", 1653, context.getResources().getString(R.string.desc), 49.2365885f, 19.7578529f));
        this.peaksList.add(new Peak(3, "Gęsia Szyja", "Karpaty", "Tatry", 1489, context.getResources().getString(R.string.desc), 49.2597213f, 20.069023f));
        this.peaksList.add(new Peak(3, "Wielki Kopieniec", "Karpaty", "Tatry", 1328, context.getResources().getString(R.string.desc), 49.2713851f, 20.0142256f));
        this.peaksList.add(new Peak(3, "Nosal", "Karpaty", "Tatry", 1206, context.getResources().getString(R.string.desc), 49.2766974f, 19.9808478f));

        // Orla Perć
        this.peaksList.add(new Peak(7, "Przełęcz Zawrat", "Karpaty", "Tatry", 2159, context.getResources().getString(R.string.desc), 49.219159f, 20.0077798f));
        this.peaksList.add(new Peak(7, "Mały Kozi Wierch", "Karpaty", "Tatry", 2228, context.getResources().getString(R.string.desc), 49.2189032f, 20.0112963f));
        this.peaksList.add(new Peak(7, "Zamarła Turnia", "Karpaty", "Tatry", 2179, context.getResources().getString(R.string.desc), 49.2194359f, 20.0158023f));
        this.peaksList.add(new Peak(7, "Kozia Przełęcz", "Karpaty", "Tatry", 2137, context.getResources().getString(R.string.desc), 49.2195296f, 20.0230577f));
        this.peaksList.add(new Peak(7, "Kozie Czuby", "Karpaty", "Tatry", 2266, context.getResources().getString(R.string.desc), 49.2186734f, 20.025376f));
        this.peaksList.add(new Peak(7, "Kozi Wierch", "Karpaty", "Tatry", 2291, context.getResources().getString(R.string.desc), 49.2184108f, 20.0201461f));
        this.peaksList.add(new Peak(7, "Czarne Ściany", "Karpaty", "Tatry", 2242, context.getResources().getString(R.string.desc), 49.2210914f, 20.0272881f));
        this.peaksList.add(new Peak(7, "Granaty", "Karpaty", "Tatry", 2240, context.getResources().getString(R.string.desc), 49.2245562f, 20.0233283f));
        this.peaksList.add(new Peak(7, "Orla Baszta", "Karpaty", "Tatry", 2177, context.getResources().getString(R.string.desc), 49.2275428f, 20.0253615f));
        this.peaksList.add(new Peak(7, "Wielka Buczynowa Turnia", "Karpaty", "Tatry", 2184, context.getResources().getString(R.string.desc), 49.2270213f, 20.0389514f));
        this.peaksList.add(new Peak(7, "Mała Buczynowa Turnia", "Karpaty", "Tatry", 2172, context.getResources().getString(R.string.desc), 49.2272543f, 20.0399572f));
        this.peaksList.add(new Peak(7, "Przełęcz Krzyżne", "Karpaty", "Tatry ", 2135, context.getResources().getString(R.string.desc), 49.2286885f, 20.0385153f));

        //Korona Bieszczad
        this.peaksList.add(new Peak(8, "Tarnica", "Karpaty", "Bieszczady", 1346, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Krzemień", "Karpaty", "Bieszczady", 1335, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Halicz", "Karpaty", "Bieszczady", 1333, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Kopa Bukowska", "Karpaty", "Bieszczady", 1320, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Szeroki Wierch", "Karpaty", "Bieszczady", 1315, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Bukowe Berdo", "Karpaty", "Bieszczady", 1311, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Wielka Rawka", "Karpaty", "Bieszczady", 1307, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Połonina Caryńska", "Karpaty", "Bieszczady", 1297, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Rozsypaniec", "Karpaty", "Bieszczady", 1280, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Mała Rawka", "Karpaty", "Bieszczady", 1272, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Połonina Wetlińska", "Karpaty", "Bieszczady", 1255, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Smerek", "Karpaty", "Bieszczady", 1222, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Krzemieniec", "Karpaty", "Bieszczady", 1221, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Kamienna", "Karpaty", "Bieszczady", 1201, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Rabia Skała", "Karpaty", "Bieszczady", 1199, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Paportna", "Karpaty", "Bieszczady", 1198, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Dziurkowiec", "Karpaty", "Bieszczady", 1188, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Płasza", "Karpaty", "Bieszczady", 1063, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Jasło", "Karpaty", "Bieszczady", 1153, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Fereczata", "Karpaty", "Bieszczady", 1102, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Okrąglik", "Karpaty", "Bieszczady", 1101, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Wołosań", "Karpaty", "Bieszczady", 1071, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Łopiennik", "Karpaty", "Bieszczady", 1069, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Magura Stuposiańska", "Karpaty", "Bieszczady", 1016, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Stryb", "Karpaty", "Bieszczady", 1011, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Chryszczata", "Karpaty", "Bieszczady", 997, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Czerenina", "Karpaty", "Bieszczady", 981, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Trohaniec", "Karpaty", "Bieszczady", 939, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Wysoki Groń", "Karpaty", "Bieszczady", 905, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(8, "Korbania ", "Karpaty", "Bieszczady", 894, context.getResources().getString(R.string.desc), 0f, 0f));

        //Korona Sudet
        this.peaksList.add(new Peak(9, "Luż", "Sudety", "Góry Łużyckie", 793, context.getResources().getString(R.string.desc), 50.8286072f, 14.6865123f));
        this.peaksList.add(new Peak(9, "Ještěd", "Sudety", "Grzbiet Jesztiedzki", 1012, context.getResources().getString(R.string.desc), 50.7334733f, 14.9463001f));
        this.peaksList.add(new Peak(9, "Wysoka Kopa", "Sudety", "Góry Izerskie", 1126, context.getResources().getString(R.string.desc), 50.8499991f, 15.4106896f));
        this.peaksList.add(new Peak(9, "Śnieżka", "Sudety", "Karkonosze", 1602, context.getResources().getString(R.string.desc), 50.7360162f, 15.7311909f));
        this.peaksList.add(new Peak(9, "Skalnik", "Sudety", "Rudawy Janowickie", 945, context.getResources().getString(R.string.desc), 50.8083219f, 15.8998623f));
        this.peaksList.add(new Peak(9, "Skopiec", "Sudety", "Góry Kaczawskie", 724, context.getResources().getString(R.string.desc), 50.9444509f, 15.882819f));
        this.peaksList.add(new Peak(9, "Žaltman", "Sudety", "Góry Jastrzębie", 739, context.getResources().getString(R.string.desc), 50.5524174f, 16.0512871f));
        this.peaksList.add(new Peak(9, "Waligóra", "Sudety", "Góry Kamienne", 936, context.getResources().getString(R.string.desc), 50.6824469f, 16.2706828f));
        this.peaksList.add(new Peak(9, "Borowa", "Sudety", "Góry Wałbrzyskie", 853, context.getResources().getString(R.string.desc), 50.7230351f, 16.2952137f));
        this.peaksList.add(new Peak(9, "Wielka Sowa", "Sudety", "Góry Sowie", 1015, context.getResources().getString(R.string.desc), 50.680344f, 16.4767429f));
        this.peaksList.add(new Peak(9, "Kłodzka Góra", "Sudety", "Góry Bardzkie", 765, context.getResources().getString(R.string.desc), 50.4516517f, 16.7444568f));
        this.peaksList.add(new Peak(9, "Szczeliniec Wielki", "Sudety", "Góry Stołowe", 919, context.getResources().getString(R.string.desc), 50.4836576f, 16.3401783f));
        this.peaksList.add(new Peak(9, "Jagodna", "Sudety", "Góry Bystrzyckie", 977, context.getResources().getString(R.string.desc), 50.252776f, 16.5579118f));
        this.peaksList.add(new Peak(9, "Velká Deštná", "Sudety", "Góry Orlickie", 1115, context.getResources().getString(R.string.desc), 50.3022213f, 16.389023f));
        this.peaksList.add(new Peak(9, "Smrek", "Sudety", "Góry Złote i Bialskie", 1125, context.getResources().getString(R.string.desc), 50.8999697f, 15.2749261f));
        this.peaksList.add(new Peak(9, "Śnieżnik", "Sudety", "Masyw Śnieżnika", 1425, context.getResources().getString(R.string.desc), 50.1729673f, 16.802597f));
        this.peaksList.add(new Peak(9, "Láze", "Sudety", "Zábřežská vrchovina", 714, context.getResources().getString(R.string.desc), 0f, 0f));
        this.peaksList.add(new Peak(9, "Jeřáb", "Sudety", "Hanušovická vrchovina", 1003, context.getResources().getString(R.string.desc), 50.0666658f, 16.8079119f));
        this.peaksList.add(new Peak(9, "Pradziad", "Sudety", "Wysoki Jesionik", 1491, context.getResources().getString(R.string.desc), 50.0830684f, 17.2222094f));
        this.peaksList.add(new Peak(9, "Příčný vrch", "Sudety", "Góry Opawskie", 975, context.getResources().getString(R.string.desc), 50.224381f, 17.3791585f));
        this.peaksList.add(new Peak(9, "Slunečná", "Sudety", "Niski Jesionik", 800, context.getResources().getString(R.string.desc), 49.8427769f, 17.4229119f));
        this.peaksList.add(new Peak(9, "Ślęża", "Sudety", "Masyw Ślęży", 718, context.getResources().getString(R.string.desc), 50.8647302f, 16.699094f));

        //Górskie Jeziora i Wodospady
        this.peaksList.add(new Peak(10, "Morskie Oko", "Karpaty", "Tatry", 1393, context.getResources().getString(R.string.desc), 49.1973639f, 20.0653292f));
        this.peaksList.add(new Peak(10, "Wielki Staw Polski", "Karpaty", "Tatry", 1664, context.getResources().getString(R.string.desc), 49.2085499f, 20.0352829f));
        this.peaksList.add(new Peak(10, "Czarny Staw pod Rysami", "Karpaty", "Tatry", 1579, context.getResources().getString(R.string.desc), 49.1885359f, 20.0718961f));
        this.peaksList.add(new Peak(10, "Wielki Hińczowy Staw", "Karpaty", "Tatry", 1945, context.getResources().getString(R.string.desc), 49.1789674f, 20.0551455f));
        this.peaksList.add(new Peak(10, "Szczyrbskie Jezioro", "Karpaty", "Tatry", 1346, context.getResources().getString(R.string.desc), 49.1222314f, 20.0535315f));
        this.peaksList.add(new Peak(10, "Czarny Staw Gąsienicowy", "Karpaty", "Tatry", 1624, context.getResources().getString(R.string.desc), 49.23067f, 20.0135059f));
        this.peaksList.add(new Peak(10, "Czarny Staw Polski", "Karpaty", "Tatry", 1722, context.getResources().getString(R.string.desc), 49.2042232f, 20.0239261f));
        this.peaksList.add(new Peak(10, "Niżni Ciemnosmreczyński Staw", "Karpaty", "Tatry", 1674, context.getResources().getString(R.string.desc), 49.1927072f, 20.027036f));
        this.peaksList.add(new Peak(10, "Wyżni Żabi Staw Białczański", "Karpaty", "Tatry", 1702, context.getResources().getString(R.string.desc), 49.1938557f, 20.0905926f));
        this.peaksList.add(new Peak(10, "Przedni Staw Polski", "Karpaty", "Tatry", 1668, context.getResources().getString(R.string.desc), 49.2118847f, 20.0432251f));
        this.peaksList.add(new Peak(10, "Popradzki Staw", "Karpaty", "Tatry", 1494, context.getResources().getString(R.string.desc), 49.1532465f, 20.0777552f));
        this.peaksList.add(new Peak(10, "Zadni Staw Polski", "Karpaty", "Tatry", 1890, context.getResources().getString(R.string.desc), 49.2129853f, 20.0106648f));
        this.peaksList.add(new Peak(10, "Wyżni Ciemnosmreczyński Staw", "Karpaty", "Tatry", 1716, context.getResources().getString(R.string.desc), 49.1887461f, 20.0369087f));
        this.peaksList.add(new Peak(10, "Niżni Teriański Staw", "Karpaty", "Tatry", 1941, context.getResources().getString(R.string.desc), 49.1697492f, 20.0117665f));
        this.peaksList.add(new Peak(10, "Wyżni Wielki Furkotny Staw", "Karpaty", "Tatry", 2145, context.getResources().getString(R.string.desc), 49.164059f, 20.023546f));
        this.peaksList.add(new Peak(10, "Siklawa", "Karpaty", "Tatry", 1666, context.getResources().getString(R.string.desc), 49.212367f, 20.042308f));
        this.peaksList.add(new Peak(10, "Siklawica", "Karpaty", "Tatry", 1129, context.getResources().getString(R.string.desc), 49.259253f, 19.930229f));
        this.peaksList.add(new Peak(10, "Wodogrzmoty Mickiewicza", "Karpaty", "Tatry", 1100, context.getResources().getString(R.string.desc), 49.233756f, 20.088485f));

        //Korona Ziemi Wojnickiej
        this.peaksList.add(new Peak(11, "Panieńska Góra", "Karpaty", "Beskid Wyspowy", 326, context.getResources().getString(R.string.panienska_desc), 49.926625f, 20.825079f));
        this.peaksList.add(new Peak(11, "Wilkówka", "Karpaty", "Beskid Wyspowy", 409, context.getResources().getString(R.string.wilkowka_desc), 49.9165745f, 20.746376f));
        this.peaksList.add(new Peak(11, "Kamionka", "Karpaty", "Beskid Wyspowy", 382, context.getResources().getString(R.string.kamionka_desc), 49.9260081f, 20.7127647f));
        this.peaksList.add(new Peak(11, "Jurasówka", "Karpaty", "Beskid Wyspowy", 458, context.getResources().getString(R.string.jurasowka_desc), 49.8686911f, 20.8963051f));
        this.peaksList.add(new Peak(11, "Wał", "Karpaty", "Beskid Wyspowy", 502, context.getResources().getString(R.string.wal_desc), 49.8832860f, 20.9108963f));
        this.peaksList.add(new Peak(11, "Lubinka", "Karpaty", "Beskid Wyspowy", 438, context.getResources().getString(R.string.lubinka_desc), 49.9045027f, 20.8985710f));
        this.peaksList.add(new Peak(11, "Góra Świętego Marcina", "Inne", "Beskid Wyspowy", 384, context.getResources().getString(R.string.marcinka_desc), 49.986259f, 21.011369f));

        //Druga Korona Polski
        this.peaksList.add(new Peak(6, "Mięguszowiecki Szczyt", "Karpaty", "Tatry", 2438, context.getResources().getString(R.string.desc), 49.1872211f, 20.0512452f));
        this.peaksList.add(new Peak(6, "Pilsko", "Karpaty", "Beskid Żywiecki", 1557, context.getResources().getString(R.string.desc), 49.5279568f, 19.3146181f));
        this.peaksList.add(new Peak(6, "Wielki Szyszak", "Sudety", "Karkonosze", 1509, context.getResources().getString(R.string.desc), 50.7769435f, 15.560134f));
        this.peaksList.add(new Peak(6, "Mały Śnieżnik", "Sudety", "Masyw Śnieżnika", 1337, context.getResources().getString(R.string.desc), 50.1930546f, 16.8079118f));
        this.peaksList.add(new Peak(6, "Krzemień", "Karpaty", "Bieszczady", 1335, context.getResources().getString(R.string.desc), 49.0881452f, 22.7191119f));
        this.peaksList.add(new Peak(6, "Kudłoń", "Karpaty", "Gorce", 1274, context.getResources().getString(R.string.desc), 49.5725214f, 20.1673595f));
        this.peaksList.add(new Peak(6, "Złomisty Wierch", "Karpaty", "Beskid Sądecki", 1224, context.getResources().getString(R.string.desc), 49.4600681f, 20.5771866f));
        this.peaksList.add(new Peak(6, "Barania Góra", "Karpaty", "Beskid Śląski", 120, context.getResources().getString(R.string.desc), 49.6114415f, 19.001718f));
        this.peaksList.add(new Peak(6, "Smrek", "Sudety", "Góry Izraelskie", 1124, context.getResources().getString(R.string.desc), 50.9028895f, 15.2909764f));
        this.peaksList.add(new Peak(6, "Rudawiec", "Sudety", "Góry Bialskie", 1112, context.getResources().getString(R.string.desc), 50.2440551f, 16.9671342f));
        this.peaksList.add(new Peak(6, "Jałowiec", "Karpaty", "Beskid Makowskiego", 1111, context.getResources().getString(R.string.desc), 49.7016485f, 19.5089118f));
        this.peaksList.add(new Peak(6, "Ćwilin", "Karpaty", "Beskig Wyspowy", 1072, context.getResources().getString(R.string.desc), 49.6887297f, 20.1892986f));
        this.peaksList.add(new Peak(6, "Trzy Korony", "Karpaty", "Pieniny", 982, context.getResources().getString(R.string.desc), 49.4139982f, 20.4052109f));
        this.peaksList.add(new Peak(6, "Sasanka", "Sudety", "Góry Bystrzyckie", 965, context.getResources().getString(R.string.desc), 50.2527768f, 16.5579118f));
        this.peaksList.add(new Peak(6, "Kalenica", "Sudety", "Góry Sowie", 964, context.getResources().getString(R.string.desc), 50.6422329f, 16.5369197f));
        this.peaksList.add(new Peak(6, "Ostry Wierch", "Karpaty", "Beskid Niski", 938, context.getResources().getString(R.string.desc), 49.4388879f, 21.1134674f));
        this.peaksList.add(new Peak(6, "Łamana Skała", "Sudety", "Beskid Mały", 929, context.getResources().getString(R.string.desc), 49.7638879f, 19.3870785f));
        this.peaksList.add(new Peak(6, "Suchawa - Sucha Kopa", "Sudety", "Góry Bialskie", 928, context.getResources().getString(R.string.desc), 50.2524350f, 16.9310989f));
        this.peaksList.add(new Peak(6, "Skalniak", "Sudety", "Góry Stołowe", 915, context.getResources().getString(R.string.desc), 50.4720394f, 16.3216496f));
        this.peaksList.add(new Peak(6, "Dzicza Góra", "Sudety", "Rudawy Janowickie", 881, context.getResources().getString(R.string.desc), 50.8168515f, 15.8866616f));
        this.peaksList.add(new Peak(6, "Chełmiec", "Karpaty", "Góry Wałbrzyskie", 851, context.getResources().getString(R.string.desc), 50.779619f, 16.210728f));
        this.peaksList.add(new Peak(6, "Srebna Kopa", "Sudety", "Góry Opawskie", 785, context.getResources().getString(R.string.desc), 50.2563978f, 17.4523252f));
        this.peaksList.add(new Peak(6, "Ostra Góra", "Sudety", "Góry Bradzkie", 751, context.getResources().getString(R.string.desc), 50.4833321f, 16.2995782f));
        this.peaksList.add(new Peak(6, "Baraniec", "Sudety", "Góry Kaczawskie", 720, context.getResources().getString(R.string.desc), 50.940674f, 15.8825168f));
        this.peaksList.add(new Peak(6, "Łysa Góra", "Góry Świętokrzyskie", "Łysogóry", 594, context.getResources().getString(R.string.desc), 50.8717159f, 20.9638342f));
        this.peaksList.add(new Peak(6, "Radunia", "Sudety", "Masyw Ślęży", 573, context.getResources().getString(R.string.desc), 50.8298954f, 16.6987182f));

    }

    private void initPersonalData() {
        personalDataDBAdapter.open();
        Cursor cursor = personalDataDBAdapter.getLastEntry();
        if (cursor == null) {
            return;
        }
        personalDataDBAdapter.close();
        name = cursor.getString(1);
        surname = cursor.getString(2);
        numberSOS = cursor.getString(3);
    }

    private void LoadGains(Context context) {
        gainsDBAdapter.open();
        Cursor cursor = gainsDBAdapter.getEntry();
        Gain gainTmp;
        if (cursor == null) {
            return;
        }
        while (cursor.moveToNext()) {
            gainTmp = new Gain(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getFloat(5), cursor.getFloat(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getInt(13));
            gainTmp.setmemory(cursor.getString(12));
            gainsList.add(gainTmp);
            getThemAll(gainTmp.getPeakStringId(), gainTmp.getId());
        }
        gainsDBAdapter.close();
    }

    private void getThemAll(String stringId, int gainId) {
        for (Peak tmpPeak : getPeakNameAndHeight(stringId)) {
            tmpPeak.setGainId(gainId);
        }
    }

    private int getQuantity(int type) {
        int quantity = 0;
        for (Peak peak : peaksList) {
            if (peak.getRangeId() == type) {
                quantity++;
            }
        }
        if (quantity == 0) return 1;
        return quantity;
    }

    private int lootedQuantity(int type) {
        int quantity = 0;
        for (Peak peak : peaksList) {
            if (peak.getRangeId() == type) {
                if (peak.getGainId() >= 0) {
                    quantity++;
                }
            }
        }
        return quantity;
    }

    private void sortPeakList() {
        Collections.sort(peaksList, Peak.heightdComparator);
    }

    private boolean ifExist(Peak peak, ArrayList<Peak> peaks) {
        String so2 = peak.getRangeId() + peak.getTitle().trim() + String.valueOf(peak.getHeight()).trim();//+o2.getRange().trim();
        for (Peak tmpPeak : peaks) {
            String so1 = tmpPeak.getRangeId() + tmpPeak.getTitle().trim() + String.valueOf(tmpPeak.getHeight()).trim();//+o1.getRange().trim();
            if (so1.compareTo(so2) == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean ifExistE(Peak peak, ArrayList<Peak> peaks) {
        String so2 = "0" + peak.getTitle().trim() + String.valueOf(peak.getHeight()).trim();//+o2.getRange().trim();
        for (Peak tmpPeak : peaks) {
            String so1 = tmpPeak.getRangeId() + tmpPeak.getTitle().trim() + String.valueOf(tmpPeak.getHeight()).trim();//+o1.getRange().trim();
            if (so1.compareTo(so2) == 0) {
                return false;
            }
        }
        if (peak.getyCord() < 19.98f) {
            return true;
        } else {
            return false;
        }
    }

    private boolean ifExistW(Peak peak, ArrayList<Peak> peaks) {
        String so2 = "1" + peak.getTitle().trim() + String.valueOf(peak.getHeight()).trim();//+o2.getRange().trim();
        for (Peak tmpPeak : peaks) {
            String so1 = tmpPeak.getRangeId() + tmpPeak.getTitle().trim() + String.valueOf(tmpPeak.getHeight()).trim();//+o1.getRange().trim();
            if (so1.compareTo(so2) == 0) {
                return false;
            }
        }
        if (peak.getyCord() >= 19.98f) {
            return true;
        } else {
            return false;
        }
    }

    private void initOtherPeaks() {
        ArrayList<Peak> peakListTmp = new ArrayList<Peak>();
        Peak peakTmp;
        for (Peak peak : peaksList) {
            peakTmp = new Peak(peak);
            switch (peak.getChain()) {
                case "Karpaty":
                    peakTmp.setRangeId(12);
                    if (ifExist(peakTmp, peakListTmp)) {
                        peakListTmp.add(peakTmp);
                    }
                    break;
                case "Sudety":
                    peakTmp.setRangeId(13);
                    if (ifExist(peakTmp, peakListTmp)) {
                        peakListTmp.add(peakTmp);
                        continue;
                    }
                    break;
                case "Góry Świętokrzyskie":
                    peakTmp.setRangeId(14);
                    if (ifExist(peakTmp, peakListTmp)) {
                        peakListTmp.add(peakTmp);
                        continue;
                    }
                    break;
                default:
                    peakTmp.setRangeId(15);
                    if (ifExist(peakTmp, peakListTmp)) {
                        peakListTmp.add(peakTmp);
                        continue;
                    }
                    break;
            }
            if (peak.getRange() == "Tatry") {

                if (ifExistE(peak, peakListTmp)) {
                    peakTmp = new Peak(peak);
                    peakTmp.setRangeId(0);
                    peakListTmp.add(peakTmp);
                } else if (ifExistW(peak, peakListTmp)) {
                    peakTmp = new Peak(peak);
                    peakTmp.setRangeId(1);
                    peakListTmp.add(peakTmp);
                }
            }
        }

        for (Peak peak2 : peakListTmp) {
            peaksList.add(peak2);
        }
    }

    private void initLoadPeaks(Context context) {
        peaksDBAdapter.open();
        Cursor cursor = peaksDBAdapter.getEntry();
        if (cursor != null) {
            while (cursor.moveToNext())
                peaksList.add(new Peak(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getFloat(7), cursor.getFloat(8)));
        } else {
            Log.e("ERROR: ", "CURSOR IS NULL !!!");
        }
        peaksDBAdapter.close();
    }

    public ArrayList<Peak> getPeakNameAndHeight(String index) {
        ArrayList<Peak> tmpPeaks = new ArrayList<>();
        for (Peak tmpPeak : peaksList) {
            String so1 = tmpPeak.getTitle().trim() + String.valueOf(tmpPeak.getHeight()).trim();
            if (so1.compareTo(index) == 0) {
                tmpPeaks.add(tmpPeak);
            }
        }
        return tmpPeaks;
    }

    public Peak getPeakId(int index) {
        for (Peak tmpPeak : peaksList) {
            if (tmpPeak.getId() == index) {
                return tmpPeak;
            }
        }
        return null;
    }
}
