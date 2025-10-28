import java.util.Scanner;

public class TresEnRaya {

    private static char[][] tablero = new char[3][3];
    private static char jugadorActual = 'X';

    public static void main(String[] args) {
        inicializarTablero();
        boolean juegoTerminado = false;
        Scanner scanner = new Scanner(System.in);
        while (!juegoTerminado) {
            mostrarTablero();
            System.out.println("Jugador " + jugadorActual + ", ingresa fila (0-2) y columna (0-2): ");
            int fila = scanner.nextInt();
            int columna = scanner.nextInt();
            if (fila >= 0 && fila < 3 && columna >= 0 && columna < 3 && tablero[fila][columna] == ' ') {
                tablero[fila][columna] = jugadorActual;
                if (verificarGanador()) {
                    mostrarTablero();
                    System.out.println("¡Jugador " + jugadorActual + " gana!");
                    juegoTerminado = true;
                } else if (esEmpate()) {
                    mostrarTablero();
                    System.out.println("¡Es un empate!");
                    juegoTerminado = true;
                } else {
                    cambiarJugador();
                }
            } else {
                System.out.println("Movimiento inválido. Intenta de nuevo.");
            }
        }
        scanner.close();
    }


    private static void inicializarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = ' ';
            }
        }
    }


    private static void mostrarTablero() {
        System.out.println("  0 1 2");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(tablero[i][j]);
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("  -----");
        }
    }


    private static void cambiarJugador() {
        jugadorActual = (jugadorActual == 'X') ? 'O' : 'X';
    }


    private static boolean verificarGanador() {
        // Verificar filas, columnas y diagonales
        for (int i = 0; i < 3; i++) {
            if ((tablero[i][0] == jugadorActual && tablero[i][1] == jugadorActual && tablero[i][2] == jugadorActual) ||
                    (tablero[0][i] == jugadorActual && tablero[1][i] == jugadorActual && tablero[2][i] == jugadorActual)) {
                return true;
            }
        }
        if ((tablero[0][0] == jugadorActual && tablero[1][1] == jugadorActual && tablero[2][2] == jugadorActual) ||
                (tablero[0][2] == jugadorActual && tablero[1][1] == jugadorActual && tablero[2][0] == jugadorActual)) {
            return true;
        }
        return false;
    }


    private static boolean esEmpate() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}

