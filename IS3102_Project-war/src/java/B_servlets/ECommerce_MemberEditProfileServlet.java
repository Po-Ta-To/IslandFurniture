/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package B_servlets;

import HelperClasses.Member;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author USER
 */
@WebServlet(name = "ECommerce_MemberEditProfileServlet", urlPatterns = {"/ECommerce_MemberEditProfileServlet"})
public class ECommerce_MemberEditProfileServlet extends HttpServlet {

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
        try {
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            //String country = request.getParameter("country");
            String address = request.getParameter("address");
            String securityAnswer = request.getParameter("securityAnswer");
            Integer securityQuestion = Integer.parseInt(request.getParameter("securityQuestion"));
            Integer age = Integer.parseInt(request.getParameter("age"));
            Integer income = Integer.parseInt(request.getParameter("income"));
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
//            System.out.println("ECommerce_MemberEditProfileServlet name: " + name);
//            System.out.println("ECommerce_MemberEditProfileServlet phone: " + phone);
//            System.out.println("ECommerce_MemberEditProfileServlet address: " + address);
//            System.out.println("ECommerce_MemberEditProfileServlet securityAnswer: " + securityAnswer);
//            System.out.println("ECommerce_MemberEditProfileServlet securityQuestion: " + securityQuestion);
//            System.out.println("ECommerce_MemberEditProfileServlet age: " + age);
//            System.out.println("ECommerce_MemberEditProfileServlet income: " + income);
//            System.out.println("ECommerce_MemberEditProfileServlet email: " + email); 
            
            Client client = ClientBuilder.newClient();
            WebTarget target = client
                    .target("http://localhost:8080/IS3102_WebService-Student/webresources/entity.memberentity").path("postMember")
                    .queryParam("email", email)
                    .queryParam("name", name)
                    .queryParam("phone", phone)
                    .queryParam("address", address)
                    .queryParam("securityAnswer", securityAnswer)
                    .queryParam("securityQuestion", securityQuestion)
                    .queryParam("age", age)
                    .queryParam("income", income)
                    .queryParam("password", password);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response res = invocationBuilder.post(null);
            
            System.out.println("ECommerce_MemberEditProfileServlet status: " + res.getStatus());
            
            
            if (res.getStatus() != 200) {
                response.sendRedirect("/IS3102_Project-war/B/SG/memberLogin.jsp");
            }else{
                response.sendRedirect("/IS3102_Project-war/ECommerce_GetMember");
            }
            
            
        } catch (Exception e) {
            PrintWriter out = response.getWriter();
            out.println(e);
            e.printStackTrace();
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
