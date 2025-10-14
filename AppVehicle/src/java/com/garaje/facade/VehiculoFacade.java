package com.garaje.facade;

import com.garaje.model.Vehiculo;
import com.garaje.persistence.VehiculoDAO;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

/*
 * ===========================================================
 *  VehiculoFacade.java
 * ===========================================================
 * Capa de negocio (fachada) que implementa todas las reglas 
 * de validación y restricciones antes de que un vehículo sea 
 * insertado, actualizado o eliminado de la base de datos.
 *
 * Sirve como intermediaria entre el controlador (VehiculoServlet)
 * y el DAO (VehiculoDAO), aplicando la lógica de negocio sobre
 * los datos.
 *
 * Reglas implementadas:
 *  1. No permitir agregar un vehículo con placa duplicada.
 *  2. No aceptar propietario vacío o con menos de 5 caracteres.
 *  3. Marca, modelo y placa deben tener al menos 3 caracteres.
 *  4. El color debe ser uno de los valores permitidos (Rojo, Blanco, Negro, Azul, Gris).
 *  5. No aceptar vehículos con más de 20 años de antigüedad.
 *  6. Las placas deben ser únicas en toda la base.
 *  7. No se puede eliminar un vehículo si el propietario es “Administrador”.
 *  8. Actualizar solo si el vehículo realmente existe.
 *  9. Validación simulada contra SQL Injection.
 * 10. Si la marca es “Ferrari”, simular envío de notificación.
 */
public class VehiculoFacade {

    /** Acceso a la capa de persistencia (DAO) */
    private final VehiculoDAO dao;

    /** Lista de colores válidos en el sistema */
    private static final List<String> COLORES_VALIDOS = Arrays.asList("Rojo", "Blanco", "Negro", "Azul", "Gris");

    /** Constructor principal: inicializa el DAO usando el DataSource */
    public VehiculoFacade(DataSource ds) {
        this.dao = new VehiculoDAO(ds);
    }

    // ===========================================================
    // MÉTODOS CRUD PRINCIPALES
    // ===========================================================

    /** Devuelve todos los vehículos registrados */
    public List<Vehiculo> listar() throws SQLException {
        return dao.listar();
    }

    /** Busca vehículos por texto (placa, marca o propietario) */
    public List<Vehiculo> buscar(String query) throws SQLException {
        return dao.buscarVehiculos(query);
    }

    /** Busca un vehículo específico por ID */
    public Vehiculo buscar(int id) throws SQLException {
        return dao.buscar(id);
    }

    /**
     * Inserta un nuevo vehículo en la base de datos, validando todas las reglas de negocio.
     * 
     * @param v vehículo a registrar
     * @return true si el vehículo es un Ferrari (para mostrar notificación visual en el servlet)
     */
    public boolean insertar(Vehiculo v) throws Exception {
        validarDatos(v);

        // (1 y 6) Verificar duplicado de placa
        if (existePlaca(v.getPlaca())) {
            throw new Exception("Ya existe un vehículo con la placa " + v.getPlaca());
        }

        // (10) Simular notificación si es Ferrari
        boolean esFerrari = "Ferrari".equalsIgnoreCase(v.getMarca());
        if (esFerrari) {
            System.out.println("🚗 [Notificación] Nuevo Ferrari registrado: " + v.getPlaca());
        }

        // Inserta el vehículo
        dao.insertar(v);

        return esFerrari; // Permite al servlet decidir si mostrar mensaje visual
    }

    /** Actualiza los datos de un vehículo existente */
    public void actualizar(Vehiculo v) throws Exception {
        validarDatos(v);

        // (8) Verificar existencia
        Vehiculo existente = dao.buscar(v.getId());
        if (existente == null) {
            throw new Exception("El vehículo con ID " + v.getId() + " no existe.");
        }

        // (1 y 6) Validar duplicado si cambió la placa
        if (!existente.getPlaca().equalsIgnoreCase(v.getPlaca()) && existePlaca(v.getPlaca())) {
            throw new Exception("Ya existe otro vehículo con la placa " + v.getPlaca());
        }

        dao.actualizar(v);
    }

    /** Elimina un vehículo validando las reglas de negocio */
    public void eliminar(int id) throws Exception {
        Vehiculo v = dao.buscar(id);
        if (v == null) {
            throw new Exception("No se puede eliminar: el vehículo no existe.");
        }

        // (7) No eliminar si pertenece al propietario “Administrador”
        if ("Administrador".equalsIgnoreCase(v.getPropietario())) {
            throw new Exception("No se puede eliminar un vehículo del propietario 'Administrador'.");
        }

        dao.eliminar(id);
    }

    // ===========================================================
    // VALIDACIONES DE NEGOCIO
    // ===========================================================

    /** Valida los datos del vehículo conforme a las reglas del negocio */
    private void validarDatos(Vehiculo v) throws Exception {
        if (v == null) throw new Exception("El vehículo no puede ser nulo.");

        // (2) Propietario mínimo 5 caracteres
        if (v.getPropietario() == null || v.getPropietario().trim().length() < 5) {
            throw new Exception("El propietario debe tener al menos 5 caracteres.");
        }

        // (3) Marca, modelo y placa mínimo 3 caracteres
        if (v.getPlaca() == null || v.getPlaca().trim().length() < 3)
            throw new Exception("La placa debe tener al menos 3 caracteres.");

        if (v.getMarca() == null || v.getMarca().trim().length() < 3)
            throw new Exception("La marca debe tener al menos 3 caracteres.");

        if (v.getModelo() == null || v.getModelo().trim().length() < 3)
            throw new Exception("El modelo debe tener al menos 3 caracteres.");

        // (4) Validar color permitido
        if (!COLORES_VALIDOS.contains(v.getColor())) {
            throw new Exception("Color inválido. Solo se permiten: " + COLORES_VALIDOS);
        }

        // (5) No aceptar modelos con más de 20 años de antigüedad
        try {
            int anio = Integer.parseInt(v.getModelo().replaceAll("[^0-9]", ""));
            int actual = Year.now().getValue();
            if (anio < actual - 20) {
                throw new Exception("El modelo es demasiado antiguo (" + anio + "). No puede tener más de 20 años.");
            }
        } catch (NumberFormatException e) {
            // Si no es numérico, no aplica
        }

        // (9) Simulación de validación anti SQL Injection
        String regexSQL = ".*([';]|--|select|insert|delete|update|drop|alter).*";
        if (v.getPlaca().toLowerCase().matches(regexSQL) ||
            v.getMarca().toLowerCase().matches(regexSQL) ||
            v.getModelo().toLowerCase().matches(regexSQL) ||
            v.getPropietario().toLowerCase().matches(regexSQL)) {
            throw new Exception("Entrada inválida detectada (posible SQL Injection).");
        }
    }

    /** Verifica si una placa ya existe en la base de datos */
    private boolean existePlaca(String placa) throws SQLException {
        return dao.existePlaca(placa);
    }
}
