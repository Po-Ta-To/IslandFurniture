/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import EntityManager.CountryEntity;
import HelperClasses.Furniture;
import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author USER
 */
@WebServlet(name = "ECommerce_AddFurnitureToListServlet", urlPatterns = {"/ECommerce_AddFurnitureToListServlet"})
public class ECommerce_AddFurnitureToListServlet extends HttpServlet {

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
            //String storeIDstring = request.getParameter("storeID");
            String SKU = request.getParameter("SKU");
            //String id = request.getParameter("id");
            String price = request.getParameter("price");
            String name = request.getParameter("name");
            String imageURL = request.getParameter("imageURL");
            
            if (SKU == null || SKU.equals("")) {
                response.sendRedirect("/IS3102_Project-war/B/SG/index.jsp");
            }            

            Long storeID = 10001L;
            
            int itemQty = getQuantity(storeID, SKU);
            
            HttpSession session = request.getSession();
            
            //List<Furniture> furnitures = (List<Furniture>) (session.getAttribute("furnitures"));
            
            ArrayList<ShoppingCartLineItem> shoppingCart = new ArrayList<ShoppingCartLineItem>();
            
            shoppingCart = (ArrayList<ShoppingCartLineItem>)session.getAttribute("shoppingCart");
            
            ShoppingCartLineItem addNewItem = new ShoppingCartLineItem();
            
            boolean addCart = false;
            int qty = 1;
                
            if(shoppingCart == null) {
                shoppingCart = new ArrayList<ShoppingCartLineItem>();
            } else {
                for(int i = 0; i < shoppingCart.size(); i++) {
                    if(shoppingCart.get(i).getSKU().equals(SKU)) { //already in cart
                        
                        qty += shoppingCart.get(i).getQuantity();
                        
                        if(itemQty >= qty) {
                            shoppingCart.remove(i);
                        }
                        
                        break;
                    }
                }
                
                
                
            }
            
            if(itemQty < qty) {
                String errorMsg = "Out of Stock!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + URLEncoder.encode(errorMsg));
            } else {
                //addNewItem.setId(id);
                addNewItem.setSKU(SKU);
                addNewItem.setName(name);
                addNewItem.setImageURL(imageURL);
                addNewItem.setPrice(Double.parseDouble(price));
                addNewItem.setQuantity(qty);
                //addNewItem.setCountryID();

                shoppingCart.add(addNewItem);

                session.setAttribute("shoppingCart", shoppingCart);

                String goodMsg = "Item added!";
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
    
    public int getQuantity(Long storeID, String SKU) {
        try {
            System.out.println("getQuantity() SKU: " + SKU);
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.storeentity")
                    .path("getQuantity")
                    .queryParam("storeID", storeID)
                    .queryParam("SKU", SKU);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println("status: " + response.getStatus());
            if (response.getStatus() != 200) {
                return 0;
            }
            String result = (String) response.readEntity(String.class);
            System.out.println("Result returned from ws: " + result);
            return Integer.parseInt(result);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    

}
