package A3_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import EntityManager.RegionalOfficeEntity;
import EntityManager.StaffEntity;
import EntityManager.SupplierEntity;
import SCM.SupplierManagement.SupplierManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SupplierManagement_SupplierServlet extends HttpServlet {

    @EJB
    private SupplierManagementBeanLocal supplierManagementBean;
    @EJB
    private AccountManagementBeanLocal accountManagementBean;
    @EJB
    private FacilityManagementBeanLocal facilityManagementBean;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession();
            String errMsg = request.getParameter("errMsg");
            String goodMsg = request.getParameter("goodMsg");
            List<SupplierEntity> suppliers = new ArrayList<>();
            StaffEntity staffEntity = (StaffEntity) session.getAttribute("staffEntity");
            List<RegionalOfficeEntity>  listOfRegionalOffice = facilityManagementBean.viewListOfRegionalOffice();
            session.setAttribute("listOfRegionalOffice", listOfRegionalOffice);
            if (accountManagementBean.checkIfStaffIsAdministrator(staffEntity.getId()) || accountManagementBean.checkIfStaffIsGlobalManager(staffEntity.getId())) {
                suppliers = supplierManagementBean.viewAllSupplierList();
            } else if (accountManagementBean.checkIfStaffIsRegionalManager(staffEntity.getId()) || accountManagementBean.checkIfStaffIsPurchasingManager(staffEntity.getId())) {
                Long roID = accountManagementBean.getRegionalOfficeIdBasedOnStaffRole(staffEntity.getId());
                if (roID != null) {
                    suppliers = supplierManagementBean.getSupplierListOfRO(roID);
                }
            }
            session.setAttribute("suppliers", suppliers);

            if (errMsg == null && goodMsg == null) {
                response.sendRedirect("A3/supplierManagement.jsp");
            } else if ((errMsg != null) && (goodMsg == null)) {
                if (!errMsg.equals("")) {
                    response.sendRedirect("A3/supplierManagement.jsp?errMsg=" + errMsg);
                }
            } else if ((errMsg == null && goodMsg != null)) {
                if (!goodMsg.equals("")) {
                    response.sendRedirect("A3/supplierManagement.jsp?goodMsg=" + goodMsg);

                }
            }
        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
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
