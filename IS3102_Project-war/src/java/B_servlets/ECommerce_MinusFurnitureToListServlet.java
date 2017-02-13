/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author USER
 */
@WebServlet(name = "ECommerce_MinusFurnitureToListServlet_1", urlPatterns = {"/ECommerce_MinusFurnitureToListServlet"})
public class ECommerce_MinusFurnitureToListServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            
            String SKU = request.getParameter("SKU");
            
            HttpSession session = request.getSession();
            
            ArrayList<ShoppingCartLineItem> shoppingCart = new ArrayList<ShoppingCartLineItem>();
            
            shoppingCart = (ArrayList<ShoppingCartLineItem>)session.getAttribute("shoppingCart");
            
            boolean isError = true;
            
            for(int i = 0; i < shoppingCart.size(); i++) {
                if(shoppingCart.get(i).getSKU().equals(SKU)) { //already in cart

                    int qty;
                    qty = shoppingCart.get(i).getQuantity() - 1;
                    
                    if(qty == 0) {
                        shoppingCart.remove(i);
                    } else {
                        shoppingCart.get(i).setQuantity(qty);
                    }
                    
                    isError = false;
                    break;
                }
            }
            
            session.setAttribute("shoppingCart", shoppingCart);
            
            if(isError) {
                String errorMsg = "Error!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + URLEncoder.encode(errorMsg));
            } else {
                String goodMsg = "Item removed!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + URLEncoder.encode(goodMsg));
            }
            

        } catch (Exception ex) {
            out.println(ex);
            ex.printStackTrace();
        }
    }

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
        processRequest(request, response);
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
        processRequest(request, response);
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
