package arenagt;
import java.util.*;
public interface Resultado {
 default Marcador comoMarcador(){throw new IllegalArgumentException("Se requieren dos marcadores enteros");}
 default ConPenales comoPenales(){throw new IllegalArgumentException("Se requiere resultado con definición de penales");}
 default RelevoResultado comoRelevo(){throw new IllegalArgumentException("Se necesitan tiempos de relevos");}
}
final class Marcador implements Resultado {
 final int a,b;
 @Override public Marcador comoMarcador(){return this;}
 Marcador(int a,int b){this.a=Util.noNegativo(a,"Marcador A");this.b=Util.noNegativo(b,"Marcador B");}
}
final class ConPenales implements Resultado {
 final int a,b;final Integer pa,pb;
 @Override public ConPenales comoPenales(){return this;}
 ConPenales(int a,int b,Integer pa,Integer pb){this.a=Util.noNegativo(a,"Goles A");this.b=Util.noNegativo(b,"Goles B");this.pa=pa;this.pb=pb;}
}
final class RelevoResultado implements Resultado {
 final Map<Participante,Double> tiempos;
 @Override public RelevoResultado comoRelevo(){return this;}
 RelevoResultado(Map<Participante,Double> tiempos){this.tiempos=Collections.unmodifiableMap(new LinkedHashMap<>(tiempos));}
}
