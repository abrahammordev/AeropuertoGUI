package Interfaces;

import java.util.SortedSet;

public interface Aeropuerto {

    String getNombre();

    String getCiudad();

    SortedSet<Vuelo> getVuelos();

}