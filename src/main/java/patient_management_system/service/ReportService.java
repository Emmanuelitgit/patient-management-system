package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import patient_management_system.dto.ResponseDTO;

public interface ReportService {
    ResponseEntity<ResponseDTO> generateReport(Object data);
    ResponseEntity<ResponseDTO> viewReport(Object data);
}
