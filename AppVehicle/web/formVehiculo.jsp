<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.garaje.model.Vehiculo" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulario de Vehículo</title>
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
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .container {
            width: 100%;
            max-width: 500px;
        }

        .form-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
            animation: slideUp 0.5s ease;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .form-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 30px;
            text-align: center;
            color: white;
        }

        .form-header h1 {
            font-size: 1.8rem;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 12px;
        }

        .form-header h1::before {
            content: "🚗";
            font-size: 2rem;
        }

        .form-header p {
            opacity: 0.9;
            font-size: 0.95rem;
        }

        .form-body {
            padding: 35px;
        }

        .error-message {
            background: #fee;
            border-left: 4px solid #f44336;
            color: #c62828;
            padding: 15px 20px;
            margin-bottom: 25px;
            border-radius: 8px;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 10px;
            animation: shake 0.5s ease;
        }

        .error-message::before {
            content: "⚠️";
            font-size: 1.2rem;
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
        }

        .form-group {
            margin-bottom: 22px;
        }

        label {
            display: block;
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            font-size: 0.95rem;
        }

        label::after {
            content: "*";
            color: #f44336;
            margin-left: 4px;
        }

        input[type="text"],
        input[type="hidden"],
        select {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            font-size: 1rem;
            transition: all 0.3s ease;
            background: #fafafa;
        }

        input[type="text"]:focus,
        select:focus {
            outline: none;
            border-color: #667eea;
            background: white;
            box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
            transform: translateY(-2px);
        }

        select {
            cursor: pointer;
            appearance: none;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%23667eea' d='M6 9L1 4h10z'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 16px center;
            padding-right: 40px;
        }

        .btn-group {
            display: flex;
            gap: 12px;
            margin-top: 30px;
        }

        button {
            flex: 1;
            padding: 14px;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
        }

        .btn-save {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-save:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
        }

        .btn-save:active {
            transform: translateY(0);
        }

        .btn-save::before {
            content: "💾";
        }

        /* 🎨 BOTÓN "VOLVER" MEJORADO */
        .btn-back {
            position: relative;
            flex: 1;
            padding: 14px;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            font-weight: 600;
            text-align: center;
            background: linear-gradient(135deg, #ff9800 0%, #ffb74d 100%);
            color: white;
            text-decoration: none;
            cursor: pointer;
            overflow: hidden;
            box-shadow: 0 4px 15px rgba(255, 152, 0, 0.4);
            transition: all 0.3s ease;
        }

        .btn-back::before {
            content: "←";
            margin-right: 8px;
            font-size: 1.2rem;
            transition: transform 0.3s ease;
        }

        .btn-back::after {
            content: "";
            position: absolute;
            top: 50%;
            left: 50%;
            width: 0;
            height: 0;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 50%;
            transform: translate(-50%, -50%);
            transition: width 0.6s ease, height 0.6s ease;
            z-index: 0;
        }

        .btn-back:hover::after {
            width: 300px;
            height: 300px;
        }

        .btn-back:hover {
            background: linear-gradient(135deg, #f57c00 0%, #ff9800 100%);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(255, 152, 0, 0.6);
        }

        .btn-back:hover::before {
            transform: translateX(-4px);
        }

        @media (max-width: 600px) {
            .form-body {
                padding: 25px;
            }

            .form-header h1 {
                font-size: 1.5rem;
            }

            .btn-group {
                flex-direction: column;
            }
        }

        .error-field {
            border-color: #f44336 !important;
            animation: shake 0.3s ease;
        }

        button:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-card">
            <div class="form-header">
                <h1>
                    <% 
                    Vehiculo v = (Vehiculo) request.getAttribute("vehiculo"); 
                    if (v == null) v = new Vehiculo();
                    boolean isEdit = v.getId() != 0;
                    %>
                    <%= isEdit ? "Editar Vehículo" : "Nuevo Vehículo" %>
                </h1>
                <p><%= isEdit ? "Actualiza la información del vehículo" : "Registra un nuevo vehículo en el sistema" %></p>
            </div>

            <div class="form-body">
                <% if (request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <form action="vehiculos" method="post" id="vehiculoForm">
                    <input type="hidden" name="id" value="<%= v.getId() %>" />

                    <div class="form-group">
                        <label for="placa">Placa</label>
                        <div class="input-icon placa">
                            <input type="text" 
                                   id="placa" 
                                   name="placa" 
                                   value="<%= v.getPlaca() != null ? v.getPlaca() : "" %>" 
                                   placeholder="Ej: ABC-123"
                                   required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="marca">Marca</label>
                        <div class="input-icon marca">
                            <input type="text" 
                                   id="marca" 
                                   name="marca" 
                                   value="<%= v.getMarca() != null ? v.getMarca() : "" %>" 
                                   placeholder="Ej: Toyota, Honda, Ford"
                                   required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="modelo">Modelo</label>
                        <div class="input-icon modelo">
                            <input type="text" 
                                   id="modelo" 
                                   name="modelo" 
                                   value="<%= v.getModelo() != null ? v.getModelo() : "" %>" 
                                   placeholder="Ej: 2024, Corolla 2023"
                                   required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="color">Color</label>
                        <select id="color" name="color" required>
                            <option value="">-- Seleccione un color --</option>
                            <option value="Rojo" <%= "Rojo".equals(v.getColor()) ? "selected" : "" %>>🔴 Rojo</option>
                            <option value="Blanco" <%= "Blanco".equals(v.getColor()) ? "selected" : "" %>>⚪ Blanco</option>
                            <option value="Negro" <%= "Negro".equals(v.getColor()) ? "selected" : "" %>>⚫ Negro</option>
                            <option value="Azul" <%= "Azul".equals(v.getColor()) ? "selected" : "" %>>🔵 Azul</option>
                            <option value="Gris" <%= "Gris".equals(v.getColor()) ? "selected" : "" %>>⚫ Gris</option>
                            <option value="Verde" <%= "Verde".equals(v.getColor()) ? "selected" : "" %>>🟢 Verde</option>
                            <option value="Amarillo" <%= "Amarillo".equals(v.getColor()) ? "selected" : "" %>>🟡 Amarillo</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="propietario">Propietario</label>
                        <div class="input-icon propietario">
                            <input type="text" 
                                   id="propietario" 
                                   name="propietario" 
                                   value="<%= v.getPropietario() != null ? v.getPropietario() : "" %>" 
                                   placeholder="Nombre completo del propietario"
                                   required>
                        </div>
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn-save">Guardar</button>
                        <a href="vehiculos" class="btn-back">Volver</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Validación adicional y mejora UX
        const form = document.getElementById('vehiculoForm');
        const inputs = form.querySelectorAll('input[required], select[required]');
        inputs.forEach(input => {
            input.addEventListener('invalid', function() {
                this.classList.add('error-field');
            });
            input.addEventListener('input', function() {
                this.classList.remove('error-field');
            });
        });

        // Convertir placa a mayúsculas automáticamente
        const placaInput = document.getElementById('placa');
        placaInput.addEventListener('input', function() {
            this.value = this.value.toUpperCase();
        });
    </script>
</body>
</html>
