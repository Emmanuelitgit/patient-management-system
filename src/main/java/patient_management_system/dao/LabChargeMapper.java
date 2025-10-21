package patient_management_system.dao;

import patient_management_system.models.LabCharge;
import patient_management_system.models.PrescriptionCharge;

import java.util.List;
import java.util.Optional;

public interface LabChargeMapper {
    List<LabCharge> findAll(String search, Integer limit, Integer offset);
    Optional<LabCharge> findById(String id);
    Integer addLabCharge(LabCharge labCharge);
    Integer updateById(LabCharge labCharge);
    Integer deleteById(String id);
    Optional<LabCharge> findByName(String name);
}
