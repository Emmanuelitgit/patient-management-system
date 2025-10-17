package patient_management_system.dao;

import org.apache.ibatis.annotations.Mapper;
import patient_management_system.models.User;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    List<User> findAll(String search, Integer limit, Integer offset);
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    Integer addUser(User user);
    Integer updateById(User user);
    Integer deleteById(String id);
}
