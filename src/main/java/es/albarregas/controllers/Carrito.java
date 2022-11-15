/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.controllers;

import es.albarregas.beans.BeanBook;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
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
@WebServlet(name = "Carrito", urlPatterns = {"/carrito"})
public class Carrito extends HttpServlet {

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

        request.getRequestDispatcher("JSP/carrito.jsp").forward(request, response);

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

        String url = "JSP/carrito.jsp";
        ArrayList<BeanBook> coleccionLibros = new ArrayList<>();
        coleccionLibros = (ArrayList<BeanBook>) request.getSession().getAttribute("carrito_sesion");
        String btn = request.getParameter("enviar");
        String msnLibroEliminado = null;
        String msnCarritoEliminado = null;
        StringBuilder formatoCarritoCookie = new StringBuilder();
        Cookie[] co = request.getCookies();

        Cookie carrito_cookie = comprobarCookieUsuario(co);

        switch (btn) {
            case "add":
                if (coleccionLibros == null) {
                    request.setAttribute("existeCarrito", false);
                } else {
                    request.setAttribute("existeCarrito", true);
                }
                url = "JSP/tienda.jsp";
                break;
            case "delete":
                if (coleccionLibros != null) {
                    coleccionLibros.removeAll(coleccionLibros);
                    request.getSession().removeAttribute("carrito_sesion");
                    carrito_cookie.setMaxAge(0);
                    response.addCookie(carrito_cookie);
                    msnLibroEliminado = "El carrito ha sido eliminado.<br>Comience a comprar de nuevo.";

                } else {
                    msnLibroEliminado = "No hay productos en su carrito, siga comprando.";
                }
                request.setAttribute("existeCarrito", false);
                break;
            case "pay":
                double total = 0;
                if (coleccionLibros!=null && !coleccionLibros.isEmpty()) {
                    for(BeanBook book :  coleccionLibros){
                        total+=Math.round(book.getCantidad() * book.getPrecio() * 10) / 10D ;
                    }
                }
                request.setAttribute("total", total);
                url = "JSP/factura.jsp";
                break;
            default:
                
                if (coleccionLibros!=null && !coleccionLibros.isEmpty()) {
                    
                    coleccionLibros = eliminarProducto((ArrayList) request.getSession().getAttribute("carrito_sesion"), btn);
                }
                if (coleccionLibros!=null && !coleccionLibros.isEmpty()) {
                    request.getSession().setAttribute("carrito_sesion", coleccionLibros);

                    for (BeanBook elemento : coleccionLibros) {
                        formatoCarritoCookie.append(elemento.getPrecio()).append("-").append(elemento.getNombre()).append("-").append(elemento.getCantidad()).append("/");
                    }

                    carrito_cookie = new Cookie("carrito_cookie", URLEncoder.encode(formatoCarritoCookie.toString(), "UTF-8"));
                    carrito_cookie.setMaxAge(3600 * 24);
                    response.addCookie(carrito_cookie);
                    request.setAttribute("existeCarrito", true);
                    msnLibroEliminado = "Ha eliminado el libro: " + btn;

                } else {
                    request.getSession().invalidate();
                    carrito_cookie = new Cookie("carrito_cookie", "");
                    carrito_cookie.setMaxAge(0);
                    response.addCookie(carrito_cookie);
                    request.setAttribute("existeCarrito", false);
                    msnLibroEliminado = "Ese era el último libro y el carrito ha sido eliminado.<br>Comience a comprar de nuevo.";
                }

                
                break;
        }

        request.setAttribute("msnCarritoEliminado", msnCarritoEliminado);
        request.setAttribute("msnLibroEliminado", msnLibroEliminado);
        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * En este método implementaremos Iterator para evitar errores de concurrencia que se dan cuando se realiza un forEach y se elimina un objeto de la lista:
     * [ java.util.ConcurrentModificationException ]
     * @param coleccionLibros
     * @param libroSeleccionado
     * @return 
     */
    public static ArrayList<BeanBook> eliminarProducto(ArrayList<BeanBook> coleccionLibros, String libroSeleccionado) {
        
        Iterator <BeanBook> it = coleccionLibros.iterator();
        
        while(it.hasNext()){
            String nombre = it.next().getNombre();
            if(nombre.equals(libroSeleccionado)){
                it.remove();
            }
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
