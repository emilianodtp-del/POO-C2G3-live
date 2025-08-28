package pe.edu.upeu.Claseinterface;

public class ClaseGeneral {
    public static void main(String[] args) {
        Animal a=new Loro();
        a.Dormir();
        a.EmitirSonido();

        a=new Gato();
        a.Dormir();
        a.EmitirSonido();
    }
}
