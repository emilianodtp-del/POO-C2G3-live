package pe.edu.upeu.herencia;

public class Carro extends Vehiculo {


    public static void main(String[] args) {
        Carro c = new Carro();
        System.out.println("Caracteristicas:");
        c.marca="Toyota";
        System.out.println("Marca"+c.marca);
        System.out.println("Modelo"+c.modelo);
        System.out.println("Color:"+"No se puede" +
                " heredar por ser privado");
        c.sonido();
    }
}
