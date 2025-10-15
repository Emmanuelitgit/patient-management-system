package patient_management_system.dao;

import patient_management_system.models.Patient;
import patient_management_system.models.User;

import java.util.List;
import java.util.Optional;

public interface UserMapper {
    List<User> findAll();
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    Integer addUser(User user);
    Integer updateById(User user);
    Integer deleteById(String id);
    Integer countAllUsers();
    List<User> getPaginatedUsers(Integer limit, Integer offset);
}
