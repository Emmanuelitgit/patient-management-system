<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Patient List</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
    <jsp:include page="navbar.jsp" />
    <jsp:include page="sidebar.jsp" />
<div class="" style="margin-left: 87%; margin-bottom: 1%; margin-top: 1%">
    <a href="${pageContext.request.contextPath}/view/addPatientForm" class="btn btn-primary">
        <i class="bi bi-person-plus"></i> Add New Patient
    </a>
</div>
    <div class="container mt-2">

        <table class="table table-bordered shadow-sm align-middle table-striped"
               style="margin-left: 15%; width: 90%">
            <thead class="table-dark text-center">
            <tr>
                <th>#</th>
                <th>Full Name</th>
                <th>Date of Birth</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody class="text-center">
            <c:forEach var="patient" items="${patients}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${patient.name}</td>
                    <td>${patient.dob}</td>
                    <td>${patient.email}</td>
                    <td>${patient.phone}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/view/viewPatient/${patient.id}"
                           class="btn btn-sm btn-primary me-1">
                            <i class="bi bi-eye"></i> View
                        </a>

                        <a href="${pageContext.request.contextPath}/view/updatePatient/${patient.id}"
                           class="btn btn-sm btn-success me-1">
                            <i class="bi bi-pencil-square"></i> Edit
                        </a>

                        <a href="${pageContext.request.contextPath}/view/deletePatient/${patient.id}"
                           class="btn btn-sm btn-danger"
                           onclick="return confirm('Are you sure you want to delete this patient?');">
                            <i class="bi bi-trash"></i> Delete
                        </a>

                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    </body>
</html>