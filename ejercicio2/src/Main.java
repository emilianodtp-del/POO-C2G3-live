import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    System.out.println("¿que figura quieres crear? ");
        System.out.println("1:cuadrado ");
        System.out.println("2:triangulo ");
        System.out.println("3:rectangulo ");
        System.out.println("4:rombo ");
        System.out.println("Elige una opcion (1-4): ");
        int opcion = scan.nextInt();

        System.out.print("ingrese el tamaño del lado: ");
        int lado = scan.nextInt();

        System.out.println("n/Aqui tienes tu figura:/n");

        switch (opcion) {
            case 1:
                for (int i = 0; i < lado; i++) {
                    for (int j = 0; j < lado; j++) {
                        System.out.print("*");
                    }
                    System.out.println();
                }
                break;

                case 2:
                    for (int i = 1; i <= lado; i++) {
                        for (int j = 1; j <= i; j++) {
                            System.out.print("* ");
                        }
                        System.out.println();
                    }
                    break;

                    case 3:
                        for (int i = 0; i < lado; i++) {
                            for (int j = 0; j < lado*2; j++) {
                                System.out.print(" * ");
                            }
                            System.out.println();
                        }
                        break;
                        case 4:
                            for (int i = 1; i <= lado; i++) {
                                for (int j = i; j < lado; j++) {
                                    System.out.print(" ");
                                }
                                for (int j = 1; j <= (2*i-1); j++) {
                                    System.out.print("*");
                                }
                                System.out.println();
                            }
                            for (int i = lado-1; i >= 1; i--) {
                                for (int j = lado; j > i; j--) {
                                    System.out.print(" ");
                                }
                                for (int j = 1; j <= (2*i-1); j++) {
                                    System.out.print("*");
                                }
                                System.out.println();
                            }
                            break;

                            default:
                                System.out.println("Opcion invalida");
        }
        scan.close();
    }
}