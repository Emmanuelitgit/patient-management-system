package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.AppointmentCharge;

public interface AppointmentChargeService {
    ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page);
    ResponseEntity<ResponseDTO> findById(String id);
    ResponseEntity<ResponseDTO> updateById(AppointmentCharge appointmentCharge);
    ResponseEntity<ResponseDTO> deleteById(String id);
    ResponseEntity<ResponseDTO> addAppointmentCharge(AppointmentCharge appointmentCharge);
}
