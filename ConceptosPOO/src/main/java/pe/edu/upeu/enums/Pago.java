package pe.edu.upeu.enums;

enum TIPO_PAGO {Efectivo, Credito, Transferencia, Yape};
enum MES{Enero, Febrero, }
public class Pago {
    TIPO_PAGO tipo;
    MES mes;
    double monto;
    String cuenta;
    double impuesto;

    public static void main(String[] args) {
        Pago p = new Pago();
        p.tipo = TIPO_PAGO.Efectivo;
        p.mes = MES.Enero;
        p.monto = 100;
        p.cuenta = "14545 54545 5454 5454";
        p.impuesto = 10;

        System.out.println("Tipo de pago: " + p.tipo);
        System.out.println("Mes de pago: " + p.mes);
        System.out.println("Monto: " + p.monto);
        System.out.println("Cuenta: " + p.cuenta);
        System.out.println("Impuesto: " + p.impuesto);

        for (TIPO_PAGO t : TIPO_PAGO.values()) {
            System.out.println("Tipo de pago: " + t);
        }
    }
}
