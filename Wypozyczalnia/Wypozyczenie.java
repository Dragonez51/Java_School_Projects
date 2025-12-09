public class Wypozyczenie {
    private String imieNazwisko;
    private String nazwaSprzetu;
    private int liczbaDni;
    private double stawkaZaDzien;

    public Wypozyczenie(String imieNazwisko, String nazwaSprzetu, int liczbaDni, double stawkaZaDzien){
        this.imieNazwisko = imieNazwisko;
        this.nazwaSprzetu = nazwaSprzetu;
        this.liczbaDni = liczbaDni;
        this.stawkaZaDzien = stawkaZaDzien;
    }

    public double koszt(){
        return this.liczbaDni * this.stawkaZaDzien;
    }


    /*

    */
    public String getImieNazwisko() {
        return imieNazwisko;
    }

    public void setImieNazwisko(String imieNazwisko) {
        this.imieNazwisko = imieNazwisko;
    }

    public String getNazwaSprzetu() {
        return nazwaSprzetu;
    }

    public void setNazwaSprzetu(String nazwaSprzetu) {
        this.nazwaSprzetu = nazwaSprzetu;
    }

    public int getLiczbaDni() {
        return liczbaDni;
    }

    public void setLiczbaDni(int liczbaDni) {
        this.liczbaDni = liczbaDni;
    }

    public double getStawkaZaDzien() {
        return stawkaZaDzien;
    }

    public void setStawkaZaDzien(double stawkaZaDzien) {
        this.stawkaZaDzien = stawkaZaDzien;
    }
}