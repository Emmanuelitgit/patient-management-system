package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Appointment;
import patient_management_system.models.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    ResponseEntity<ResponseDTO> findAll();
    ResponseEntity<ResponseDTO> findById(String id);
    ResponseEntity<ResponseDTO> addPatient(Patient patient);
    ResponseEntity<ResponseDTO> updateById(Patient patient);
    ResponseEntity<ResponseDTO> deleteById(String id);
    ResponseEntity<ResponseDTO> countAllPatients();
    ResponseEntity<ResponseDTO> getPaginatedPatients(Integer size, Integer page);
}
