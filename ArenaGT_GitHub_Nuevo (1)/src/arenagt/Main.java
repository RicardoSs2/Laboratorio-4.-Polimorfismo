package arenagt;
import java.util.*;
import java.time.*;
public final class Main {
 private final Sistema s=new Sistema();private final Scanner in=new Scanner(System.in);
 public static void main(String[] args){new Main().ejecutar();}
 private String pedir(String texto){System.out.print(texto+": ");if(!in.hasNextLine())throw new NoSuchElementException("Entrada cerrada");return in.nextLine().trim();}
 private int entero(String texto){try{return Integer.parseInt(pedir(texto));}catch(NumberFormatException ex){throw new IllegalArgumentException("Ingrese un entero válido");}}
 private double decimal(String texto){try{return Double.parseDouble(pedir(texto).replace(',','.'));}catch(NumberFormatException ex){throw new IllegalArgumentException("Ingrese un decimal válido");}}
 private void disciplinas(){for(int i=0;i<s.getDisciplinas().size();i++)System.out.println((i+1)+". "+s.getDisciplinas().get(i));}
 private void imprimir(Iterable<Evento> c){boolean hay=false;for(Evento e:c){System.out.println(e);hay=true;}if(!hay)System.out.println("No hay eventos programados.");}
 private void tabla(Torneo t){System.out.println(t);for(Torneo.Estadistica e:t.getTabla())System.out.println("  "+e);if(t.getCampeon()!=null)System.out.println("Campeón: "+t.getCampeon()+"; premio: "+Util.dinero(t.getPremio()));}
 private void ejecutar(){s.cargarDemo();System.out.println("ArenaGT - gestión deportiva (datos precargados; sin ingresos iniciales)");while(true){try{
 System.out.println("\n1 Participante | 2 Torneo | 3 Cotización | 4 Inscripción | 5 Cerrar | 6 Eventos | 7 Programar/reprogramar | 8 Resultado | 9 Tablas | 10 Calendarios | 11 Reportes | 12 Consultar participantes | 0 Salir");
 int op=entero("Opción");switch(op){
 case 0:System.out.println("Hasta luego");return;
 case 1:System.out.println("Creado: "+s.crearParticipante(pedir("Código"),pedir("Nombre"),pedir("Empresa")));break;
 case 2:disciplinas();System.out.println("Creado: "+s.crearTorneo(pedir("Código"),pedir("Nombre"),entero("Número de disciplina")));break;
 case 3:disciplinas();System.out.println("Cotización: "+Util.dinero(s.cotizar(entero("Disciplina"),entero("Cantidad"))));break;
 case 4:s.inscribirConCobro(pedir("Código torneo"),pedir("Código participante"));System.out.println("Inscripción cobrada");break;
 case 5:s.cerrar(pedir("Código torneo"));System.out.println("Inscripciones cerradas; eventos generados");break;
 case 6:for(Torneo t:s.getTorneos()){System.out.println(t);for(Evento e:t.getEventos())System.out.println("  "+e);}break;
 case 7:String id=pedir("Código evento");LocalDate fecha=LocalDate.parse(pedir("Fecha YYYY-MM-DD"));s.programar(id,fecha,entero("Hora (0-23)"),pedir("Sede"));System.out.println("Programación confirmada");break;
 case 8:Evento e=s.evento(pedir("Código evento"));System.out.println(e);s.resultado(e.getCodigo(),e.getTorneo().getDisciplina().leerResultado(in,e));System.out.println("Resultado registrado");break;
 case 9:for(Torneo t:s.getTorneos())tabla(t);break;
 case 10:System.out.println("1 general, 2 torneo, 3 participante");int v=entero("Tipo");switch(v){case 1:imprimir(s.calendario());break;case 2:imprimir(s.calendarioTorneo(pedir("Torneo")));break;case 3:imprimir(s.calendarioParticipante(pedir("Participante")));break;default:throw new IllegalArgumentException("Tipo no válido");}break;
 case 11:System.out.println("Ingresos: "+Util.dinero(s.getIngresos())+" | Premios: "+Util.dinero(s.getPremios()));for(Map.Entry<String,Double> x:s.cobros().entrySet())System.out.println(x.getKey()+": "+Util.dinero(x.getValue()));System.out.println("Ranking de empresas:");for(Map.Entry<String,Integer> x:s.rankingEmpresas().entrySet())System.out.println(x.getKey()+" | "+x.getValue());break;
 case 12:for(Participante p:s.getParticipantes())System.out.println(p);break;
 default:System.out.println("Opción no reconocida");
 }
 }catch(NoSuchElementException ex){System.out.println("Entrada finalizada");return;}catch(RuntimeException ex){System.out.println("Operación rechazada: "+ex.getMessage());}}
 }
}
