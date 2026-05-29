package beauty_salon_explorer.salonExplorer.salon.importer;

public enum WarsawDistrict {

    BEMOWO("Bemowo"),
    BIALOLEKA("Białołęka"),
    BIELANY("Bielany"),
    MOKOTOW("Mokotów"),
    OCHOTA("Ochota"),
    PRAGA_POLUDNIE("Praga-Południe"),
    PRAGA_POLNOC("Praga-Północ"),
    REMBERTOW("Rembertów"),
    SRODMIESCIE("Śródmieście"),
    TARGOWEK("Targówek"),
    URSUS("Ursus"),
    URSYNOW("Ursynów"),
    WAWER("Wawer"),
    WESOLA("Wesoła"),
    WILANOW("Wilanów"),
    WLOCHY("Włochy"),
    WOLA("Wola"),
    ZOLIBORZ("Żoliborz");

    private final String displayName;

    WarsawDistrict(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}