public class Ksiazka {
    public int id;
    public String tytul;
    public String autor;
    public String rok;
    public boolean status;
    public int id_wypozyczajacego;

    public Ksiazka(int id, String tytul, String autor, String rok, boolean status){
        this.id = id;
        this.tytul = tytul;
        this.autor = autor;
        this.rok = rok;
        this.status = status;
    }

    public void changeStatus(){
        status = !status;
    }

    public void setId_wypozyczajacego(int id_wypozyczajacego){
        this.id_wypozyczajacego = id_wypozyczajacego;
    }

    @Override
    public String toString(){
        return this.id+";"+this.tytul+";"+this.autor+";"+this.rok+";"+this.status;
    }
}