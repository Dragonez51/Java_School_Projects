public class Ksiazka {
    public String tytul;
    public String autor;
    public String rok;
    public boolean status;

    public Ksiazka(String tytul, String autor, String rok, boolean status){
        this.tytul = tytul;
        this.autor = autor;
        this.rok = rok;
        this.status = status;
    }

    public void changeStatus(){
        status = !status;
    }

    @Override
    public String toString(){
        return this.tytul+";"+this.autor+";"+this.rok+";"+this.status;
    }
}