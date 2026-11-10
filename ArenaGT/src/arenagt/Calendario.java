package arenagt;
import java.util.*;
public final class Calendario implements Iterable<Evento> {
 private final List<Evento> eventos;
 public Calendario(Collection<Evento> fuente){eventos=new ArrayList<>();for(Evento e:fuente)if(e.isProgramado())eventos.add(e);Collections.sort(eventos);}
 @Override public Iterator<Evento> iterator(){return Collections.unmodifiableList(eventos).iterator();}
 public int size(){return eventos.size();}
}
