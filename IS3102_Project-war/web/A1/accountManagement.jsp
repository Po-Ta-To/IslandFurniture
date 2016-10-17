<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Account Management</h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-users"></i> Account Management
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->
                    <div class="row featured-boxes">
                        <div class="col-md-12">
                            <div class="col-md-4">
                                <div class="featured-box featured-box-secundary">
                                    <div class="box-content">
                                        <a href="../StaffManagement_StaffServlet"><i class="icon-featured icon icon-user"> </i>
                                            <h4>Staff Management</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="featured-box featured-box-quartenary">
                                    <div class="box-content">
                                        <a href="../RoleManagement_RoleServlet"><i class="icon-featured icon icon-user"> </i>
                                            <h4>Roles Management</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="featured-box featured-box-primary">
                                    <div class="box-content">
                                        <a href="../MemberManagement_MemberServlet"><i class="icon-featured icon icon-user"></i>
                                        <h4>Member Management</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.container-fluid -->
            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>
    </body>
</html>
