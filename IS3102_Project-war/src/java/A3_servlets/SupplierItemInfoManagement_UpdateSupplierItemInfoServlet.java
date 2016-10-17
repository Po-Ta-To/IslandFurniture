package A3_servlets;

import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import HelperClasses.ReturnHelper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SupplierItemInfoManagement_UpdateSupplierItemInfoServlet extends HttpServlet {

    @EJB
    private ItemManagementBeanLocal ItemManagementBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String supplierItemId = request.getParameter("id");
            String costPrice = request.getParameter("costPrice");
            String lotSize = request.getParameter("lotSize");
            String leadTime = request.getParameter("leadTime");
            ReturnHelper rh = ItemManagementBean.editSupplierItemInfo(Long.parseLong(supplierItemId), Double.parseDouble(costPrice), Integer.parseInt(lotSize), Integer.parseInt(leadTime));

            if (!rh.getIsSuccess()) {
                result = "?errMsg=" + rh.getMessage() + "&id=" + supplierItemId;
                response.sendRedirect("A3/supplierItemInfoManagement_update.jsp" + result);
            } else {
                result = "?errMsg=" + rh.getMessage();
                response.sendRedirect("SupplierItemInfoManagement_Servlet" + result);
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
