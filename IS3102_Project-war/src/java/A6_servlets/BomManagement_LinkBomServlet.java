package A6_servlets;

import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BomManagement_LinkBomServlet extends HttpServlet {

    @EJB
    private ItemManagementBeanLocal itemManagementBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String id = request.getParameter("id");
            String furnitureId = request.getParameter("furnitureId" + id);
            if (furnitureId.equals("")) {
                result = "?errMsg=Please try again.";
                response.sendRedirect("BomManagement_BomServlet" + result);
            }
            System.out.println("BOMId: " + id);
            System.out.println("FurnitureId: " + furnitureId);

            boolean success = itemManagementBean.linkBOMAndFurniture(Long.parseLong(id), Long.parseLong(furnitureId));
            if (!success) {
                result = "?errMsg=Please try again.";
                response.sendRedirect("BomManagement_BomServlet" + result);
            } else {
                result = "?goodMsg=BOM linked successfully.";
                response.sendRedirect("BomManagement_BomServlet" + result);
            }
        } catch (Exception ex) {
            out.println(ex);

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
