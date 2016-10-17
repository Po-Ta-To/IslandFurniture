package A6_servlets;

import CorporateManagement.ItemManagement.ItemManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductGroupLineItemManagement_RemoveServlet extends HttpServlet {

    @EJB
    private ItemManagementBeanLocal ItemManagementBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        try {
            String productGroupId = request.getParameter("id");

            String[] deleteArr = request.getParameterValues("delete");
            if (deleteArr != null) {
                for (int i = 0; i < deleteArr.length; i++) {
                    ItemManagementBean.removeLineItemFromProductGroup(Long.parseLong(productGroupId), Long.parseLong(deleteArr[i]));
                }
                response.sendRedirect("ProductGroupLineItemManagement_Servlet?goodMsg=Successfully removed: " + deleteArr.length + " record(s).&id=" + productGroupId);
            } else {
                response.sendRedirect("A6/productGroupManagement_Update.jsp?errMsg=Nothing is selected.&id=" + productGroupId);
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
