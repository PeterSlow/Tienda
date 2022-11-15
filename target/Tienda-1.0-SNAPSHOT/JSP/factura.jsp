<%-- 
    Document   : factura
    Created on : 11-nov-2022, 10:52:03
    Author     : dcc_s
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="es.albarregas.beans.BeanBook"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../INCLUDES/meta.inc" %>
        <style><%@include file="../css/index.css"%></style>
        <title>Libreria</title>
        <link rel="stylesheet" href="css/index.css">
        <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@200;300&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400;500&display=swap" rel="stylesheet">
    </head>
    <%
        ArrayList<BeanBook> carrito = (ArrayList) request.getSession().getAttribute("carrito_sesion");
    %>
    <body>
        <%@include file="../INCLUDES/header.inc" %>
        <main>
            <form action="factura" method="post" class="form-carrito">
                <table>
                    <%
                        if (request.getSession().getAttribute("carrito_sesion")!=null) {
                    %>
                    <%= "<caption>Este es el detalle de su pedido</caption><tr><th>Cantidad</th><th>Título</th><th>Precio unidad</th><th>Precio total</th></tr>"
                    %>
                    <%        
                        for (BeanBook elemento : carrito) {
                    %>
                    <%="<tr><td>" + elemento.getCantidad() + " </td>"
                            + "<td>" + elemento.getNombre() + " </td>"
                            + "<td>" + elemento.getPrecio() + " €</td>"
                            + "<td>" + Math.round(elemento.getCantidad() * elemento.getPrecio() * 100) / 100D + " €</td></tr>"
                    %>
                    <%
                            }
                        }
                    %>
                    <tr><td colspan="4" class="total">El total es: <%= request.getAttribute("total") %> €</td></tr>
                </table>
                <div class="carrito-btn">
                    <button name="enviar" value="eliminarSesion" type="submit" class="btn">Volver a inicio</button>
                </div>
            </form>
        </main>
        <%@include file="../INCLUDES/footer.inc" %>
    </body>

</html>
