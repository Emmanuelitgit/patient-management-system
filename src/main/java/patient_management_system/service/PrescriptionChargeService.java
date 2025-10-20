package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.PrescriptionCharge;

public interface PrescriptionChargeService {
    ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page);
    ResponseEntity<ResponseDTO> findById(String id);
    ResponseEntity<ResponseDTO> updateById(PrescriptionCharge prescriptionCharge);
    ResponseEntity<ResponseDTO> deleteById(String id);
    ResponseEntity<ResponseDTO> addPrescriptionCharge(PrescriptionCharge prescriptionCharge);
}
