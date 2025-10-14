<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error del Sistema</title>
<style>
body { font-family: Arial, sans-serif; text-align: center; background-color: #fff0f0; }
h2 { color: #cc0000; }
p { color: #333; }
a { color: #007bff; text-decoration: none; }
</style>
</head>
<body>
<h2>Ocurrió un error</h2>
<p><%= request.getAttribute("error") != null ? request.getAttribute("error") : "Error desconocido." %></p>
<a href="vehiculos">Volver</a>
</body>
</html>