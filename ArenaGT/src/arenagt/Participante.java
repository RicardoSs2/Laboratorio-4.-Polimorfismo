package arenagt;
import java.util.Objects;
public final class Participante {
    private final String codigo,nombre,empresa;
    public Participante(String codigo,String nombre,String empresa){this.codigo=Util.texto(codigo,"Código");this.nombre=Util.texto(nombre,"Nombre");this.empresa=Util.texto(empresa,"Empresa");}
    public String getCodigo(){return codigo;} public String getNombre(){return nombre;} public String getEmpresa(){return empresa;}
    @Override public boolean equals(Object o){return o==this || (o!=null && o.getClass()==getClass() && codigo.equals(((Participante)o).codigo));}
    @Override public int hashCode(){return Objects.hash(codigo);}
    @Override public String toString(){return codigo+" - "+nombre+" ("+empresa+")";}
}
