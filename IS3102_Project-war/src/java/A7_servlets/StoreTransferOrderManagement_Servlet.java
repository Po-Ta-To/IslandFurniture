package A7_servlets;

import EntityManager.StorageBinEntity;
import EntityManager.TransferOrderEntity;
import EntityManager.WarehouseEntity;
import InventoryManagement.StoreAndKitchenInventoryManagement.StoreAndKitchenInventoryManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class StoreTransferOrderManagement_Servlet extends HttpServlet {

    @EJB
    private StoreAndKitchenInventoryManagementBeanLocal simbl;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session;
            session = request.getSession();
            String errMsg = request.getParameter("errMsg");
            String goodMsg = request.getParameter("goodMsg");
            WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));

            if (warehouseEntity == null) {
                response.sendRedirect("A7/storeWarehouseManagement_view.jsp");
            } else {
                List<StorageBinEntity> storageBins = simbl.viewAllStorageBin(warehouseEntity.getId());
                session.setAttribute("storageBins", storageBins);

                List<TransferOrderEntity> transferOrders = simbl.viewAllTransferOrderByWarehouseId(warehouseEntity.getId());
                session.setAttribute("transferOrders", transferOrders);

                if (errMsg == null && goodMsg == null) {
                    response.sendRedirect("A7/transferOrderManagement.jsp");
                } else if ((errMsg != null) && (goodMsg == null)) {
                    if (!errMsg.equals("")) {
                        response.sendRedirect("A7/transferOrderManagement.jsp?errMsg=" + errMsg);
                    }
                } else if ((errMsg == null && goodMsg != null)) {
                    if (!goodMsg.equals("")) {
                        response.sendRedirect("A7/transferOrderManagement.jsp?goodMsg=" + goodMsg);
                    }
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
