package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Patient;

public interface PatientService {
    ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page);
    ResponseEntity<ResponseDTO> findById(String id);
    ResponseEntity<ResponseDTO> addPatient(Patient patient);
    ResponseEntity<ResponseDTO> updateById(Patient patient);
    ResponseEntity<ResponseDTO> deleteById(String id);
    ResponseEntity<ResponseDTO> countAllPatients();
}
