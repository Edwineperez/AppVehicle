package com.garaje.persistence;

import com.garaje.model.Vehiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/*
 * ===========================================================
 * VehiculoDAO.java
 * ===========================================================
 * Objeto de acceso a datos (DAO) encargado de todas las
 * operaciones CRUD sobre la tabla "vehiculos" en la base de datos.
 *
 * Esta clase no contiene reglas de negocio, solo la lógica
 * necesaria para ejecutar consultas SQL seguras.
 *
 * Características:
 * - Uso de PreparedStatement para evitar inyección SQL.
 * - Manejo automático de recursos con try-with-resources.
 * - Métodos CRUD bien definidos: listar, buscar, insertar, actualizar y eliminar.
 * - Método auxiliar existePlaca() para validación de unicidad de placas.
 *
 */
public class VehiculoDAO {

    // Fuente de datos (DataSource) gestionada por el contenedor GlassFish
    private final DataSource ds;

    /*
     * Constructor principal.
     * Recibe la referencia del DataSource configurado en el servidor
     * para obtener conexiones a la base de datos.
     */
    public VehiculoDAO(DataSource ds) {
        this.ds = ds;
    }

    // ===========================================================
    // MÉTODOS CRUD
    // ===========================================================

    /*
     * Lista todos los vehículos registrados.
     * Se ordenan de forma descendente por ID (últimos primero).
     */
    public List<Vehiculo> listar() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos ORDER BY id DESC";

        try (Connection con = ds.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // Recorre los resultados y mapea cada registro a un objeto Vehiculo
            while (rs.next()) {
                Vehiculo v = new Vehiculo();
                v.setId(rs.getInt("id"));
                v.setPlaca(rs.getString("placa"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));
                v.setColor(rs.getString("color"));
                v.setPropietario(rs.getString("propietario"));
                lista.add(v);
            }
        }
        return lista;
    }

    /*
     * Busca vehículos de forma parcial según texto ingresado.
     * Permite buscar por placa, marca o propietario.
     * Ejemplo: buscar("toyota") -> devuelve todos los vehículos con marca o propietario que contenga "toyota".
     */
    public List<Vehiculo> buscarVehiculos(String query) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos WHERE placa LIKE ? OR marca LIKE ? OR propietario LIKE ? ORDER BY id DESC";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String q = "%" + query + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setString(3, q);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vehiculo v = new Vehiculo();
                    v.setId(rs.getInt("id"));
                    v.setPlaca(rs.getString("placa"));
                    v.setMarca(rs.getString("marca"));
                    v.setModelo(rs.getString("modelo"));
                    v.setColor(rs.getString("color"));
                    v.setPropietario(rs.getString("propietario"));
                    lista.add(v);
                }
            }
        }
        return lista;
    }

    /*
     * Busca un vehículo específico por su ID único.
     * Retorna null si no existe.
     */
    public Vehiculo buscar(int id) throws SQLException {
        Vehiculo v = null;
        String sql = "SELECT * FROM vehiculos WHERE id = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    v = new Vehiculo();
                    v.setId(rs.getInt("id"));
                    v.setPlaca(rs.getString("placa"));
                    v.setMarca(rs.getString("marca"));
                    v.setModelo(rs.getString("modelo"));
                    v.setColor(rs.getString("color"));
                    v.setPropietario(rs.getString("propietario"));
                }
            }
        }
        return v;
    }

    /*
     * Inserta un nuevo vehículo en la base de datos.
     * Se espera que las validaciones ya hayan sido realizadas en la capa de negocio (VehiculoFacade).
     */
    public void insertar(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, color, propietario) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getColor());
            ps.setString(5, v.getPropietario());
            ps.executeUpdate();
        }
    }

    /*
     * Actualiza los datos de un vehículo existente según su ID.
     * Si el ID no existe, no se modifica ningún registro.
     */
    public void actualizar(Vehiculo v) throws SQLException {
        String sql = "UPDATE vehiculos SET placa=?, marca=?, modelo=?, color=?, propietario=? WHERE id=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getColor());
            ps.setString(5, v.getPropietario());
            ps.setInt(6, v.getId());
            ps.executeUpdate();
        }
    }

    /*
     * Elimina un vehículo de la base de datos usando su ID.
     */
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM vehiculos WHERE id=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /*
     * ===========================================================
     * MÉTODO DE UTILIDAD
     * ===========================================================
     */

    /*
     * Verifica si una placa específica ya está registrada en la base.
     * Retorna true si ya existe (se usa para evitar duplicados).
     *
     * La comparación es insensible a mayúsculas/minúsculas (LOWER()).
     */
    public boolean existePlaca(String placa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vehiculos WHERE LOWER(placa) = LOWER(?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
