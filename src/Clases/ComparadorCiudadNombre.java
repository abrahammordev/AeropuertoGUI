package Clases;

import java.util.Comparator;

import Interfaces.Aeropuerto;

public class ComparadorCiudadNombre implements Comparator {
	public int compare(Object o1, Object o2) {

		int cmp = 0;
		if (o1 != null && o2 != null) {

			Aeropuerto a1 = (Aeropuerto) o1;
			Aeropuerto a2 = (Aeropuerto) o2;

			// Implementamos el compareTo que devuelve un -1 si no es igual , si es igual un
			// 0

			cmp = a1.getCiudad().compareToIgnoreCase(a2.getCiudad());

			if (cmp == 0) {

				cmp = a1.getNombre().compareToIgnoreCase(a2.getNombre());
			}

			if (cmp <= 1) {
				cmp = -1;
			}
			if (cmp >= 1) {
				cmp = 1;
			}

		}
		return cmp;

	}
}
