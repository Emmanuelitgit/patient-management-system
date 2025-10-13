<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Patient Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">

<div class="container mt-5">
    <h2 class="mb-4">Update Patient</h2>

    <form:form modelAttribute="patient" action="/pms/view/updatePatient" method="post" class="needs-validation">

        <div class="mb-3 w-50 hidden">
            <form:label path="id" cssClass="form-label hidden">Full Name<span class="text-danger">*</span></form:label>
        </div>

        <div class="mb-3 w-50">
            <form:label path="name" cssClass="form-label">Full Name<span class="text-danger">*</span></form:label>
            <form:input path="name" required="required" cssClass="form-control p-2" placeholder="Enter full name" />
        </div>

        <div class="mb-3 w-50">
            <form:label path="email" cssClass="form-label">
                Email<span class="text-danger">*</span>
            </form:label>
            <form:input path="email" required="required" cssClass="form-control p-2" placeholder="Enter email address" />
        </div>

        <div class="mb-3 w-50">
            <form:label path="phone" cssClass="form-label">
                Phone<span class="text-danger">*</span>
            </form:label>
            <form:input path="phone" cssClass="form-control p-2" placeholder="Enter phone number" />
        </div>

        <div class="mb-3 w-50">
            <form:label path="dob" cssClass="form-label">
                Date of Birth<span class="text-danger">*</span>
            </form:label>
            <form:input path="dob" required="required" type="date" cssClass="form-control p-2" placeholder="Enter date of birth" />
        </div>

        <div class="mb-3 w-50">
            <form:label path="gender" cssClass="form-label">
                Gender<span class="text-danger">*</span>
            </form:label>
            <form:select path="gender" required="required" cssClass="form-control p-2">
                <form:option value="Male">Male</form:option>
                <form:option value="Female">Female</form:option>
            </form:select>
        </div>

        <div class="mb-3 w-50">
            <form:label path="address" cssClass="form-label">Address</form:label>
            <form:input path="address" cssClass="form-control p-2" placeholder="Enter address" />
        </div>

        <div class="mb-3 w-50">
            <form:label path="address" cssClass="form-label">Medical History</form:label>
            <form:textarea path="medicalHistory" cssClass="form-control p-2" placeholder="Enter medical history"/>
        </div>

        <button type="submit" class="btn btn-primary p-2 mb-5">Save Patient</button>
    </form:form>
</div>

</body>
</html>