package Clases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import Interfaces.Aeropuerto;

import Interfaces.Vuelo;

public class AeropuertoImpl implements Aeropuerto {

	// ------ATRIBUTOS

	private String nombre;
	private String ciudad;
	private SortedSet<Vuelo> vuelos;

	// ------CONSTRUCTORES

	public AeropuertoImpl(String nombre, String ciudad) {
		this.nombre = nombre;
		this.ciudad = ciudad;
		vuelos = new TreeSet<Vuelo>();
	}

	public AeropuertoImpl(String nombre, String ciudad, SortedSet<Vuelo> vuelos) {
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.vuelos = vuelos;
	}

	// ------GETTERS && SETTERS

	public String getNombre() {
		return nombre;
	}

	public String getCiudad() {
		return ciudad;
	}

	public SortedSet<Vuelo> getVuelos() {
		return vuelos;
	}

	// ------METODOS

	public boolean equals(Object o) {
		if (o instanceof Aeropuerto) {
			Aeropuerto vuelo2 = (Aeropuerto) o;
			return (this.getNombre().equals(vuelo2.getNombre()));
		} else {
			return false;
		}
	}

	public void ordenacionNatural() {
		// Implementación para ordenación natural
	}

	public void ordenacionAdicional() {
		// Implementación para ordenación adicional
	}

	public String toString() {
		return "AeropuertoImpl [nombre=" + nombre + ", ciudad=" + ciudad + ", vuelos=" + vuelos + "]";
	}

	// ------METODOS TACTICOS

	public void nuevoVuelo(Vuelo v) {
		if (vuelos != null) {
			vuelos.add(v);
		}
	}

	public void nuevosVuelos(Collection<Vuelo> vuelosAñadidos) {
		if (vuelos != null) {
			vuelos.addAll(vuelosAñadidos);
		}
	}

	public boolean contieneVuelo(Vuelo v) {
		return vuelos.contains(v);
	}

	public void eliminarVuelo(Vuelo v) {
		if (v != null) {
			vuelos.remove(v);
		}
	}

	public Set<Vuelo> seleccionaVuelosFecha(LocalDateTime fechaSalida) {
		Set<Vuelo> listaFecha = new TreeSet<Vuelo>();

		for (Vuelo vuelo : this.vuelos) {
			if (vuelo.getFechaSalida().equals(fechaSalida)) {
				listaFecha.add(vuelo);
			}
		}

		return listaFecha;
	}

	public Vuelo getVueloMasPasajeros() {
		Vuelo vueloConMasPasajeros = null;
		int maxPasajeros = 0;

		for (Vuelo vuelo : vuelos) {
			if (vuelo.getNumPasajeros() > maxPasajeros) {
				maxPasajeros = vuelo.getNumPasajeros();
				vueloConMasPasajeros = vuelo;
			}
		}

		return vueloConMasPasajeros;
	}

//	public PersonaImpl getPasajeroMayor() {
//		PersonaImpl pasajeroMayor = null;
//
//		for (Vuelo vuelo : vuelos) {
//			for (PersonaImpl persona : vuelo.getPasajeros()) {
//				if (pasajeroMayor == null
//						|| persona.getFechaNacimiento().isBefore(pasajeroMayor.getFechaNacimiento())) {
//					pasajeroMayor = persona;
//				}
//			}
//		}
//
//		return pasajeroMayor;
//	}

	public Vuelo getVueloPlazasLibresDestino(String destino) {
		for (Vuelo vuelo : vuelos) {
			if (!vuelo.isCompleto() && vuelo.getDestino().equals(destino)) {
				return vuelo;
			}
		}
		return null;
	}

	public Integer calcularTotalPasajeros(String destino) {
		int total = 0;
		for (Vuelo vuelo : vuelos) {
			if (vuelo.getDestino().equals(destino)) {
				total += vuelo.getNumPasajeros();
			}
		}
		return total;
	}

	public Double calcularMediaPasajerosPorDía() {
		if (vuelos == null || vuelos.isEmpty()) {
			return 0.0;
		}

		Map<LocalDate, Integer> pasajerosPorDia = new HashMap<>();

		for (Vuelo vuelo : vuelos) {
			LocalDate fecha = vuelo.getFechaSalida().toLocalDate();
			pasajerosPorDia.put(fecha, pasajerosPorDia.getOrDefault(fecha, 0) + vuelo.getNumPasajeros());
		}

		int totalPasajeros = 0;
		for (Integer pasajeros : pasajerosPorDia.values()) {
			totalPasajeros += pasajeros;
		}
		int totalDias = pasajerosPorDia.size();

		return (double) totalPasajeros / totalDias;
	}

	public Map<String, Integer> getNumeroPasajerosPorDestino() {
		Map<String, Integer> pasajerosPorDestino = new HashMap<>();
		for (Vuelo vuelo : vuelos) {
			String destino = vuelo.getDestino();
			int numPasajeros = vuelo.getNumPasajeros();
			pasajerosPorDestino.put(destino, pasajerosPorDestino.getOrDefault(destino, 0) + numPasajeros);
		}
		return pasajerosPorDestino;
	}

	public SortedMap<LocalDateTime, List<Vuelo>> getVuelosPorFecha() {
		SortedMap<LocalDateTime, List<Vuelo>> vuelosPorFecha = new TreeMap<>();

		for (Vuelo vuelo : vuelos) {
			LocalDateTime fechaSalida = vuelo.getFechaSalida();
			if (!vuelosPorFecha.containsKey(fechaSalida)) {
				vuelosPorFecha.put(fechaSalida, new ArrayList<Vuelo>());
			}
			vuelosPorFecha.get(fechaSalida).add(vuelo);
		}

		return vuelosPorFecha;
	}

	public int compareTo(Object a) {
		try {
			Aeropuerto a1 = (Aeropuerto) a;

			int cmp = this.getNombre().compareToIgnoreCase(a1.getNombre());

			if (cmp == 0) {
				cmp = this.getCiudad().compareToIgnoreCase(a1.getCiudad());
			}

			return cmp;
		} catch (ClassCastException e) {
			System.out.println(
					"Error: No se pudo realizar la comparación. El objeto no es una instancia válida de Aeropuerto.");
			return -1;
		}
	}
}
