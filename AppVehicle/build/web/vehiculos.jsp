<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.garaje.model.Vehiculo" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Vehículos</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
        }

        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 30px;
            color: white;
        }

        .header h1 {
            font-size: 2rem;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .header h1::before {
            content: "🚗";
            font-size: 2.5rem;
        }

        .header-controls {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            align-items: center;
        }

        .search-form {
            display: flex;
            gap: 10px;
            flex: 1;
            max-width: 450px;
            min-width: 280px;
        }

        .search-input {
            flex: 1;
            padding: 12px 16px;
            border-radius: 10px;
            border: none;
            outline: none;
            font-size: 1rem;
            background: rgba(255, 255, 255, 0.95);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            transition: all 0.3s ease;
        }

        .search-input:focus {
            background: white;
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
            transform: translateY(-2px);
        }

        .search-input::placeholder {
            color: #999;
        }

        .btn-search {
            padding: 12px 20px;
            background: white;
            color: #667eea;
            border: none;
            border-radius: 10px;
            font-weight: 600;
            font-size: 0.95rem;
            cursor: pointer;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .btn-search::before {
            content: "🔍";
            margin-right: 6px;
        }

        .btn-search::after {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: 0;
            height: 0;
            border-radius: 50%;
            background: rgba(102, 126, 234, 0.2);
            transform: translate(-50%, -50%);
            transition: width 0.6s, height 0.6s;
        }

        .btn-search:hover::after {
            width: 300px;
            height: 300px;
        }

        .btn-search:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
        }

        .btn-search span {
            position: relative;
            z-index: 1;
        }

        .btn-new {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 24px;
            background: white;
            color: #667eea;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            position: relative;
            overflow: hidden;
        }

        .btn-new::after {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: 0;
            height: 0;
            border-radius: 50%;
            background: rgba(102, 126, 234, 0.2);
            transform: translate(-50%, -50%);
            transition: width 0.6s, height 0.6s;
        }

        .btn-new:hover::after {
            width: 300px;
            height: 300px;
        }

        .btn-new:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
        }

        .btn-new span {
            position: relative;
            z-index: 1;
        }

        .content {
            padding: 30px;
        }

        .error {
            background: #fee;
            border-left: 4px solid #f44336;
            color: #c62828;
            padding: 15px 20px;
            margin-bottom: 20px;
            border-radius: 4px;
            font-weight: 500;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #666;
        }

        .empty-state::before {
            content: "🚙";
            font-size: 4rem;
            display: block;
            margin-bottom: 20px;
            opacity: 0.5;
        }

        .empty-state p {
            font-size: 1.2rem;
        }

        .table-wrapper {
            overflow-x: auto;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        th {
            padding: 16px;
            text-align: left;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }

        tbody tr {
            border-bottom: 1px solid #f0f0f0;
            transition: all 0.3s ease;
        }

        tbody tr:hover {
            background: #f8f9ff;
            transform: scale(1.01);
            box-shadow: 0 2px 8px rgba(102, 126, 234, 0.1);
        }

        tbody tr:last-child {
            border-bottom: none;
        }

        td {
            padding: 16px;
            color: #333;
        }

        .actions {
            display: flex;
            gap: 8px;
            align-items: center;
        }

        .btn-edit, .btn-delete {
            padding: 8px 16px;
            border-radius: 6px;
            font-size: 0.9rem;
            font-weight: 500;
            text-decoration: none;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-edit {
            background: #4caf50;
            color: white;
        }

        .btn-edit:hover {
            background: #45a049;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(76, 175, 80, 0.4);
        }

        .btn-delete {
            background: #f44336;
            color: white;
        }

        .btn-delete:hover {
            background: #da190b;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(244, 67, 54, 0.4);
        }

        .delete-form {
            display: inline;
        }

        .badge {
            display: inline-block;
            padding: 4px 12px;
            background: #e3f2fd;
            color: #1976d2;
            border-radius: 12px;
            font-size: 0.85rem;
            font-weight: 600;
        }

        @media (max-width: 768px) {
            .container {
                border-radius: 0;
            }

            .header h1 {
                font-size: 1.5rem;
            }

            .header-controls {
                flex-direction: column;
                align-items: stretch;
            }

            .search-form {
                max-width: 100%;
            }

            .table-wrapper {
                overflow-x: scroll;
            }

            table {
                font-size: 0.9rem;
            }

            th, td {
                padding: 12px 8px;
            }

            .actions {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Gestión de Vehículos</h1>
            <div class="header-controls">
                <!-- Botón de nuevo vehículo -->
                <a href="vehiculos?action=new" class="btn-new">
                    <span>➕ Agregar Nuevo Vehículo</span>
                </a>
                <!-- Barra de búsqueda -->
                <form action="vehiculos" method="get" class="search-form">
                    <input type="hidden" name="action" value="search" />
                    <input type="text" 
                           name="query" 
                           class="search-input"
                           placeholder="🔍 Buscar por placa, marca o propietario" />
                    <button type="submit" class="btn-search">
                        <span>Buscar</span>
                    </button>
                </form>
            </div>
        </div>

        <div class="content">
            <% if (request.getAttribute("error") != null) { %>
                <div class="error">
                    ⚠️ <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <%
            List<Vehiculo> vehiculos = (List<Vehiculo>) request.getAttribute("vehiculos");
            if (vehiculos == null || vehiculos.isEmpty()) {
            %>
                <div class="empty-state">
                    <p>No hay vehículos registrados en el sistema.</p>
                </div>
            <% } else { %>
                <div class="table-wrapper">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Placa</th>
                                <th>Marca</th>
                                <th>Modelo</th>
                                <th>Color</th>
                                <th>Propietario</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Vehiculo v : vehiculos) { %>
                            <tr>
                                <td><span class="badge"><%= v.getId() %></span></td>
                                <td><strong><%= v.getPlaca() %></strong></td>
                                <td><%= v.getMarca() %></td>
                                <td><%= v.getModelo() %></td>
                                <td><%= v.getColor() %></td>
                                <td><%= v.getPropietario() %></td>
                                <td>
                                    <div class="actions">
                                        <a href="vehiculos?action=edit&id=<%= v.getId() %>" class="btn-edit">
                                            ✏️ Editar
                                        </a>
                                        <form action="vehiculos" method="post" class="delete-form" 
                                              onsubmit="return confirm('¿Está seguro de eliminar este vehículo?');">
                                            <input type="hidden" name="id" value="<%= v.getId() %>" />
                                            <button type="submit" name="action" value="delete" class="btn-delete">
                                                🗑️ Eliminar
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                <% } %>
        </div>
    </div>

    <% if (request.getAttribute("mensaje") != null) {%>
    <div style="background:#e8f5e9; border-left:4px solid #4caf50; color:#2e7d32;
                padding:15px; margin-bottom:20px; border-radius:6px;">
        <%= request.getAttribute("mensaje")%>
    </div>
    <% }%>


</body>
</html>