package patient_management_system.dao;

import org.apache.ibatis.annotations.Mapper;
import patient_management_system.models.Lab;
import patient_management_system.models.Patient;

import java.util.List;
import java.util.Optional;

@Mapper
public interface LabMapper {
    List<Lab> findAll(String search, Integer limit, Integer offset);
    Optional<Lab> findById(String id);
    Optional<Lab> fetchLabRecordById(String id);
    Integer addLab(Lab lab);
    Integer updateById(Lab lab);
    Integer deleteById(String id);
    Integer countAllLabRecords();
}
