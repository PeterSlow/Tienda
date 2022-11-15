/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.controllers;

import es.albarregas.beans.BeanBook;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dcc_s
 */
@WebServlet(name = "Productos", urlPatterns = {"/productos"})
public class Productos extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("JSP/tienda.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<BeanBook> coleccionLibros = new ArrayList<>();

        String url = "JSP/tienda.jsp";

        String btn = request.getParameter("enviar");

        String mnsError = null;

        String mnsAdd = null;

        Cookie[] co = request.getCookies();

        Cookie carrito_cookie = comprobarCookieUsuario(co);

        StringBuilder formatoCarritoCookie = new StringBuilder();
        
        String [] componentesLibro = request.getParameter("libroSeleccionado").split("-");

        if (carrito_cookie != null) {

            coleccionLibros = cargarColeccionLibrosConCookie(carrito_cookie, coleccionLibros);

            switch (btn) {
                case "add":
                    if (request.getParameter("libroSeleccionado") != null && !request.getParameter("cantidad").equals("")) {
                        String unidades = request.getParameter("cantidad").equals("1")? " unidad del libro " : " unidades del libro";
                        coleccionLibros = sumarLibroAColeccionConCarga(coleccionLibros, request.getParameter("libroSeleccionado"), request.getParameter("cantidad"));
                        mnsAdd = "Ha añadido un producto a la cesta." + "<br>" + request.getParameter("cantidad") + unidades + "\"" + componentesLibro[1] + "\"";
                    } else {
                        mnsError = "Debe seleccionar un libro y una cantidad.";
                    }
                    break;
                case "show":
                    url = "JSP/carrito.jsp";
                    break;
                case "pay":
                    double total = 0;
                    if (coleccionLibros != null && !coleccionLibros.isEmpty()) {
                        for (BeanBook book : coleccionLibros) {
                            total += Math.round(book.getCantidad() * book.getPrecio() * 10) / 10D;
                        }
                    }
                    request.setAttribute("total", total);
                    url = "JSP/factura.jsp";
                    break;
            }

        } else {

            if (btn.equals("add")) {
                if (request.getParameter("libroSeleccionado") != null && !request.getParameter("cantidad").equals("")) {
                    String unidades = request.getParameter("cantidad").equals("1")? " unidad del libro " : " unidades del libro ";
                    coleccionLibros = sumarLibroAColeccionVacia(coleccionLibros, request.getParameter("libroSeleccionado"), request.getParameter("cantidad"));
                    mnsAdd = "Ha añadido un producto a la cesta." + "<br>" + request.getParameter("cantidad") + unidades + "\"" + componentesLibro[1] + "\"";
                } else {
                    mnsError = "Debe seleccionar un libro y una cantidad.";
                }
            }
        }

        /*
        Esta condición es necesaria dado que si falta alguno de los dos parámetros por seleccionar, la colección será en la
        única ocasión en que no se cargue. De no existir, provocaríamos excepciones.
         */
        if (coleccionLibros != null && !coleccionLibros.isEmpty()) {

            request.getSession().setAttribute("carrito_sesion", coleccionLibros);

            for (BeanBook elemento : coleccionLibros) {
                formatoCarritoCookie.append(elemento.getPrecio()).append("-").append(elemento.getNombre()).append("-").append(elemento.getCantidad()).append("/");
            }

            carrito_cookie = new Cookie("carrito_cookie", URLEncoder.encode(formatoCarritoCookie.toString(), "UTF-8"));
            carrito_cookie.setMaxAge(3600 * 24);
            response.addCookie(carrito_cookie);

            request.setAttribute("existeCarrito", true);
        } else {
            request.setAttribute("existeCarrito", false);
        }

        request.setAttribute("mnsError", mnsError);
        request.setAttribute("mnsAdd", mnsAdd);

        request.getRequestDispatcher(url).forward(request, response);

    }

    public static ArrayList<BeanBook> sumarLibroAColeccionVacia(ArrayList<BeanBook> coleccionLibros, String libroSeleccionado, String cantidadSeleccionada) {
        String[] componentesLibro = libroSeleccionado.split("-");
        Short cantidad = Short.parseShort(cantidadSeleccionada);
        BeanBook libro = new BeanBook();
        libro.setPrecio(Double.parseDouble(componentesLibro[0]));
        libro.setNombre(componentesLibro[1]);
        libro.setCantidad(cantidad);
        coleccionLibros.add(libro);
        return coleccionLibros;
    }

    public static ArrayList<BeanBook> sumarLibroAColeccionConCarga(ArrayList<BeanBook> coleccionLibros, String libroSeleccionado, String cantidadSeleccionada) {

        String[] componentesLibro = libroSeleccionado.split("-");
        Short cantidad = Short.parseShort(cantidadSeleccionada);
        Boolean libroRepetido = false;

        for (BeanBook book : coleccionLibros) {
            if (book.getNombre().equals(componentesLibro[1])) {
                book.setCantidad((short) ((short) book.getCantidad() + cantidad));
                libroRepetido = true;
                break;
            }
        }

        if (!libroRepetido) {
            BeanBook libro = new BeanBook();
            libro.setPrecio(Double.parseDouble(componentesLibro[0]));
            libro.setNombre(componentesLibro[1]);
            libro.setCantidad(cantidad);
            coleccionLibros.add(libro);
        }

        return coleccionLibros;
    }

    public static ArrayList<BeanBook> cargarColeccionLibrosConCookie(Cookie cookie, ArrayList<BeanBook> coleccionLibros) throws UnsupportedEncodingException {

        String carritoDecoder = URLDecoder.decode(cookie.getValue(), "utf8");
        String[] coleccionLibrosEnCookie = carritoDecoder.split("/");

        for (String libroCookie : coleccionLibrosEnCookie) {
            String[] libroCarrito = libroCookie.split("-");
            BeanBook libro = new BeanBook();
            libro.setPrecio(Double.parseDouble(libroCarrito[0]));
            libro.setNombre(libroCarrito[1]);
            libro.setCantidad(Short.parseShort(libroCarrito[2]));
            coleccionLibros.add(libro);
        }

        return coleccionLibros;
    }

    public static Cookie comprobarCookieUsuario(Cookie[] co) {

        Cookie cookie = null;

        if (co != null) {
            for (Cookie c : co) {
                if (c.getName().equals("carrito_cookie")) {
                    cookie = c;
                    break;
                }
            }
        }

        return cookie;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
