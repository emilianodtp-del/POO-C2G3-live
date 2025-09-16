import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ingrese la cantidad de respuestas: ");
        int correctas = scanner.nextInt();
        System.out.println("ingrese la cantidad de respuestas incorrectas: ");
        int incorrectas = scanner.nextInt();
        System.out.println("ingrese la cantidad de respuestas en blanco: ");
        int enBlanco = scanner.nextInt();

        int puntaje =(correctas*3)+(incorrectas*-1)+(enBlanco*0);
        System.out.println("/n-----------");
        System.out.println("RESULTADO DEL POSTULANTE");
        System.out.println("-------------------------------");
        System.out.println(" respuestas correctas: "+correctas+"->+"+(correctas*3)+"puntos");
        System.out.println(" respuestas incorrectas: "+incorrectas+"->+"+(incorrectas*-1)+"puntos");
        System.out.println(" respuestas en blanco"+enBlanco+ "-> 0 puntos");
        System.out.println("---------------------------------------------------");
        System.out.println("puntaje final del postulante es:"+puntaje +"puntos");

        if (puntaje >= 30){
            System.out.println("el postulante APROBÓ.");
        }else {
            System.out.println("el postulanbte DESAPROBÓ.");
        }
    }
}
