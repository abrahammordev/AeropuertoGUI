# Sistema de Gestión de Aeropuertos

Este proyecto es un sistema de gestión de aeropuertos desarrollado en Java utilizando Swing para la interfaz gráfica y MySQL para la base de datos. El sistema implementa un CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar aeropuertos, vuelos y personas.

# Para ver el video tutorial

https://drive.google.com/file/d/13QkbrUHHzmYcwZbBleydpc_UihUq6Odn/view?usp=drive_link

## Requisitos

1. **Java Development Kit (JDK)**
2. **XAMPP** para levantar el servidor MySQL
3. **MySQL versión 5.0.37**

## Configuración

### Configurar MySQL

1. Instale y configure XAMPP.
2. Levante el servidor MySQL desde el panel de control de XAMPP.
3. Cree la base de datos. El script de creación está incluido en el código.
4. Versión de Windows (en sistemas operativos como MacOS MYSQL es case sensitive pero en este caso se realiza la conversión internamente).

### Configurar `m.jar`

El archivo `m.jar` debe contener la ruta absoluta. Actualice la ruta después de extraer el ZIP:

```bash
java -jar /ruta/absoluta/a/m.jar

### En este caso se adjunta también el package Service Clases e Interfaces a pesar de no haber sido usados en la elaboración de la interfaz gráfica
### Estos se usaron para el CRUD exclusivo por consola y para la realización del ejercicio del 2º Trimestre
