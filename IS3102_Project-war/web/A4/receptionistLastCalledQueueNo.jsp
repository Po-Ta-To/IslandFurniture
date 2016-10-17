<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.PickRequestEntity"%>
<%@ page import="java.io.*,java.util.*" %>
<%
    StaffEntity staff = (StaffEntity) (session.getAttribute("receptionist"));
    if (staff == null) {
%>
<jsp:forward page="../A1/staffLogin.jsp?errMsg=Session Expired." />
<% }%>
<html>
    <head>
        <jsp:include page="../header1.html" />
        <meta http-equiv="refresh" content="3; url=../ReceptionistLastCalled_Servlet">
    </head>
    <body class="dark">


        <div role="main" class="main">

            <div class="header-container">
                <div class="row" style="background-color : rgb(153, 0, 0); margin-bottom: 50px" >
                    <div class="col-md-4 col-md-offset-4">  
                        <img class="center-block img-responsive"  src="../img/logo-label.png" style="margin-top: 20px; margin-bottom: 20px;">
                    </div>
                </div>
            </div>


            <div class="container">

                <div class="row">
                    <table class="table table-striped table-bordered" i>
                        <thead>
                            <tr>
                                <th>Queue No.</th>
                                <th>Status</th>
                                <th>Trolley</th>
                                <th>Date/Time Called</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<PickRequestEntity> pickRequests = (List<PickRequestEntity>) (session.getAttribute("pickRequests"));
                                if (pickRequests != null) {
                                    for (int i = 0; i < pickRequests.size(); i++) {
                            %>
                            <tr>
                                <td>
                                    <%= pickRequests.get(i).getQueueNo()%>
                                </td>
                                <td>
                                    <%=pickRequests.get(i).getCollectionStatus()%>
                                </td>
                                <td>
                                    <% StaffEntity picker = pickRequests.get(i).getPicker();
                                        if (picker != null) {
                                            out.println(picker.getName());
                                        } else {
                                            out.println("Unassigned");
                                        }
                                    %>
                                </td>
                                <td>
                                    <% Date dateCalled =  pickRequests.get(i).getDateCalled();
                                    if (dateCalled!=null) {
                                        String date = pickRequests.get(i).getDateCalled().toString();
                                        out.println(date);
                                    } else {
                                        out.println("Please wait...");
                                    }
                                    %>
                                </td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                    </table>

                    <hr class="tall">
                </div>
            </div>
        </div>
    </body>
</html>
