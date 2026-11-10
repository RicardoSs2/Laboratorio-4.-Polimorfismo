package arenagt;
import java.time.LocalDate;
import java.util.*;
public abstract class Evento implements Comparable<Evento> {
 private static int secuencia=0;
 private final String codigo;private final Torneo torneo;
 private LocalDate fecha;private Integer hora;private String sede;private boolean finalizado;
 protected Evento(Torneo torneo){this.torneo=torneo;this.codigo="EV"+(++secuencia);}
 public final String getCodigo(){return codigo;} public final Torneo getTorneo(){return torneo;}
 public final LocalDate getFecha(){return fecha;} public final Integer getHora(){return hora;} public final String getSede(){return sede;}
 public final boolean isProgramado(){return fecha!=null;} public final boolean isFinalizado(){return finalizado;}
 public final String getEstado(){return finalizado?"FINALIZADO":isProgramado()?"PROGRAMADO":"SIN PROGRAMAR";}
 public abstract List<Participante> getParticipantes();
 // Enlace polimórfico: solo Encuentro admite su operación específica.
 public Encuentro comoEncuentro(){throw new IllegalArgumentException("Se requiere un encuentro");}
 public Carrera comoCarrera(){throw new IllegalArgumentException("Se requiere una carrera");}
 final void programar(LocalDate f,int h,String s){Util.exigir(!finalizado,"Evento ya finalizado");Objects.requireNonNull(f,"Fecha obligatoria");Util.exigir(h>=0&&h<=23,"Hora fuera del rango 0-23");s=Util.texto(s,"Sede");fecha=f;hora=h;sede=s;}
 final void finalizar(){Util.exigir(isProgramado()&&!finalizado,"Evento no programado o ya finalizado");finalizado=true;}
 @Override public final int compareTo(Evento o){if(fecha==null&&o.fecha!=null)return 1;if(fecha!=null&&o.fecha==null)return -1;if(fecha!=null&&o.fecha!=null){int c=fecha.compareTo(o.fecha);if(c!=0)return c;c=hora.compareTo(o.hora);if(c!=0)return c;c=sede.compareToIgnoreCase(o.sede);if(c!=0)return c;}return codigo.compareTo(o.codigo);}
 @Override public String toString(){return codigo+" | "+torneo.getNombre()+" | "+getClass().getSimpleName()+" | "+(isProgramado()?fecha+" "+String.format("%02d:00",hora)+" @ "+sede:"Sin programar")+" | "+getEstado();}
}
final class Encuentro extends Evento {
 private final Participante a,b;
 Encuentro(Torneo t,Participante a,Participante b){super(t);this.a=a;this.b=b;}
 @Override public Encuentro comoEncuentro(){return this;}
 public Participante getA(){return a;}public Participante getB(){return b;}
 @Override public List<Participante> getParticipantes(){return Collections.unmodifiableList(Arrays.asList(a,b));}
 @Override public String toString(){return super.toString()+" | "+a.getNombre()+" vs "+b.getNombre();}
}
final class Carrera extends Evento {
 private final List<Participante> participantes;
 Carrera(Torneo t,List<Participante> ps){super(t);participantes=Collections.unmodifiableList(new ArrayList<>(ps));}
 @Override public Carrera comoCarrera(){return this;}
 @Override public List<Participante> getParticipantes(){return participantes;}
}
