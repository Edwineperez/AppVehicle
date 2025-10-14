package com.garaje.controller;

import com.garaje.facade.VehiculoFacade;
import com.garaje.model.Vehiculo;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/*
 * ===========================================================
 * VehiculoServlet.java
 * ===========================================================
 * Controlador principal del módulo de gestión de vehículos.
 *
 * Se encarga de recibir las solicitudes HTTP (GET y POST),
 * invocar la lógica de negocio a través de la clase VehiculoFacade
 * y reenviar los resultados a las vistas JSP correspondientes.
 *
 * Cumple con el patrón MVC:
 * - Modelo → Vehiculo.java
 * - Vista → vehiculos.jsp / formVehiculo.jsp
 * - Controlador → VehiculoServlet.java
 *
 * Autor: Edwin
 * Fecha: Octubre 2025
 */
@WebServlet("/vehiculos")
public class VehiculoServlet extends HttpServlet {

    // Inyección del DataSource configurado en GlassFish (pool JDBC)
    @Resource(lookup = "jdbc/myPool")
    private DataSource ds;

    // Fachada con las reglas de negocio
    private VehiculoFacade facade;

    /*
     * Inicializa la fachada al iniciar el servlet.
     * Se ejecuta una sola vez cuando el servlet es cargado.
     */
    @Override
    public void init() throws ServletException {
        facade = new VehiculoFacade(ds);
    }

    /*
     * Maneja las peticiones GET (listar, buscar, mostrar formulario).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {

                // Mostrar formulario para nuevo vehículo
                case "new":
                    request.setAttribute("vehiculo", new Vehiculo());
                    request.getRequestDispatcher("formVehiculo.jsp").forward(request, response);
                    break;

                // Editar vehículo existente
                case "edit":
                    int idEdit = Integer.parseInt(request.getParameter("id"));
                    Vehiculo v = facade.buscar(idEdit);
                    request.setAttribute("vehiculo", v);
                    request.getRequestDispatcher("formVehiculo.jsp").forward(request, response);
                    break;

                // Buscar vehículo(s)
                case "search":
                    String query = request.getParameter("query");
                    List<Vehiculo> filtrados = facade.buscar(query);
                    request.setAttribute("vehiculos", filtrados);
                    request.getRequestDispatcher("vehiculos.jsp").forward(request, response);
                    break;

                // Listar todos
                default:
                    List<Vehiculo> lista = facade.listar();
                    request.setAttribute("vehiculos", lista);
                    request.getRequestDispatcher("vehiculos.jsp").forward(request, response);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("vehiculos.jsp").forward(request, response);
        }
    }

    /*
     * Maneja las peticiones POST (crear, actualizar o eliminar vehículos).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "";

        try {
            // =====================================================
            // ELIMINAR VEHÍCULO
            // =====================================================
            if (action.equals("delete")) {
                int id = Integer.parseInt(request.getParameter("id"));
                facade.eliminar(id);
                response.sendRedirect("vehiculos");
                return;
            }

            // =====================================================
            // INSERTAR O ACTUALIZAR VEHÍCULO
            // =====================================================
            int id = 0;
            if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
                id = Integer.parseInt(request.getParameter("id"));
            }

            String placa = request.getParameter("placa");
            String marca = request.getParameter("marca");
            String modelo = request.getParameter("modelo");
            String color = request.getParameter("color");
            String propietario = request.getParameter("propietario");

            Vehiculo v = new Vehiculo(id, placa, marca, modelo, color, propietario);

            // Nuevo registro
            if (id == 0) {
                facade.insertar(v);

                // =====================================================
                // (10) Notificación simulada al registrar un Ferrari
                // =====================================================
                if ("Ferrari".equalsIgnoreCase(v.getMarca())) {
                    request.setAttribute("mensaje", "🚗 Se ha registrado un nuevo Ferrari con placa " + v.getPlaca() + ".");
                    request.setAttribute("vehiculos", facade.listar());
                    request.getRequestDispatcher("vehiculos.jsp").forward(request, response);
                    return; // evita el redirect
                }

            // Actualización existente
            } else {
                facade.actualizar(v);
            }

            // Redirige a la lista de vehículos
            response.sendRedirect("vehiculos");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("vehiculos.jsp").forward(request, response);
        }
    }
}
