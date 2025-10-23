package patient_management_system.dao;

import org.apache.ibatis.annotations.Mapper;
import patient_management_system.models.AppointmentCharge;
import patient_management_system.models.PrescriptionCharge;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AppointmentChargeMapper {
    List<AppointmentCharge> findAll(String search, Integer limit, Integer offset);
    Optional<AppointmentCharge> findById(String id);
    Integer addAppointmentCharge(AppointmentCharge appointmentCharge);
    Integer updateById(AppointmentCharge appointmentCharge);
    Integer deleteById(String id);
    Optional<AppointmentCharge> findByName(String name);
}
