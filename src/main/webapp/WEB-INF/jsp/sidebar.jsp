<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Sidebar -->
<div id="sidebar" class="d-flex flex-column flex-shrink-0 p-3 bg-dark text-white vh-100" style="width: 250px; position: fixed;">
    <a href="${pageContext.request.contextPath}/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
        <span class="fs-4">Clinic System</span>
    </a>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="#" class="nav-link text-white">
                <img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" alt="" width="90" height="90" class="rounded-circle me-2">
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-link text-white">
                <i class="bi bi-speedometer2 me-2"></i> Dashboard
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/patients" class="nav-link text-white">
                <i class="bi bi-people me-2"></i> Patients
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/appointments" class="nav-link text-white">
                <i class="bi bi-calendar-check me-2"></i> Appointments
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/doctors" class="nav-link text-white">
                <i class="bi bi-person-badge me-2"></i> Doctors
            </a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/reports" class="nav-link text-white">
                <i class="bi bi-file-earmark-text me-2"></i> Reports
            </a>
        </li>
    </ul>
    <hr>
    <div class="dropdown">
        <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
            <img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" alt="" width="32" height="32" class="rounded-circle me-2">
            <strong>Admin</strong>
        </a>
        <ul class="dropdown-menu dropdown-menu-dark text-small shadow" aria-labelledby="dropdownUser1">
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Sign out</a></li>
        </ul>
    </div>
</div>
