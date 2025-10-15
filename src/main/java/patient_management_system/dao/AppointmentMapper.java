package patient_management_system.dao;

import org.apache.ibatis.annotations.Mapper;
import patient_management_system.dto.AppointmentDTO;
import patient_management_system.models.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface AppointmentMapper {
    List<AppointmentDTO> findAll(String search,LocalTime startTime,LocalTime endTime);
    Optional<Appointment> findById(String id);
    Optional<AppointmentDTO> fetchAppointmentsById(String id);
    Integer addAppointment(Appointment appointment);
    Integer updateById(Appointment appointment);
    Integer deleteById(String id);
    Integer countAppointmentsByDate(LocalDate date, String doctorId);
    Integer checkOverLapping(LocalDate date,
                             LocalTime startTime,
                             LocalTime endTime,
                             String doctorId,
                             String appointmentId);
    List<AppointmentDTO> fetchAppointmentsForDoctor(String doctorId,
                                                    String search,
                                                    LocalTime startTime,
                                                    LocalTime endTime);
    Integer countAllAppointments();
    List<Appointment> getPaginatedAppointments(Integer limit, Integer offset);
}
