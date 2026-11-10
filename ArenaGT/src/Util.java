package arenagt;
import java.util.*;
public final class Util {
 private Util(){}
 public static String texto(String s,String campo){if(s==null||s.trim().isEmpty())throw new IllegalArgumentException(campo+" obligatorio");return s.trim();}
 public static void exigir(boolean cond,String mensaje){if(!cond)throw new IllegalArgumentException(mensaje);}
 public static int noNegativo(int n,String campo){exigir(n>=0,campo+" no puede ser negativo");return n;}
 public static int positivo(int n,String campo){exigir(n>0,campo+" debe ser positivo");return n;}
 public static String dinero(double n){return String.format(Locale.US,"Q%.2f",n);}
}
