package patient_management_system.dao;

import patient_management_system.models.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentMapper {
    List<Appointment> findAll();
    Optional<Appointment> findById(String id);
    Optional<Appointment> findByEmail(String email);
    Integer addAppointment(Appointment appointment);
    Integer updateById(Appointment appointment);
    Integer deleteById(String id);
    Integer countAppointmentsByDate(LocalDate date, String doctorId);
    Integer checkOverLapping(LocalDate date,
                             LocalTime startTime,
                             LocalTime endTime,
                             String doctorId);
    Integer countAllAppointments();
    List<Appointment> getPaginatedAppointments(Integer limit, Integer offset);
}
