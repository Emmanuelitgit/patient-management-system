package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import patient_management_system.dto.ResponseDTO;

public interface ReportService {
    ResponseEntity<ResponseDTO> generateLabReport(String patientId, String labId);
    ResponseEntity<ResponseDTO> generateInvoiceReport(String paymentId);
    ResponseEntity<ResponseDTO> generatePrescriptionReport(String prescriptionId);
    ResponseEntity<ResponseDTO> uploadReport(MultipartFile multipartFile);
}
