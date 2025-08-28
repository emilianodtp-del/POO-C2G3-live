package pe.edu.upeu.enums;

enum TIPO_PAGO{Efectivo, Credito, transferencia, Yape}

enum MES{Enero, Febrero}

public class Pago {

    TIPO_PAGO tipo;
    MES mes;
    double monto;
    String cuenta;
    double impuesto;


    public static void main(String[] args) {
        Pago p=new Pago();
        p.tipo=TIPO_PAGO.Efectivo;
        p.mes=MES.Enero;
        p.monto=100;
        p.impuesto=100;
        p.cuenta="1232 3531 2332";

        for(TIPO_PAGO t:TIPO_PAGO.values()){
            System.out.println("Tipo de pago"+t);
        }


    }



}










