package patient_management_system.dao;

import org.apache.ibatis.annotations.Mapper;
import patient_management_system.models.Patient;
import patient_management_system.models.Prescription;
import patient_management_system.models.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PrescriptionMapper {
    List<Prescription> findAll();
    Optional<Prescription> findById(String id);
    Integer addPrescription(Prescription prescription);
    Integer updateById(User user);
    Integer deleteById(String id);
    Integer countAllPrescriptions();
    List<Prescription> getPaginatedPrescriptions(Integer limit, Integer offset);
}
