package arenagt;
import java.time.*;
import java.util.*;
public final class Pruebas {
 private static int ok=0,fail=0;
 private static void verificar(String nombre,boolean condicion){if(condicion){ok++;System.out.println("OK | "+nombre);}else{fail++;System.out.println("FALLO | "+nombre);}}
 private static void rechazar(String nombre,Runnable r){boolean rechazado=false;try{r.run();}catch(IllegalArgumentException ex){rechazado=true;}verificar(nombre,rechazado);}
 private static boolean ordenado(Iterable<Evento> eventos){Evento anterior=null;for(Evento e:eventos){if(anterior!=null&&anterior.compareTo(e)>0)return false;anterior=e;}return true;}
 private static Sistema nuevo(){Sistema s=new Sistema();String[] empresas={"A","B","C","A","B","C","D","D"};for(int i=0;i<8;i++)s.crearParticipante("P"+i,"Equipo"+i,empresas[i]);return s;}
 private static Torneo torneo(Sistema s,int indice,int n){Torneo t=s.crearTorneo("T"+indice,"Torneo"+indice,indice);for(int i=0;i<n;i++)s.inscribirConCobro(t.getCodigo(),"P"+i);t.cerrar();return t;}
 private static Evento jugar(Sistema s,Torneo t,Resultado r){Evento e=t.getEventos().get(0);s.programar(e.getCodigo(),LocalDate.of(2027,1,1),8,"Sede "+t.getCodigo());s.resultado(e.getCodigo(),r);return e;}
 public static void main(String[] args){
 Sistema s=nuevo();verificar("Ingresos iniciales=0",s.getIngresos()==0);verificar("Premios iniciales=0",s.getPremios()==0);
 int[] tarifas={600,750,500,450,300};for(int i=1;i<=5;i++)verificar("Cotización disciplina "+i,s.cotizar(i,4)==4*tarifas[i-1]);
 verificar("Cotizar no cambia ingresos",s.getIngresos()==0);rechazar("Cotización cantidad cero",()->s.cotizar(1,0));rechazar("Cotización supera cupo",()->s.cotizar(4,7));
 for(int i=1;i<=5;i++){Sistema x=nuevo();Torneo t=torneo(x,i,i==5?3:4);verificar("Cobro disciplina "+i,x.getIngresos()==t.getParticipantes().size()*tarifas[i-1]);verificar("Torneo en juego "+i,t.getEstado()==Torneo.Estado.EN_JUEGO);verificar("Eventos disciplina "+i,t.getEventos().size()==(i==5?1:6));}
 {Sistema x=nuevo();Torneo t=torneo(x,1,4);Evento e=jugar(x,t,new Marcador(4,1));verificar("Fútbol 3-0 y diferencia",t.getTabla().get(0).getPuntos()==3&&t.getTabla().get(0).getDiferencia()==3);rechazar("Resultado no puede repetirse",()->x.resultado(e.getCodigo(),new Marcador(1,1)));}
 {Sistema x=nuevo();Torneo t=torneo(x,1,4);jugar(x,t,new Marcador(2,2));verificar("Fútbol empate 1-1",t.getTabla().get(0).getPuntos()==1&&t.getTabla().get(1).getPuntos()==1);}
 {Sistema x=nuevo();Torneo t=torneo(x,2,4);jugar(x,t,new ConPenales(1,1,4,3));verificar("Penales puntos 2 y 1",t.getTabla().get(0).getPuntos()==2&&t.getTabla().get(1).getPuntos()==1);verificar("Penales diferencia cero y victoria",t.getTabla().get(0).getDiferencia()==0&&t.getTabla().get(0).getVictorias()==1);}
 {Sistema x=nuevo();Torneo t=torneo(x,2,4);Evento e=t.getEventos().get(0);x.programar(e.getCodigo(),LocalDate.of(2027,1,1),8,"Sede");rechazar("Penales iguales",()->x.resultado(e.getCodigo(),new ConPenales(0,0,2,2)));rechazar("Empate sin penales",()->x.resultado(e.getCodigo(),new ConPenales(0,0,null,null)));rechazar("Penales no permitidos con goles distintos",()->x.resultado(e.getCodigo(),new ConPenales(1,0,3,2)));verificar("Rechazos conservan evento",!e.isFinalizado()&&t.getTabla().stream().allMatch(a->a.getPuntos()==0));}
 {Sistema x=nuevo();Torneo t=torneo(x,3,4);Evento e=t.getEventos().get(0);x.programar(e.getCodigo(),LocalDate.of(2027,1,1),8,"Sede");rechazar("Baloncesto empate",()->x.resultado(e.getCodigo(),new Marcador(70,70)));verificar("Sin cambio baloncesto",t.getTabla().stream().allMatch(a->a.getPuntos()==0));x.resultado(e.getCodigo(),new Marcador(70,60));verificar("Baloncesto ganador 2 perdedor 1",t.getTabla().get(0).getPuntos()==2&&t.getTabla().get(1).getPuntos()==1);}
 for(int perdedor=0;perdedor<=2;perdedor++){Sistema x=nuevo();Torneo t=torneo(x,4,4);jugar(x,t,new Marcador(3,perdedor));int p=perdedor==2?2:3;verificar("Voleibol 3-"+perdedor,t.getTabla().get(0).getPuntos()==p&&t.getTabla().stream().mapToInt(Torneo.Estadistica::getPuntos).sum()==(perdedor==2?3:3));}
 {Sistema x=nuevo();Torneo t=torneo(x,4,4);Evento e=t.getEventos().get(0);x.programar(e.getCodigo(),LocalDate.of(2027,1,1),8,"Sede");rechazar("Voleibol inválido",()->x.resultado(e.getCodigo(),new Marcador(2,2)));}
 {Sistema x=nuevo();Torneo t=torneo(x,5,4);Evento e=t.getEventos().get(0);x.programar(e.getCodigo(),LocalDate.of(2027,1,1),8,"Pista");Map<Participante,Double> m=new LinkedHashMap<>();for(int i=0;i<4;i++)m.put(t.getParticipantes().get(i),50.0+i);m.put(t.getParticipantes().get(3),null);x.resultado(e.getCodigo(),new RelevoResultado(m));verificar("Relevos 10,8,6,0",t.getTabla().get(0).getPuntos()==10&&t.getTabla().get(1).getPuntos()==8&&t.getTabla().get(2).getPuntos()==6&&t.getTabla().get(3).getPuntos()==0);verificar("Relevos victoria y diferencia",t.getTabla().get(0).getVictorias()==1&&t.getTabla().get(0).getDiferencia()==0);verificar("Finaliza automáticamente",t.getEstado()==Torneo.Estado.FINALIZADO);verificar("Premio relevo",t.getPremio()==600);}
 {Sistema x=nuevo();Torneo t=torneo(x,5,3);Evento e=t.getEventos().get(0);x.programar(e.getCodigo(),LocalDate.of(2027,1,1),8,"Pista");Map<Participante,Double> m=new LinkedHashMap<>();m.put(t.getParticipantes().get(0),50.0);m.put(t.getParticipantes().get(1),50.0);m.put(t.getParticipantes().get(2),52.0);rechazar("Relevos tiempos repetidos",()->x.resultado(e.getCodigo(),new RelevoResultado(m)));verificar("Rechazo no cambia premio",t.getPremio()==0&&x.getPremios()==0&&!e.isFinalizado());}
 {Sistema x=nuevo();Torneo t=torneo(x,1,4);Evento e=t.getEventos().get(0),e2=t.getEventos().get(1);LocalDate f=LocalDate.of(2027,1,1);rechazar("Resultado sin programación",()->x.resultado(e.getCodigo(),new Marcador(2,1)));x.programar(e.getCodigo(),f,8,"Norte");rechazar("Choque sede",()->x.programar(e2.getCodigo(),f,8,"Norte"));rechazar("Choque participante",()->x.programar(e2.getCodigo(),f,8,"Sur"));verificar("Sin cambios en conflicto",!e2.isProgramado());x.programar(e2.getCodigo(),f,9,"Norte");verificar("Calendario ordenado",x.calendario().iterator().next()==e);}
 {Sistema x=nuevo();Torneo t=x.crearTorneo("T","T",1);for(int i=0;i<3;i++)x.inscribirConCobro("T","P"+i);rechazar("Cierre bajo mínimo",()->x.cerrar("T"));verificar("Inscripción preservada",t.getEstado()==Torneo.Estado.INSCRIPCION);double dinero=x.getIngresos();rechazar("Inscrito duplicado",()->x.inscribirConCobro("T","P0"));verificar("Ingreso no cambia por rechazo",x.getIngresos()==dinero);}
 {Sistema x=nuevo();Torneo t=torneo(x,1,4);verificar("Ranking empresas muestra todas",x.rankingEmpresas().size()==4);verificar("Cobros por disciplina con cero",x.cobros().size()==5&&x.cobros().get("Relevos")==0.0);}

 // Todos los encuentros completados y premio automatico para cada disciplina.
 for(int d=1;d<=4;d++){
  Sistema x=nuevo();Torneo t=torneo(x,d,4);int j=0;
  for(Evento e:t.getEventos()){
   x.programar(e.getCodigo(),LocalDate.of(2027,3,1).plusDays(j),8,"Sede"+d);
   Resultado r=d==2?new ConPenales(2,1,null,null):d==4?new Marcador(3,1):new Marcador(4,1);
   x.resultado(e.getCodigo(),r);j++;
  }
  verificar("Premio y finalización disciplina "+d,t.getEstado()==Torneo.Estado.FINALIZADO&&t.getPremio()==4*new int[]{0,600,750,500,450}[d]*0.5&&x.getPremios()==t.getPremio()&&t.getCampeon()!=null);
 }
 {Sistema x=nuevo();Torneo t=torneo(x,1,4);List<Evento> es=t.getEventos();
  x.programar(es.get(0).getCodigo(),LocalDate.of(2027,4,1),8,"A");x.resultado(es.get(0).getCodigo(),new Marcador(3,1));
  x.programar(es.get(1).getCodigo(),LocalDate.of(2027,4,2),8,"A");x.resultado(es.get(1).getCodigo(),new Marcador(1,0));
  verificar("Desempate por diferencia con puntos iguales",t.getTabla().get(0).getParticipante().getCodigo().equals("P0"));
 }
 {Sistema x=nuevo();Torneo t=torneo(x,1,4);Evento e=t.getEventos().get(0);LocalDate fecha=LocalDate.of(2027,4,1);
  x.programar(e.getCodigo(),fecha,8,"A");x.programar(e.getCodigo(),fecha,11,"B");verificar("Reprogramación permitida",e.getHora()==11&&e.getSede().equals("B"));
  x.resultado(e.getCodigo(),new Marcador(1,0));rechazar("Reprogramar finalizado",()->x.programar(e.getCodigo(),fecha,12,"B"));
 }
 {Sistema x=nuevo();Torneo a=torneo(x,1,4);Torneo b=torneo(x,3,4);Evento ea=a.getEventos().get(0),eb=b.getEventos().get(0);LocalDate fecha=LocalDate.of(2027,4,1);
  x.programar(ea.getCodigo(),fecha,9,"Gimnasio");rechazar("Choque de participante entre torneos",()->x.programar(eb.getCodigo(),fecha,9,"Otra sede"));
  rechazar("Choque de sede entre torneos",()->x.programar(eb.getCodigo(),fecha,9,"Gimnasio"));
 }
 {Sistema x=nuevo();Torneo t=x.crearTorneo("ABC","Copa",1);x.inscribirConCobro("ABC","P0");x.inscribirConCobro("ABC","P1");x.inscribirConCobro("ABC","P3");x.crearParticipante("P8","Equipo8","A");double antes=x.getIngresos();rechazar("Máximo dos por empresa",()->x.inscribirConCobro("ABC","P8"));verificar("No hubo cobro al rechazar tercera empresa",x.getIngresos()==antes);}

 // Verificaciones de regresión frente a descuentos anteriores (estados, cobros, desempates y reportes).
 {Sistema x=nuevo();rechazar("Código de participante vacío",()->x.crearParticipante("  ","Nombre","Empresa"));
  rechazar("Código de participante repetido",()->x.crearParticipante("P0","Otro","Empresa"));
  rechazar("Código de torneo vacío",()->x.crearTorneo(" ","Copa",1));
  x.crearTorneo("T","Copa",1);rechazar("Código de torneo repetido",()->x.crearTorneo("T","Otra",1));
  rechazar("Disciplina fuera de rango",()->x.cotizar(6,4));
  verificar("Duplicados no generan objetos extra",x.getParticipantes().size()==8&&x.getTorneos().size()==1);
 }
 {Sistema x=nuevo();Torneo t=x.crearTorneo("T","Copa",1);double total=x.getIngresos();
  x.inscribir("T","P0");verificar("Ruta directa de inscripción actualiza ingreso y reporte",x.getIngresos()==total+600&&x.cobros().get("Fútbol sala")==600);
  double antes=x.getIngresos();rechazar("Inscripción repetida no cobra",()->x.inscribir("T","P0"));
  verificar("Ingreso y desglose intactos al rechazar",x.getIngresos()==antes&&x.cobros().get("Fútbol sala")==600);
  verificar("Desglose por disciplina concilia con total",x.cobros().values().stream().mapToDouble(Double::doubleValue).sum()==x.getIngresos());
 }
 {Sistema x=nuevo();Torneo t=torneo(x,1,4);Evento e=t.getEventos().get(0);LocalDate f=LocalDate.of(2027,2,2);
  rechazar("Hora negativa",()->x.programar(e.getCodigo(),f,-1,"Sur"));
  rechazar("Hora mayor que 23",()->x.programar(e.getCodigo(),f,24,"Sur"));
  rechazar("Sede vacía",()->x.programar(e.getCodigo(),f,12," "));
  verificar("Programaciones inválidas no alteran evento",!e.isProgramado());
  x.programar(e.getCodigo(),f,12,"Sur");rechazar("Marcador negativo",()->x.resultado(e.getCodigo(),new Marcador(-1,0)));
  verificar("Marcador inválido no altera tabla ni estado",!e.isFinalizado()&&t.getTabla().stream().allMatch(p->p.getPuntos()==0));
 }
 {Sistema x=nuevo();Torneo t=torneo(x,5,3);Evento e=t.getEventos().get(0);x.programar(e.getCodigo(),LocalDate.of(2027,1,1),12,"Pista");
  Map<Participante,Double> m=new LinkedHashMap<>();m.put(t.getParticipantes().get(0),Double.NaN);m.put(t.getParticipantes().get(1),55.0);m.put(t.getParticipantes().get(2),null);
  rechazar("Tiempo NaN inválido",()->x.resultado(e.getCodigo(),new RelevoResultado(m)));
  m.put(t.getParticipantes().get(0),0.0);rechazar("Tiempo cero inválido",()->x.resultado(e.getCodigo(),new RelevoResultado(m)));
  m.put(t.getParticipantes().get(0),50.0);m.remove(t.getParticipantes().get(1));rechazar("Relevos incompletos",()->x.resultado(e.getCodigo(),new RelevoResultado(m)));
  verificar("Relevos rechazados no pagan premio",x.getPremios()==0&&!e.isFinalizado());
 }
 {Sistema x=nuevo();Torneo t=torneo(x,1,4);verificar("Desempate alfabético con igualdad total",t.getTabla().get(0).getParticipante().getNombre().equals("Equipo0"));}
 {Sistema x=nuevo();Torneo t=torneo(x,1,4);List<Evento> es=t.getEventos();
  // 0=(P0,P1),1=(P0,P2); P0 obtiene 3 puntos y diferencia 0 al ganar 2-0 y perder 0-2.
  x.programar(es.get(0).getCodigo(),LocalDate.of(2027,4,1),8,"A");x.resultado(es.get(0).getCodigo(),new Marcador(2,0));
  x.programar(es.get(1).getCodigo(),LocalDate.of(2027,4,2),8,"A");x.resultado(es.get(1).getCodigo(),new Marcador(0,2));
  verificar("Tabla acumula correctamente victorias y diferencia",t.getTabla().stream().anyMatch(a->a.getParticipante().getCodigo().equals("P0")&&a.getPuntos()==3&&a.getDiferencia()==0&&a.getVictorias()==1));
 }
 {Sistema x=new Sistema();x.cargarDemo();verificar("Demo tiene 8 participantes y cinco disciplinas",x.getParticipantes().size()==8&&x.getTorneos().size()==5);
  verificar("Demo sin ingresos ni premios contabilizados",x.getIngresos()==0&&x.getPremios()==0&&x.cobros().values().stream().allMatch(v->v==0));
  verificar("Demo programa todos los eventos en juego",x.getTorneos().stream().filter(t->t.getEstado()==Torneo.Estado.EN_JUEGO).allMatch(t->t.getEventos().stream().allMatch(Evento::isProgramado)));
  Torneo tf=x.torneo("T1");List<Torneo.Estadistica> tabla=tf.getTabla();
  verificar("Demo desempate por diferencia",tabla.stream().anyMatch(a->a.getPuntos()==4&&a.getDiferencia()==1)&&tabla.stream().anyMatch(a->a.getPuntos()==4&&a.getDiferencia()==0));
  int ia=-1,ib=-1;for(int i=0;i<tabla.size();i++){String cod=tabla.get(i).getParticipante().getCodigo();if(cod.equals("P1"))ia=i;if(cod.equals("P2"))ib=i;}
  verificar("Demo desempate por victorias",ia>=0&&ib>=0&&ia<ib&&tabla.get(ia).getPuntos()==tabla.get(ib).getPuntos()&&tabla.get(ia).getDiferencia()==tabla.get(ib).getDiferencia()&&tabla.get(ia).getVictorias()>tabla.get(ib).getVictorias());
  verificar("Demo calendario general cronológico",ordenado(x.calendario()));
  verificar("Demo calendario por torneo cronológico",ordenado(x.calendarioTorneo("T1")));
  verificar("Demo calendario por participante cronológico",ordenado(x.calendarioParticipante("P1")));
 }

 // Pruebas de regresión del despacho polimórfico y valores concretos cobrados.
 {Sistema x=nuevo();int[] cuotas={600,750,500,450,300};
  for(int d=1;d<=5;d++){final int k=d;int cuota=cuotas[d-1];verificar("Cotización exacta "+d,x.cotizar(d,4)==4*cuota);}
  verificar("Cotizar cinco disciplinas no cobra",x.getIngresos()==0&&x.cobros().values().stream().allMatch(v->v==0));
  rechazar("Cotización cero",()->x.cotizar(1,0));rechazar("Cotización excede cupo",()->x.cotizar(3,7));
  verificar("Cotizaciones rechazadas no cobran",x.getIngresos()==0);
 }
 {Sistema x=nuevo();Torneo t=torneo(x,1,4);Evento e=t.getEventos().get(0);
  x.programar(e.getCodigo(),LocalDate.of(2027,5,1),12,"Sede");
  rechazar("Marcador recibido en relevo no válido",()->new Relevos().evaluar(e,new Marcador(2,1)));
  rechazar("Resultado de relevos recibido en fútbol no válido",()->x.resultado(e.getCodigo(),new RelevoResultado(Collections.emptyMap())));
  verificar("Tipos no compatibles preservan tabla",!e.isFinalizado()&&t.getTabla().stream().allMatch(z->z.getPuntos()==0));
  x.resultado(e.getCodigo(),new Marcador(2,1));verificar("Recuperación tras rechazo",e.isFinalizado()&&t.getTabla().get(0).getPuntos()==3);
 }
 {Sistema x=nuevo();Torneo t=torneo(x,5,3);Evento e=t.getEventos().get(0);x.programar(e.getCodigo(),LocalDate.of(2027,5,2),7,"Pista");
  Map<Participante,Double> tiempos=new LinkedHashMap<>();tiempos.put(t.getParticipantes().get(0),null);tiempos.put(t.getParticipantes().get(1),54.2);tiempos.put(t.getParticipantes().get(2),57.3);
  x.resultado(e.getCodigo(),new RelevoResultado(tiempos));
  verificar("Descalificado con cero puntos",t.getTabla().stream().anyMatch(z->z.getParticipante()==t.getParticipantes().get(0)&&z.getPuntos()==0));
  verificar("Ranking relevos 10 y 8",t.getTabla().get(0).getPuntos()==10&&t.getTabla().get(1).getPuntos()==8);
  verificar("Campeón relevo primer tiempo",t.getCampeon()==t.getParticipantes().get(1));
 }
 System.out.println("RESULTADO: "+ok+" correctas; "+fail+" fallidas");if(fail>0)System.exit(1);
 }
}
