package patient_management_system.serviceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Lab;
import patient_management_system.service.LabService;

@Service
public class LabServiceImpl implements LabService {
    @Override
    public ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> findById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> addLab(Lab lab) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> updateById(Lab lab) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> deleteById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> fetchLabsForDoctor(String search, Integer size, Integer page, String doctorId) {
        return null;
    }
}
