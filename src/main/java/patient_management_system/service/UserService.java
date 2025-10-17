package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.User;

public interface UserService {
    ResponseEntity<ResponseDTO> findAll(String search,Integer size, Integer page);
    ResponseEntity<ResponseDTO> findById(String id);
    ResponseEntity<ResponseDTO> addUser(User user);
    ResponseEntity<ResponseDTO> updateById(User user);
    ResponseEntity<ResponseDTO> deleteById(String id);
}
