package arenagt;
import java.time.LocalDate;
import java.util.*;
public final class Sistema {
 private final Map<String,Participante> participantes=new LinkedHashMap<>();private final Map<String,Torneo> torneos=new LinkedHashMap<>();
 private final List<Disciplina> disciplinas=Collections.unmodifiableList(Arrays.asList(new FutbolSala(),new FutbolPenales(),new Baloncesto(),new Voleibol(),new Relevos()));
 private double ingresos;
 public List<Disciplina> getDisciplinas(){return disciplinas;}public Collection<Participante> getParticipantes(){return Collections.unmodifiableCollection(participantes.values());}public Collection<Torneo> getTorneos(){return Collections.unmodifiableCollection(torneos.values());}
 public Participante participante(String codigo){Participante p=participantes.get(Util.texto(codigo,"Código"));Util.exigir(p!=null,"Participante no encontrado");return p;}
 public Torneo torneo(String codigo){Torneo t=torneos.get(Util.texto(codigo,"Código"));Util.exigir(t!=null,"Torneo no encontrado");return t;}
 public Evento evento(String codigo){for(Torneo t:torneos.values())for(Evento e:t.getEventos())if(e.getCodigo().equalsIgnoreCase(codigo))return e;throw new IllegalArgumentException("Evento no encontrado");}
 public Participante crearParticipante(String codigo,String nombre,String empresa){Participante p=new Participante(codigo,nombre,empresa);Util.exigir(!participantes.containsKey(p.getCodigo()),"Código de participante repetido");participantes.put(p.getCodigo(),p);return p;}
 public Torneo crearTorneo(String codigo,String nombre,int indice){Util.exigir(indice>=1&&indice<=disciplinas.size(),"Disciplina inválida");Torneo t=new Torneo(codigo,nombre,disciplinas.get(indice-1));Util.exigir(!torneos.containsKey(t.getCodigo()),"Código de torneo repetido");torneos.put(t.getCodigo(),t);return t;}
 public double cotizar(int indice,int cantidad){Util.exigir(indice>=1&&indice<=disciplinas.size(),"Disciplina inválida");return disciplinas.get(indice-1).cotizar(cantidad);}
 public void inscribir(String torneo,String participante){Torneo t=torneo(torneo);t.inscribir(participante(participante));double monto=t.getDisciplina().getCuota();ingresos+=monto;cobrosPorDisciplina.merge(t.getDisciplina().getNombre(),monto,Double::sum);}
 public void cerrar(String codigo){torneo(codigo).cerrar();}
 public void programar(String id,LocalDate fecha,int hora,String sede){Evento e=evento(id);Util.exigir(e.getTorneo().getEstado()==Torneo.Estado.EN_JUEGO,"El torneo debe estar en juego");Util.exigir(!e.isFinalizado(),"Evento ya finalizado");Objects.requireNonNull(fecha,"Fecha obligatoria");Util.exigir(hora>=0&&hora<24,"Hora fuera de rango");sede=Util.texto(sede,"Sede");
 for(Evento otro:todosEventos())if(otro!=e&&otro.isProgramado()&&otro.getFecha().equals(fecha)&&otro.getHora()==hora){Util.exigir(!otro.getSede().equalsIgnoreCase(sede),"Conflicto de sede con "+otro.getCodigo());for(Participante p:e.getParticipantes())Util.exigir(!otro.getParticipantes().contains(p),"Participante ocupado: "+p.getNombre());}
 e.programar(fecha,hora,sede);
 }
 public void resultado(String codigo,Resultado r){Evento e=evento(codigo);e.getTorneo().registrar(e,r);}
 public List<Evento> todosEventos(){List<Evento> out=new ArrayList<>();for(Torneo t:torneos.values())out.addAll(t.getEventos());return out;}
 public Calendario calendario(){return new Calendario(todosEventos());}
 public Calendario calendarioTorneo(String id){return new Calendario(torneo(id).getEventos());}
 public Calendario calendarioParticipante(String codigo){Participante p=participante(codigo);List<Evento> out=new ArrayList<>();for(Evento e:todosEventos())if(e.getParticipantes().contains(p))out.add(e);return new Calendario(out);}
 public double getIngresos(){return ingresos;}
 public double getPremios(){double x=0;for(Torneo t:torneos.values())x+=t.getPremio();return x;}
 // Los inscritos precargados no deben contarse como ingresos: se lleva un libro independiente.
 private final Map<String,Double> cobrosPorDisciplina=new HashMap<>();
 public Map<String,Double> cobros(){Map<String,Double> r=new LinkedHashMap<>();for(Disciplina d:disciplinas)r.put(d.getNombre(),cobrosPorDisciplina.getOrDefault(d.getNombre(),0.0));return r;}
 public Map<String,Integer> rankingEmpresas(){Map<String,Integer> totals=new HashMap<>();for(Participante p:participantes.values())totals.putIfAbsent(p.getEmpresa(),0);for(Torneo t:torneos.values())for(Torneo.Estadistica e:t.getTabla())totals.merge(e.getParticipante().getEmpresa(),e.getPuntos(),Integer::sum);List<String> nombres=new ArrayList<>(totals.keySet());nombres.sort((a,b)->{int c=Integer.compare(totals.get(b),totals.get(a));return c!=0?c:a.compareToIgnoreCase(b);});Map<String,Integer> sorted=new LinkedHashMap<>();for(String n:nombres)sorted.put(n,totals.get(n));return sorted;}
 public void inscribirConCobro(String torneo,String participante){inscribir(torneo,participante);}
 public void cargarDemo(){String[][] p={{"P1","Águilas","Alfa"},{"P2","Halcones","Beta"},{"P3","Tigres","Gamma"},{"P4","Leones","Alfa"},{"P5","Toros","Beta"},{"P6","Lobos","Gamma"},{"P7","Pumas","Delta"},{"P8","Cóndores","Delta"}};for(String[] x:p)crearParticipante(x[0],x[1],x[2]);
 for(int d=1;d<=5;d++){Torneo t=crearTorneo("T"+d,"Copa Demo "+d,d);int n=(d==5||d==1)?5:4;for(int i=1;i<=n;i++)t.inscribir(participante("P"+i));if(d==1||d==2||d==3||d==5)t.cerrar();}
 try{Torneo futbol=torneo("T1");
 // Orden de encuentros: P1-P2, P1-P3, P1-P4, P2-P3, P2-P4, P3-P4.
 // P1 y P2 acumulan 4 puntos y diferencia cero; P1 gana por victorias (1 contra 0).
 int[][] goles={{1,1},{2,0},{0,1},{0,1},{1,1},{0,0},{2,2}};
 for(int i=0;i<goles.length;i++){Evento ev=futbol.getEventos().get(i);programar(ev.getCodigo(),LocalDate.of(2026,12,1).plusDays(i),9,"Cancha Norte");resultado(ev.getCodigo(),new Marcador(goles[i][0],goles[i][1]));}
 Torneo penal=torneo("T2");Evento ep=penal.getEventos().get(0);programar(ep.getCodigo(),LocalDate.of(2026,12,1),10,"Cancha Sur");resultado(ep.getCodigo(),new ConPenales(1,1,4,3));
 Torneo balon=torneo("T3");Evento eb=balon.getEventos().get(0);programar(eb.getCodigo(),LocalDate.of(2026,12,1),11,"Gimnasio");resultado(eb.getCodigo(),new Marcador(70,60));Evento eb2=balon.getEventos().get(5);programar(eb2.getCodigo(),LocalDate.of(2026,12,2),11,"Gimnasio");resultado(eb2.getCodigo(),new Marcador(70,67));
 Torneo relevos=torneo("T5");Evento er=relevos.getEventos().get(0);programar(er.getCodigo(),LocalDate.of(2026,12,3),8,"Pista");
 // Todos los eventos de torneos en juego quedan programados; los resultados de muestra se conservan.
 int diaPendiente=0;for(Torneo t:torneos.values())if(t.getEstado()==Torneo.Estado.EN_JUEGO)for(Evento evento:t.getEventos())if(!evento.isProgramado())programar(evento.getCodigo(),LocalDate.of(2027,2,1).plusDays(diaPendiente++),9,"Sede Demo");
 }catch(RuntimeException ex){throw new IllegalStateException("Error en datos iniciales",ex);}
 }
}
