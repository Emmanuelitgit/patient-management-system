package patient_management_system.dao;

import org.apache.ibatis.annotations.Mapper;
import patient_management_system.models.Patient;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PatientMapper {
    List<Patient> findAll();
    Optional<Patient> findById(String id);
    Optional<Patient> findByEmail(String email);
    Integer addPatient(Patient patient);
    Integer updateById(Patient patient);
    Integer deleteById(String id);
}