package com.garaje.model;

/*
 * ===========================================================
 * Vehiculo.java
 * ===========================================================
 * Clase de modelo (entidad) que representa un registro de la
 * tabla "vehiculos" en la base de datos del sistema Garaje.
 *
 * Esta clase se utiliza para mapear los datos de un vehículo
 * dentro de la aplicación, permitiendo su manipulación por
 * las capas DAO, Facade y Servlet.
 *
 * Cada instancia de Vehiculo representa un vehículo único
 * con sus datos básicos registrados en el sistema.
 *
 */
public class Vehiculo {

    // Identificador único del vehículo (clave primaria en la base de datos)
    private int id;

    // Placa del vehículo (debe ser única y cumplir las validaciones del negocio)
    private String placa;

    // Marca del vehículo (ejemplo: Toyota, Honda, Ford, Ferrari)
    private String marca;

    // Modelo o año del vehículo (ejemplo: 2023, Corolla 2024)
    private String modelo;

    // Color del vehículo (solo se permiten los definidos por las reglas de negocio)
    private String color;

    // Nombre del propietario (persona asociada al vehículo)
    private String propietario;

    /*
     * Constructor vacío.
     * Se utiliza cuando se crean objetos sin datos iniciales
     * (por ejemplo, al mostrar formularios vacíos).
     */
    public Vehiculo() { }

    /*
     * Constructor completo.
     * Permite crear un objeto Vehiculo con todos los atributos definidos.
     *
     */
    public Vehiculo(int id, String placa, String marca, String modelo, String color, String propietario) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.propietario = propietario;
    }

    // ===========================================================
    // MÉTODOS GETTER Y SETTER
    // ===========================================================

    // Devuelve el identificador único del vehículo.
    public int getId() {
        return id;
    }

    // Asigna un valor al identificador del vehículo.
    public void setId(int id) {
        this.id = id;
    }

    // Devuelve la placa actual del vehículo.
    public String getPlaca() {
        return placa;
    }

    // Asigna la placa al vehículo.
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    // Devuelve la marca del vehículo.
    public String getMarca() {
        return marca;
    }

    // Asigna la marca del vehículo.
    public void setMarca(String marca) {
        this.marca = marca;
    }

    // Devuelve el modelo o año del vehículo.
    public String getModelo() {
        return modelo;
    }

    // Asigna el modelo o año al vehículo.
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    // Devuelve el color actual del vehículo.
    public String getColor() {
        return color;
    }

    // Asigna el color del vehículo.
    public void setColor(String color) {
        this.color = color;
    }

    // Devuelve el nombre del propietario.
    public String getPropietario() {
        return propietario;
    }

    // Asigna el propietario del vehículo.
    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
}
