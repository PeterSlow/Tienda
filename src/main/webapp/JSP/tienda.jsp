<%-- 
    Document   : tienda
    Created on : 11-nov-2022, 10:53:01
    Author     : dcc_s
--%>

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
        /*String show = (String) request.getAttribute("show");
        String pay = (String) request.getAttribute("pay");*/
    %>
    <body>
        <%@include file="../INCLUDES/header.inc" %>
        <main>
            <form action="productos" method="post">
                <p class="add"> 
                    <%= request.getAttribute("mnsError") != null ? request.getAttribute("mnsError") : " "%>
                    <%= request.getAttribute("mnsAdd") != null ? request.getAttribute("mnsAdd") : " "%>
                </p>
                <h2>Seleccione un libro</h2>

                <select name="libroSeleccionado" size="6">
                    <option value="12.95-EL PERRO DE LOS BASQUERVILLE" selected>EL PERRO DE LOS BASQUERVILLE</option>
                    <option value="15.00-LOBOS">LOBOS</option>
                    <option value="22.35-LA VIDA ES BELLA">LA VIDA ES BELLA</option>
                    <option value="8.95-LA LLAMADA DE LO SALVAJE">LA LLAMADA DE LO SALVAJE</option>
                    <option value="82.45-JUEGO DE TRONOS">JUEGO DE TRONOS</option>
                    <option value="13.95-EL RESPLANDOR">EL RESPLANDOR</option>
                </select>
                <div class="container-cantidad">
                    <label for="cantidad">Cantidad: </label>
                    <input id="cantidad" name="cantidad" type="number" min="1" placeholder="0">
                </div>
                <div class="container-btn">
                    <button name="enviar" value="add" type="submit" class="btn">Añadir al carrito</button>
                    <button name="enviar" value="show"  type="submit" 
                            <%= (Boolean) request.getAttribute("existeCarrito")? "class='btn'" : "class='btn btn-disabled' disabled"%>
                            >Ver carrito</button>
                    <button name="enviar" value="pay" type="submit"
                            <%= (Boolean) request.getAttribute("existeCarrito")? "class='btn'" : "class='btn btn-disabled' disabled"%>
                            >Finalizar compra</button>
                </div>
            </form>
        </main>
        <%@include file="../INCLUDES/footer.inc" %>
    </body>

</html>
