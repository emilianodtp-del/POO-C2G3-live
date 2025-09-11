package pe.edu.upeu.encapsulamientox;

public class ClaseGeneral {
    public static void main(String[] args) {
        Persona p = new Persona();// p=objeto
        //p.nombre = "David";
        //p.edad = 25;
        p.setNombre("David"); // encapsulamiento
        p.setEdad(25); // encapsulamiento
        p.apellido = "Mamani";//No se esta aplicando emcapsulamiento
        p.saludo();

        Persona p2 = new Persona("David", "Mamani", 25);
        p2.saludo();

        Trabajador t = new Trabajador();//  t=objeto
        t.setNombre("David");
        t.setApellido("Mamani");
        t.setEdad(25);
        t.setArea("Sistemas");
        t.setGenero('M');
        System.out.println(t);

    }
}
