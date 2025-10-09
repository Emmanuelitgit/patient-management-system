package patient_management_system.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patient_management_system.dao.UserMapper;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Patient;
import patient_management_system.models.User;
import patient_management_system.util.AppUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * @description This method is used to fetch all users from the db
     * @return ResponseEntity containing the retrieved users and status info
     * @auther Emmanuel Yidana
     * @createdAt 9th October 2025
     */
    public ResponseEntity<ResponseDTO> findAll(){
        try {
            ResponseDTO responseDTO;
            log.info("In get all users method");

            /**
             * load users from db
             */
            log.info("About to load users from db");
            List<User> users = userMapper.findAll();
            if (users.isEmpty()){
                log.error("No user record found");
                responseDTO = AppUtils.getResponseDto("No user record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Users retrieved successfully");
            responseDTO = AppUtils.getResponseDto("Users successfully retrieved", HttpStatus.OK, users);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to retrieve user id given the id
     * @param id The id of the patient to be retrieved
     * @return ResponseEntity containing the retrieved user and status info
     * @auther Emmanuel Yidana
     * @createdAt 9th October 2025
     */
    public ResponseEntity<ResponseDTO> findById(String id){
        try {
            ResponseDTO responseDTO;
            log.info("In get user record by id:->>{}", id);

            /**
             * load user record from db
             */
            log.info("About to load user records");
            Optional<User> userOptional = userMapper.findById(id);
            if (userOptional.isEmpty()){
                log.error("User record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("User record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("User records retrieved successfully:->>{}", userOptional.get());
            responseDTO = AppUtils.getResponseDto("User records successfully retrieved", HttpStatus.OK, userOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to add new user record
     * @param user The payload of the user to be added
     * @return ResponseEntity containing the saved user record and status info
     * @auther Emmanuel Yidana
     * @createdAt 9th October 2025
     */
    public ResponseEntity<ResponseDTO> addUser(User user){
        try {
            ResponseDTO responseDTO;
            log.info("In add new user method:->>{}", user);

            /**
             * check if user already exist
             */
            Optional<User> userOptional = userMapper.findByEmail(user.getEmail());
            if (userOptional.isPresent()){
                log.error("User already exist this email:->>{}",userOptional.get());
                responseDTO = AppUtils.getResponseDto("User email already exist", HttpStatus.ALREADY_REPORTED);
                return new ResponseEntity<>(responseDTO, HttpStatus.ALREADY_REPORTED);
            }

            /**
             * insert record and check if it was inserted
             */
            String id = UUID.randomUUID().toString();
            user.setCreatedBy(id);
            user.setCreatedAt(LocalDate.now());
            user.setId(id);
            Integer affectedRows = userMapper.addUser(user);
            if (affectedRows<0){
                log.error("Patient record failed to insert");
                responseDTO = AppUtils.getResponseDto("Patient record failed to insert", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            /**
             * retrieve saved record to return to client
             */
            Optional<User> savedUser = userMapper.findById(id);
            if (savedUser.isEmpty()){
                log.error("User record does not exist");
                responseDTO = AppUtils.getResponseDto("User record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }

            /**
             * return response on success
             */
            log.info("User was added successfully:->>{}", savedUser.get());
            responseDTO = AppUtils.getResponseDto("User record was added successfully", HttpStatus.CREATED, savedUser.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update a user record by id
     * @param user The payload of the user to be updated
     * @return ResponseEntity containing the updated user record and status info
     * @auther Emmanuel Yidana
     * @createdAt 9th October 2025
     */
    public ResponseEntity<ResponseDTO> updateById(User user){
        try {
            ResponseDTO responseDTO;
            log.info("In update user record by id method");

            /**
             * load user record from db
             */
            log.info("About to load user records");
            Optional<User> userOptional = userMapper.findById(user.getId());
            if (userOptional.isEmpty()){
                log.error("User record does not exist:->>{}", user.getId());
                responseDTO = AppUtils.getResponseDto("User record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }

            /**
             * populate values to be updated
             */
            log.info("About to update records");
            User existingData = userOptional.get();
            existingData.setName(user.getName()!=null? user.getName() : existingData.getName());
            existingData.setUpdatedAt(LocalDate.now());
            existingData.setUpdatedBy(UUID.randomUUID().toString());
            existingData.setPhone(user.getPhone()!=null? user.getPhone() : existingData.getPhone());
            existingData.setUsername(user.getUsername()!=null? user.getUsername() : existingData.getUsername());
            existingData.setEmail(user.getEmail()!=null? user.getEmail() : existingData.getEmail());
            Integer affectedRows = userMapper.updateById(existingData);

            /**
             * check if record was updated
             */
            if (affectedRows<0){
                log.error("User record update was not successfully");
                responseDTO =AppUtils.getResponseDto("User record update was not successfully", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve updated record and return to client
             */
            Optional<User> updatedUser = userMapper.findById(existingData.getId());
            if (updatedUser.isEmpty()){
                log.info("Patient record not found");
                responseDTO = AppUtils.getResponseDto("Patient record not found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("User records was updated successfully:->>{}",updatedUser.get());
            responseDTO = AppUtils.getResponseDto("User records was updated successfully", HttpStatus.OK, updatedUser.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to delete a user record by id
     * @param id The id of the user to be deleted
     * @return ResponseEntity containing message and status info
     * @auther Emmanuel Yidana
     * @createdAt 9th October 2025
     */
    public ResponseEntity<ResponseDTO> deleteById(String id){
        try {
            ResponseDTO responseDTO;
            log.info("In delete user by id method:->>{}", id);

            /**
             * load user records
             */
            Optional<User> userOptional = userMapper.findById(id);
            if (userOptional.isEmpty()){
                log.error("User record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("User record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }

            Integer affectedRows = userMapper.deleteById(id);

            /**
             * check if record was deleted
             */
            if (affectedRows<0){
                log.error("Patient record deletion was not successfully");
                responseDTO =AppUtils.getResponseDto("Patient record deletion was not successfully", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * return response on success
             */
            log.info("User record was deleted successfully");
            responseDTO = AppUtils.getResponseDto("User record was deleted successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
