
public class Numeros_primos {

    public static boolean esPrimo(int n) {
        if (n <= 1) {
            return false; // 1 y números menores no son primos
        }
        if (n <= 3) {
            return true; // 2 y 3 son primos
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false; // Múltiplos de 2 o 3 no son primos
        }
        // Verificar divisores desde 5 hasta sqrt(n), saltando de 6 en 6
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        System.out.println("Números primos entre 1 y 20:");
        for (int i = 1; i <= 20; i++) {
            if (esPrimo(i)) {
                System.out.print(i + " ");
            }
        }
        System.out.println(); // Salto de línea al final
    }
}