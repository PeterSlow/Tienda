<%-- 
    Document   : carrito
    Created on : 10-nov-2022, 10:43:32
    Author     : dcc_s
--%>

<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
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
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
    %>
    <body>
        <%@include file="../INCLUDES/header.inc" %>
        <main>
            <form action="carrito" method="post" class="form-carrito">
                <table>
                    
                    <%= request.getAttribute("msnLibroEliminado")!=null? "<p class='add'>"+  request.getAttribute("msnLibroEliminado") + "</p>":" "%>
                    <%= request.getAttribute("msnCarritoEliminado")!=null? "<p class='add'>"+  request.getAttribute("msnCarritoEliminado") + "</p>":" "%>
                    <%
                        if (request.getSession().getAttribute("carrito_sesion")!=null) {
                    %>
                    <%= "<caption>Este es el contenido de su cesta</caption><tr><th>Cantidad</th><th>Título</th><th>Precio unidad</th><th>Precio total</th><th>Acciones</th></tr>"
                    %>
                    <%        
                        for (BeanBook elemento : carrito) {
                    %>
                    <%="<tr><td>" + elemento.getCantidad() + "</td>"
                            + "<td>" + elemento.getNombre() + "</td>"
                            + "<td>" + elemento.getPrecio() + " €</td>"
                            + "<td>" + Math.round(elemento.getCantidad() * elemento.getPrecio() * 100) / 100D + " €</td>"
                            + "<td><button type='submit' class='btn delete' name='enviar' value='" + elemento.getNombre() + "'>Eliminar</button></td></tr>"%>
                    <%
                            }
                        }
                    %>
                </table>
                <div class="carrito-btn">
                    <button name="enviar" value="add" type="submit" class="btn">Seguir comprando</button>
                    <button name="enviar" value="delete" type="submit" 
                            <%= (Boolean) request.getAttribute("existeCarrito")? "class='btn btn-carrito'" : "class='btn btn-carrito btn-disabled' disabled" %>
                            >Eliminar carrito </button>
                    <button name="enviar" value="pay" type="submit"
                            <%= (Boolean) request.getAttribute("existeCarrito")? "class='btn btn-carrito'" : "class='btn btn-carrito btn-disabled' disabled" %>
                            >Finalizar compra</button>
                </div>
            </form>
        </main>
        <%@include file="../INCLUDES/footer.inc" %>
    </body>

</html>
