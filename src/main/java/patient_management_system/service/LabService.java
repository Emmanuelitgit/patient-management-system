package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Lab;

public interface LabService {
    ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page);
    ResponseEntity<ResponseDTO> findById(String id);
    ResponseEntity<ResponseDTO> addLab(Lab lab);
    ResponseEntity<ResponseDTO> updateById(Lab lab);
    ResponseEntity<ResponseDTO> deleteById(String id);
}
