package arenagt;
import java.util.*;
public final class Torneo {
 public enum Estado {INSCRIPCION,EN_JUEGO,FINALIZADO}
 private final String codigo,nombre;private final Disciplina disciplina;
 private final List<Participante> participantes=new ArrayList<>();private final List<Evento> eventos=new ArrayList<>();private final Map<Participante,Estadistica> tabla=new LinkedHashMap<>();
 private Estado estado=Estado.INSCRIPCION;private double premio;
 public Torneo(String codigo,String nombre,Disciplina disciplina){this.codigo=Util.texto(codigo,"Código torneo");this.nombre=Util.texto(nombre,"Nombre torneo");this.disciplina=Objects.requireNonNull(disciplina);}
 public String getCodigo(){return codigo;}public String getNombre(){return nombre;}public Disciplina getDisciplina(){return disciplina;}public Estado getEstado(){return estado;}public double getPremio(){return premio;}
 public List<Participante> getParticipantes(){return Collections.unmodifiableList(participantes);}public List<Evento> getEventos(){return Collections.unmodifiableList(eventos);}
 void inscribir(Participante p){Objects.requireNonNull(p);Util.exigir(estado==Estado.INSCRIPCION,"Inscripciones cerradas");Util.exigir(participantes.size()<disciplina.getMaximo(),"Cupo lleno");Util.exigir(!participantes.contains(p),"Participante ya inscrito");long empresa=participantes.stream().filter(x->x.getEmpresa().equalsIgnoreCase(p.getEmpresa())).count();Util.exigir(empresa<2,"Máximo dos participantes por empresa");participantes.add(p);tabla.put(p,new Estadistica(p));}
 public void cerrar(){Util.exigir(estado==Estado.INSCRIPCION,"Torneo no está en inscripción");Util.exigir(participantes.size()>=disciplina.getMinimo(),"Faltan participantes para cerrar");List<Evento> creados=disciplina.crearEventos(this);Util.exigir(!creados.isEmpty(),"Debe haber eventos");eventos.addAll(creados);estado=Estado.EN_JUEGO;}
 public void registrar(Evento e,Resultado r){Util.exigir(estado==Estado.EN_JUEGO,"Torneo no está en juego");Util.exigir(eventos.contains(e),"Evento ajeno al torneo");Util.exigir(e.isProgramado(),"Debe programar el evento");Util.exigir(!e.isFinalizado(),"Resultado ya registrado");Map<Participante,Puntuacion> cambios=disciplina.evaluar(e,r);
 Util.exigir(cambios.keySet().equals(new HashSet<>(e.getParticipantes())),"Puntuación incompleta");for(Puntuacion p:cambios.values())Util.exigir(p.puntos>=0&&p.victorias>=0,"Puntuación inválida");
 // Todos los cambios se aplican solo después de validar el resultado completo.
 for(Map.Entry<Participante,Puntuacion> x:cambios.entrySet())tabla.get(x.getKey()).sumar(x.getValue());e.finalizar();if(eventos.stream().allMatch(Evento::isFinalizado)){estado=Estado.FINALIZADO;premio=disciplina.getCuota()*participantes.size()*0.5;}
 }
 public List<Estadistica> getTabla(){List<Estadistica> salida=new ArrayList<>();for(Estadistica e:tabla.values())salida.add(e.copia());Collections.sort(salida);return salida;}
 public Participante getCampeon(){return estado==Estado.FINALIZADO?getTabla().get(0).participante:null;}
 public String toString(){return codigo+" | "+nombre+" | "+disciplina.getNombre()+" | "+estado+" | Inscritos: "+participantes.size();}
 public static final class Estadistica implements Comparable<Estadistica> {
  private final Participante participante;private int puntos,diferencia,victorias;
  private Estadistica(Participante p){participante=p;}
  private void sumar(Puntuacion p){puntos+=p.puntos;diferencia+=p.diferencia;victorias+=p.victorias;}
  private Estadistica copia(){Estadistica e=new Estadistica(participante);e.puntos=puntos;e.diferencia=diferencia;e.victorias=victorias;return e;}
  public Participante getParticipante(){return participante;}public int getPuntos(){return puntos;}public int getDiferencia(){return diferencia;}public int getVictorias(){return victorias;}
  @Override public int compareTo(Estadistica e){int c=Integer.compare(e.puntos,puntos);if(c!=0)return c;c=Integer.compare(e.diferencia,diferencia);if(c!=0)return c;c=Integer.compare(e.victorias,victorias);if(c!=0)return c;return participante.getNombre().compareToIgnoreCase(e.participante.getNombre());}
  @Override public String toString(){return participante.getNombre()+" | pts="+puntos+" | dif="+diferencia+" | victorias="+victorias;}
 }
}
