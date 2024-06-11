package Clases;

import java.sql.Date;

public class PersonaImpl {

	// ------ATRIBUTOS

	private String dni;
	private String nombre;
	private String apellido;
	private Date fechaNacimiento;
	private String telefono;

	// ------CONSTRUCTOR

	public PersonaImpl(String dni, String nombre, String apellido, Date fechaNacimiento, String telefono) {

		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
	}

	// ------GETTERS && SETTERS

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
