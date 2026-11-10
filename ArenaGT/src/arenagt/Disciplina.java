package arenagt;
import java.util.*;
public abstract class Disciplina {
 private final String nombre;private final int minimo,maximo,cuota;
 protected Disciplina(String nombre,int minimo,int maximo,int cuota){this.nombre=nombre;this.minimo=minimo;this.maximo=maximo;this.cuota=cuota;}
 public final String getNombre(){return nombre;} public final int getMinimo(){return minimo;} public final int getMaximo(){return maximo;} public final int getCuota(){return cuota;}
 public final double cotizar(int cantidad){Util.positivo(cantidad,"Cantidad");Util.exigir(cantidad<=maximo,"Cantidad supera máximo de "+maximo);return (double)cuota*cantidad;}
 public abstract List<Evento> crearEventos(Torneo torneo);
 public abstract Resultado leerResultado(Scanner sc,Evento evento);
 protected static int leerInt(Scanner sc,String texto){System.out.print(texto+": ");try{return Integer.parseInt(sc.nextLine().trim());}catch(NumberFormatException ex){throw new IllegalArgumentException("Ingrese un entero válido");}}
 protected static Marcador leerMarcador(Scanner sc){return new Marcador(leerInt(sc,"Marcador/sets A"),leerInt(sc,"Marcador/sets B"));}
 public abstract Map<Participante,Puntuacion> evaluar(Evento evento,Resultado resultado);
 protected Map<Participante,Puntuacion> marcador(Encuentro e,int a,int b,int pa,int pb){Map<Participante,Puntuacion> r=new LinkedHashMap<>();r.put(e.getA(),new Puntuacion(pa,a-b,a>b?1:0));r.put(e.getB(),new Puntuacion(pb,b-a,b>a?1:0));return r;}
 @Override public String toString(){return nombre+" ("+minimo+"-"+maximo+", "+Util.dinero(cuota)+")";}
}
abstract class DisciplinaEncuentros extends Disciplina {
 protected DisciplinaEncuentros(String nombre,int min,int max,int cuota){super(nombre,min,max,cuota);}
 @Override public final List<Evento> crearEventos(Torneo torneo){List<Evento> eventos=new ArrayList<>();List<Participante> p=torneo.getParticipantes();for(int i=0;i<p.size();i++)for(int j=i+1;j<p.size();j++)eventos.add(new Encuentro(torneo,p.get(i),p.get(j)));return eventos;}
 protected Encuentro encuentro(Evento e){return e.comoEncuentro();}
 protected Marcador marcadorEntrada(Resultado r){return r.comoMarcador();}
}
class FutbolSala extends DisciplinaEncuentros {
 @Override public Resultado leerResultado(Scanner sc,Evento e){return leerMarcador(sc);}
 FutbolSala(){this("Fútbol sala",600);}
 protected FutbolSala(String nombre,int cuota){super(nombre,4,8,cuota);}
 @Override public Map<Participante,Puntuacion> evaluar(Evento evento,Resultado resultado){Encuentro e=encuentro(evento);Marcador m=marcadorEntrada(resultado);return marcador(e,m.a,m.b,m.a>m.b?3:m.a==m.b?1:0,m.b>m.a?3:m.a==m.b?1:0);}
}
final class FutbolPenales extends FutbolSala {
 @Override public Resultado leerResultado(Scanner sc,Evento e){int a=leerInt(sc,"Goles A"),b=leerInt(sc,"Goles B");return a==b?new ConPenales(a,b,leerInt(sc,"Penales A"),leerInt(sc,"Penales B")):new ConPenales(a,b,null,null);}
 FutbolPenales(){super("Fútbol sala con penales",750);}
 @Override public Map<Participante,Puntuacion> evaluar(Evento evento,Resultado resultado){Encuentro e=encuentro(evento);ConPenales r=resultado.comoPenales();
  if(r.a!=r.b){Util.exigir(r.pa==null&&r.pb==null,"No se registran penales si los goles son distintos");return marcador(e,r.a,r.b,r.a>r.b?3:0,r.b>r.a?3:0);}
  Util.exigir(r.pa!=null&&r.pb!=null,"Empate requiere penales");Util.noNegativo(r.pa,"Penales A");Util.noNegativo(r.pb,"Penales B");Util.exigir(!r.pa.equals(r.pb),"Penales no pueden empatar");Map<Participante,Puntuacion> salida=new LinkedHashMap<>();salida.put(e.getA(),new Puntuacion(r.pa>r.pb?2:1,0,r.pa>r.pb?1:0));salida.put(e.getB(),new Puntuacion(r.pb>r.pa?2:1,0,r.pb>r.pa?1:0));return salida;
 }
}
final class Baloncesto extends DisciplinaEncuentros {
 @Override public Resultado leerResultado(Scanner sc,Evento e){return leerMarcador(sc);}
 Baloncesto(){super("Baloncesto",4,6,500);}
 @Override public Map<Participante,Puntuacion> evaluar(Evento evento,Resultado resultado){Encuentro e=encuentro(evento);Marcador m=marcadorEntrada(resultado);Util.exigir(m.a!=m.b,"Baloncesto no admite empate");return marcador(e,m.a,m.b,m.a>m.b?2:1,m.b>m.a?2:1);}
}
final class Voleibol extends DisciplinaEncuentros {
 @Override public Resultado leerResultado(Scanner sc,Evento e){return leerMarcador(sc);}
 Voleibol(){super("Voleibol",4,6,450);}
 @Override public Map<Participante,Puntuacion> evaluar(Evento evento,Resultado resultado){Encuentro e=encuentro(evento);Marcador m=marcadorEntrada(resultado);Util.exigir((m.a==3&&m.b<=2)||(m.b==3&&m.a<=2),"Voleibol exige ganador con 3 sets y perdedor de 0 a 2");return marcador(e,m.a,m.b,m.a==3?(m.b==2?2:3):(m.a==2?1:0),m.b==3?(m.a==2?2:3):(m.b==2?1:0));}
}
final class Relevos extends Disciplina {
 @Override public Resultado leerResultado(Scanner sc,Evento e){Map<Participante,Double> m=new LinkedHashMap<>();for(Participante p:e.getParticipantes()){System.out.print(p.getNombre()+" tiempo (D=descalificado): ");String t=sc.nextLine().trim();if(t.equalsIgnoreCase("D"))m.put(p,null);else{try{m.put(p,Double.parseDouble(t.replace(',','.')));}catch(NumberFormatException ex){throw new IllegalArgumentException("Tiempo no numérico");}}}return new RelevoResultado(m);}
 Relevos(){super("Relevos",3,8,300);}
 @Override public List<Evento> crearEventos(Torneo t){return Collections.singletonList(new Carrera(t,t.getParticipantes()));}
 @Override public Map<Participante,Puntuacion> evaluar(Evento evento,Resultado resultado){evento.comoCarrera();Map<Participante,Double> t=resultado.comoRelevo().tiempos;Util.exigir(t.keySet().equals(new HashSet<>(evento.getParticipantes())),"Debe incluir todos los participantes");Set<Double> usados=new HashSet<>();List<Map.Entry<Participante,Double>> validos=new ArrayList<>();
 for(Map.Entry<Participante,Double> x:t.entrySet())if(x.getValue()!=null){double v=x.getValue();Util.exigir(Double.isFinite(v)&&v>0,"Tiempo debe ser positivo y finito");Util.exigir(usados.add(v),"No se aceptan tiempos repetidos");validos.add(x);}
 validos.sort(Comparator.comparingDouble(Map.Entry::getValue));int[] puntos={10,8,6,5,4,3,2,1};Map<Participante,Puntuacion> r=new LinkedHashMap<>();for(Participante p:evento.getParticipantes())r.put(p,new Puntuacion(0,0,0));for(int i=0;i<validos.size();i++)r.put(validos.get(i).getKey(),new Puntuacion(puntos[i],0,i==0?1:0));return r;
 }
}
