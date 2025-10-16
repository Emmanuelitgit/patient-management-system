package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Prescription;

public interface PrescriptionService {
    ResponseEntity<ResponseDTO> findAll(String search,Integer size, Integer page);
    ResponseEntity<ResponseDTO> findById(String id);
    ResponseEntity<ResponseDTO> addPrescription(Prescription prescription);
    ResponseEntity<ResponseDTO> updateById(Prescription prescription);
    ResponseEntity<ResponseDTO> deleteById(String id);
    ResponseEntity<ResponseDTO> fetchPrescriptionsForPractitioner(String practitionerId, String search, Integer size, Integer page);
    ResponseEntity<ResponseDTO> updatePrescriptionStatus(String id);
}
