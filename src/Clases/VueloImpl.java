package Clases;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import Interfaces.Persona;
import Interfaces.Vuelo;

public class VueloImpl implements Vuelo {

	// ------ATRIBUTOS

	protected String codigo;
	protected String origen;
	protected String destino;
	protected LocalDateTime fechaSalida;
	protected LocalDateTime fechaLlegada;
	protected Duration duracion;
	protected Integer numPlazas;
	protected Boolean completo;
	protected Set<Persona> pasajeros;
	protected Integer numPasajeros;

	// ------CONSTRUCTORES

	public VueloImpl(String codigo, String origen, String destino, LocalDateTime fechaSalida,
			LocalDateTime fechaLlegada, Duration duracion, Boolean completo, Set<Persona> pasajeros,
			Integer numPlazas) {
		this.codigo = codigo;
		this.origen = origen;
		this.destino = destino;
		this.fechaSalida = fechaSalida;
		this.fechaLlegada = fechaLlegada;
		this.duracion = duracion;
		this.numPlazas = numPlazas;
		if (numPlazas <= numPasajeros) {
			this.completo = true;
		} else {
			this.completo = false;
		}
		this.numPasajeros = pasajeros.size();

	}

	public VueloImpl(String codigo, String origen, String destino, LocalDateTime fechaSalida,
			LocalDateTime fechaLlegada, Duration duracion, Boolean completo, Integer numPlazas) {
		this.codigo = codigo;
		this.origen = origen;
		this.destino = destino;
		this.fechaSalida = fechaSalida;
		this.fechaLlegada = fechaLlegada;
		this.duracion = duracion;
		this.numPlazas = numPlazas;
		if (numPlazas <= numPasajeros) {
			this.completo = true;
		} else {
			this.completo = false;
		}
		this.pasajeros = new HashSet<Persona>();
		this.numPasajeros = pasajeros.size();

	}

	// ------GETTERS && SETTERS

	public String getCodigo() {
		return codigo;
	}

	public String getOrigen() {
		return origen;
	}

	public String getDestino() {
		return destino;
	}

	public LocalDateTime getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(LocalDateTime fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public LocalDateTime getFechaLlegada() {
		return fechaLlegada;
	}

	public void setFechaLlegada(LocalDateTime fechaLlegada) {
		this.fechaLlegada = fechaLlegada;
	}

	public Duration getDuracion() {
		return duracion;
	}

	public Integer getNumPlazas() {
		return numPlazas;
	}

	public Boolean getCompleto() {
		return completo;
	}

	public Set<Persona> getPasajeros() {
		return pasajeros;
	}

	public Integer getNumPasajeros() {
		return numPasajeros;
	}

	// ------METODOS

	public String toString() {
		return "VueloImpl [codigo=" + codigo + ", origen=" + origen + ", destino=" + destino + ", fechaSalida="
				+ fechaSalida + ", fechaLlegada=" + fechaLlegada + ", duracion=" + duracion + ", numPlazas=" + numPlazas
				+ ", completo=" + completo + ", pasajeros=" + pasajeros + ", numPasajeros=" + numPasajeros + "]";
	}

	public void nuevoPasajero(Persona p) {
		if (p != null) {
			if (completo) {
				System.out.println("Manito el vuelo ta full");
			} else {
				pasajeros.add(p);
			}
		}
	}

	public void eliminarPasajeros(Persona p) {
		if (pasajeros.contains(p)) {
			pasajeros.remove(p);
		}
	}

	public Boolean isCompleto() {
		return completo;
	}

	public int compareTo(Vuelo v2) {
		return Integer.compare(this.getNumPasajeros(), v2.getNumPasajeros());

	}

}
