package Interfaces;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

public interface Vuelo {

	public String getCodigo();

	public String getOrigen();

	public String getDestino();

	public LocalDateTime getFechaSalida();

	public void setFechaSalida(LocalDateTime fechaSalida);

	public LocalDateTime getFechaLlegada();

	public void setFechaLlegada(LocalDateTime fechaLlegada);

	public Duration getDuracion();

	public Integer getNumPlazas();

	public Boolean isCompleto();

	public Set<Persona> getPasajeros();

	public Integer getNumPasajeros();

	public void nuevoPasajero(Persona p);

	public void eliminarPasajeros(Persona p);

	public String toString();

}
