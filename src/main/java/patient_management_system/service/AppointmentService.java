package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Appointment;


public interface AppointmentService {
    ResponseEntity<ResponseDTO> findAll(String search, String startTime, String endTime,Integer size,Integer page);
    ResponseEntity<ResponseDTO> findById(String id);
    ResponseEntity<ResponseDTO> addAppointment(Appointment appointment);
    ResponseEntity<ResponseDTO> updateById(Appointment appointment);
    ResponseEntity<ResponseDTO> deleteById(String id);
    ResponseEntity<ResponseDTO> fetchAppointmentsForDoctor(String doctorId,
                                                           String search,
                                                           String startTime,
                                                           String endTime,
                                                           Integer size,
                                                           Integer page);
    ResponseEntity<ResponseDTO> updateAppointmentStatus(String id,String status);
}
