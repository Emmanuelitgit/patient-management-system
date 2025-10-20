package patient_management_system.dao;

import org.apache.ibatis.annotations.Mapper;
import patient_management_system.models.PrescriptionCharge;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PrescriptionChargeMapper {
    List<PrescriptionCharge> findAll(String search, Integer limit, Integer offset);
    Optional<PrescriptionCharge> findById(String id);
    Integer addPrescriptionCharge(PrescriptionCharge prescriptionCharge);
    Integer updateById(PrescriptionCharge prescriptionCharge);
    Integer deleteById(String id);
    Optional<PrescriptionCharge> findByName(String name);
}
