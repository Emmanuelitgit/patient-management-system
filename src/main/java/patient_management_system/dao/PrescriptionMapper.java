package patient_management_system.dao;

import org.apache.ibatis.annotations.Mapper;
import patient_management_system.dto.PrescriptionDTO;
import patient_management_system.models.Prescription;
import patient_management_system.models.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PrescriptionMapper {
    List<PrescriptionDTO> findAll(String search, Integer limit, Integer offset);
    Optional<Prescription> findById(String id);
    Integer addPrescription(Prescription prescription);
    Integer updateById(Prescription prescription);
    Integer deleteById(String id);
    Optional<PrescriptionDTO> fetchPrescriptionById(String id);
    List<PrescriptionDTO> fetchPrescriptionsForPractitioner(String practitionerId, String search, Integer limit, Integer offset);
}
