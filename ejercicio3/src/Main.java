import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("CALCULO DE CALORIAS CONSUMIDAS");
        System.out.println("--------------------------");
        System.out.println("elija la actividad: ");
        System.out.println("1:dormir ");
        System.out.println("2:sentado ");
        System.out.println("opcion (1-2)");

        int opcion = sc.nextInt();

        System.out.println("Ingrese el tiempo en minutos que realiza la actividad");
        int minutos = sc.nextInt();

        double calorias=0.0;
        if(opcion==1){
            calorias=minutos*1.08;
            System.out.println("n/actividad: dormir");
        }else if(opcion==2){
            calorias=minutos*1.66;
            System.out.println("n/actividad: sentado en reposo");
        }else{
            System.out.println("OPCION INVALIDA");
            sc.nextLine();
            return;
        }

        System.out.println("tiempo;"+minutos+"minutos");
        System.out.println("calorias consumidas:"+calorias+"cal");

        sc.close();

    }
}