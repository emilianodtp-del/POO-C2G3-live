package pe.edu.upeu.claseinterna;

public class ClaseExterna {
    int a,b;

    class ClaseInterna1{
        double r;
        double Sumar(){
            r=a+b;
            return r;
        }
    }


    class ClaseInterna2{
        double r;
        double resta(){
            r=a-b;
            return r;
        }

    }

    public static void main(String[] args) {
        ClaseExterna c=new ClaseExterna();
        c.a=10;
        c.b=5;
        ClaseInterna1 ci1=c.new ClaseInterna1();
        System.out.println(ci1.Sumar());

        ClaseInterna2 ci2=c.new ClaseInterna2();
        ci2.resta();
        System.out.println(ci2.r);

    }


}


class ClaseEnterna3{

}



