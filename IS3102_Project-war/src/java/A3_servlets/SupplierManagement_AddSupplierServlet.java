package A3_servlets;

import EntityManager.StaffEntity;
import SCM.SupplierManagement.SupplierManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SupplierManagement_AddSupplierServlet extends HttpServlet {

    @EJB
    private SupplierManagementBeanLocal supplierManagementBean;

    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String country = request.getParameter("country");
            String roId = request.getParameter("regionalOffice");
            HttpSession session = request.getSession();
            StaffEntity staffEntity = (StaffEntity) session.getAttribute("staffEntity");
            if (roId == null) {
                result = "?errMsg=No access right to create a supplier..";
                response.sendRedirect("SupplierManagement_SupplierServlet" + result);
            }
            supplierManagementBean.addSupplier(name, phone, email, address, Long.parseLong(country),Long.parseLong(roId));
            result = "?goodMsg=Supplier added successfully.";
            response.sendRedirect("SupplierManagement_SupplierServlet" + result);

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
