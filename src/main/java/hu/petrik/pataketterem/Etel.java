package hu.petrik.pataketterem;

public class Etel {
    private String azonosito;
    private String nev;
    private int kategoriaId;
    private int egysegAr;
    private int eladottMennyiseg;


    public Etel(String azonosito, String nev, int kategoriaId, int egysegAr, int eladottMennyiseg) {
        this.azonosito = azonosito;
        this.nev = nev;
        this.kategoriaId = kategoriaId;
        this.egysegAr = egysegAr;
        this.eladottMennyiseg = eladottMennyiseg;
    }
    public Etel(String sor) {
        String[] adatok = sor.split(";");
        this.azonosito = adatok[0];
        this.nev = adatok[1];
        this.kategoriaId = Integer.parseInt(adatok[2]);
        this.egysegAr = Integer.parseInt(adatok[3]);
        this.eladottMennyiseg = Integer.parseInt(adatok[4]);
    }

    public boolean isHibasAzonosito(){
        return this.azonosito.length() != 10;
    }

    public String getAzonosito() {
        return azonosito;
    }

    public String getNev() {
        return nev;
    }

    public int getKategoriaId() {
        return kategoriaId;
    }

    public String getKategoriaSzoveg() {
        String s = "";
        switch (this.kategoriaId){
            case 1: s = "leves"; break;
            case 2: s = "főétel"; break;
            case 3: s = "desszert"; break;
        }
        return s;
    }

    public int getEgysegAr() {
        return egysegAr;
    }

    public int getEladottMennyiseg() {
        return eladottMennyiseg;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%d;%d;%d", azonosito, nev,kategoriaId,egysegAr,eladottMennyiseg );
    }
}
