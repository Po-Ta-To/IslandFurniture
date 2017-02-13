/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
import HelperClasses.ShoppingCartLineItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author USER
 */
@WebServlet(name = "ECommerce_PaymentServlet", urlPatterns = {"/ECommerce_PaymentServlet"})
public class ECommerce_PaymentServlet extends HttpServlet {

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
            
            boolean isError = false;
            String errorMsg = "";
            
            String name = request.getParameter("txtName");
            String cardno = request.getParameter("txtCardNo");
            String securitycode = request.getParameter("txtSecuritycode");
            String month = request.getParameter("month");
            String year = request.getParameter("year");
            
            double price = Double.parseDouble(request.getParameter("finalPrice"));
            
            try{
                Long card = Long.parseLong(cardno);
            } catch (Exception e) {
                errorMsg = "Error with card number!";
                isError = true;
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + URLEncoder.encode(errorMsg));
            }
            
            String firstdigit = cardno.substring(0, 1);
            
            String seconddigit = cardno.substring(1, 2);
            
            System.out.print("1stdigit : " + firstdigit + " 2nddigit : " + seconddigit);
            
            String cardbrand = "";
            
            if(cardno.length() == 16 && firstdigit.equals("5")) {
                int second = Integer.parseInt(seconddigit);
                if(second >= 1 && second <= 5) {
                    cardbrand = "MASTERCARD";
                }
            } else {
                if(firstdigit.equals("2")) {
                    int second = Integer.parseInt(seconddigit);
                    if(second >= 2 && second <= 7) {
                    cardbrand = "MASTERCARD";
                    }
                } else if(cardno.length() <= 19 && firstdigit.equals("4")) {
                    cardbrand = "VISA";
                }
                isError = true;
                errorMsg = "Error with card!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + URLEncoder.encode(errorMsg));
            }  
            
            try{
                int cvv = Integer.parseInt(securitycode);
            } catch (Exception e) {
                isError = true;
                errorMsg = "Error with cvv!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + URLEncoder.encode(errorMsg));
            }
            
            if(securitycode.length() != 3) {
                isError = true;
                errorMsg = "Error with cvv!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + URLEncoder.encode(errorMsg));
            }
            
            int yearInt = Integer.parseInt(year);
            
            int monthInt = 0;
            if(month.equals("January")) {
                monthInt = 1;
            }else if(month.equals("February")) {
                monthInt = 2;
            }else if(month.equals("March")) {
                monthInt = 3;
            }else if(month.equals("April")) {
                monthInt = 4;
            }else if(month.equals("May")) {
                monthInt = 5;
            }else if(month.equals("June")) {
                monthInt = 6;
            }else if(month.equals("July")) {
                monthInt = 7;
            }else if(month.equals("August")) {
                monthInt = 8;
            }else if(month.equals("September")) {
                monthInt = 9;
            }else if(month.equals("October")) {
                monthInt = 10;
            }else if(month.equals("November")) {
                monthInt = 11;
            }else if(month.equals("December")) {
                monthInt = 12;
            }
            
            Calendar c = Calendar.getInstance();
            int currYear = c.get(Calendar.YEAR);
            int currMonth = c.get(Calendar.MONTH);
            
            if(currYear > yearInt) {
                isError = true;
                errorMsg = "Card expired!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + URLEncoder.encode(errorMsg));
            } else if (currYear == yearInt) {
                if(currMonth > monthInt) {
                    isError = true;
                    errorMsg = "Card expired!";
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + URLEncoder.encode(errorMsg));
                }
            }
            
            HttpSession session = request.getSession();
            
            ArrayList<ShoppingCartLineItem> shoppingCart = new ArrayList<ShoppingCartLineItem>();

            shoppingCart = (ArrayList<ShoppingCartLineItem>)session.getAttribute("shoppingCart");

            String memberEmail = (String)session.getAttribute("memberEmail");

            Long storeID = 10001L;

            for(ShoppingCartLineItem item : shoppingCart) {
                int inStorage = getQuantity(storeID, item.getSKU());
                int toPurchase = item.getQuantity();

                if(inStorage < toPurchase) {
                    isError = true;
                    errorMsg = "Out of Stock! Item: " + item.getName();
                    response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + URLEncoder.encode(errorMsg));
                    break;
                }

            }
            
            if(!isError) {
                

                ArrayList<String> ids = new ArrayList<String>();

                for(ShoppingCartLineItem item : shoppingCart) {
                    int inStorage = getQuantity(storeID, item.getSKU());
                    int toPurchase = item.getQuantity();
                    int newQty = inStorage - toPurchase;
                    ids.add(setQuantity(storeID, item.getSKU(), newQty));
                }

                Long memberId = getMemberId(memberEmail);

                String key = putTransRecord(memberId, price, 25L);

                for(String id : ids) {
                    putItemRecord(Long.parseLong(key), id);
                }

                shoppingCart = new ArrayList<ShoppingCartLineItem>();

                session.setAttribute("shoppingCart", shoppingCart);

                String goodMsg = "Payment Success!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?goodMsg=" + URLEncoder.encode(goodMsg));
            } else {
                errorMsg = "Something went wrong!";
                response.sendRedirect("/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=" + URLEncoder.encode(errorMsg));
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
    
    public String setQuantity(Long storeID, String SKU, int qty) {
        try {
            System.out.println("getQuantity() SKU: " + SKU);
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.furnitureentity")
                    .path("updateFurnitureQty")
                    .queryParam("storeID", storeID)
                    .queryParam("SKU", SKU)
                    .queryParam("qty", qty);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(null);
            System.out.println("status: " + response.getStatus());
            String result = response.readEntity(String.class);
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String putTransRecord(Long memberID, Double amountPaid, Long countryID) {
        try {
            
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/commerce")
                    .path("createECommerceTransactionRecord")
                    .queryParam("memberID", memberID)
                    .queryParam("amountPaid", amountPaid)
                    .queryParam("countryID", countryID);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.put(Entity.json(""));
            System.out.println("status: " + response.getStatus());
            
            String result = response.readEntity(String.class);
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Long getMemberId(String memberEmail) {
        try {
            
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity").path("getMember")
                .queryParam("memberEmail", memberEmail);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            System.out.println("status: " + response.getStatus());
            
            Member result = response.readEntity(Member.class);
            
            return result.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    public void putItemRecord(Long salesRecordID, String id) {
        try {
            
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                .target("http://localhost:8080/IS3102_WebService-Student/webresources/commerce").path("createECommerceLineItemRecord")
                .queryParam("salesRecordID", salesRecordID)
                .queryParam("id", id);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.put(Entity.json(""));
            System.out.println("status: " + response.getStatus());
            
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        
    }

}
