<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Patient</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        .patient-card {
            max-width: 700px;
            margin: 40px auto;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            background-color: #ffffff;
            padding: 30px;
        }

        .profile-photo {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #0d6efd;
        }

        .patient-details {
            margin-left: 30px;
        }

        .detail-label {
            font-weight: bold;
            color: #495057;
        }
    </style>
</head>
<body class="bg-light">

<jsp:include page="navbar.jsp" />
<jsp:include page="sidebar.jsp" />

<div class="container">
    <div class="patient-card d-flex align-items-center">
        <div>
            <c:choose>
                <c:when test="${not empty patient.photo}">
                    <img src="${pageContext.request.contextPath}/uploads/${patient.photo}" alt="Profile Photo" class="profile-photo">
                </c:when>
                <c:otherwise>
                    <img src="https://cdn-icons-png.flaticon.com/512/847/847969.png" alt="Default Icon" class="profile-photo">
                </c:otherwise>
            </c:choose>
        </div>

        <div class="patient-details">
            <h3 class="mb-3 text-primary">${patient.name}</h3>
            <p><span class="detail-label">Email:</span> ${patient.email}</p>
            <p><span class="detail-label">Phone:</span> ${patient.phone}</p>
            <p><span class="detail-label">Gender:</span> ${patient.gender}</p>
            <p><span class="detail-label">Date of Birth:</span> ${patient.dob}</p>
            <p><span class="detail-label">Address:</span> ${patient.address}</p>
        </div>
    </div>

    <div class="text-center mt-4">
        <a href="${pageContext.request.contextPath}/patients" class="btn btn-secondary px-4">Back to List</a>
        <a href="${pageContext.request.contextPath}/patients/edit/${patient.id}" class="btn btn-primary px-4">Edit</a>
        <a href="${pageContext.request.contextPath}/patients/delete/${patient.id}"
           class="btn btn-danger px-4"
           onclick="return confirm('Are you sure you want to delete this patient?');">Delete</a>
    </div>
</div>

</body>
</html>