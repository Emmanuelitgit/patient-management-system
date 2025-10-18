package patient_management_system.serviceImpl;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patient_management_system.dao.LabMapper;
import patient_management_system.dao.PatientMapper;
import patient_management_system.dao.UserMapper;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Lab;
import patient_management_system.models.Patient;
import patient_management_system.models.User;
import patient_management_system.service.LabService;
import patient_management_system.util.AppConstants;
import patient_management_system.util.AppUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class LabServiceImpl implements LabService {
    private final LabMapper labMapper;
    private final PatientMapper patientMapper;
    private final UserMapper userMapper;

    @Autowired
    public LabServiceImpl(LabMapper labMapper, PatientMapper patientMapper, UserMapper userMapper) {
        this.labMapper = labMapper;
        this.patientMapper = patientMapper;
        this.userMapper = userMapper;
    }

    /**
     * @description This method is used to fetch all labs from the db
     * @return ResponseEntity containing the retrieved labs and status info
     * @auther Emmanuel Yidana
     * @createdAt 17th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page) {
        try{
            ResponseDTO responseDTO;
            log.info("In get all lab records method");
            /**
             * load records from db
             */
            if (page<=0){
                page=1;
            }
            Integer offset = (page-1)*size;
            log.info("About to load lab records from db...");
            List<Lab> labs = labMapper.findAll(search,size,offset);
            if (labs.isEmpty()){
                log.error("No lab record found");
                responseDTO = AppUtils.getResponseDto("No lab record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Labs was loaded successfully");
            responseDTO = AppUtils.getResponseDto("Labs fetched successfully", HttpStatus.OK,labs);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to retrieve lab record by id
     * @param id The id of the lab record to be retrieved
     * @return ResponseEntity containing the retrieved lab and status info
     * @auther Emmanuel Yidana
     * @createdAt 17th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findById(String id) {
        try {
            log.info("In get lab record by id method:->>{}", id);
            ResponseDTO responseDTO;
            /**
             * load lab record from db
             */
            log.info("About to load lab record from db...");
            Optional<Lab> labOptional = labMapper.findById(id);
            if (labOptional.isEmpty()){
                log.error("Lab record does not exist:->>{}",id);
                responseDTO = AppUtils.getResponseDto("Lab record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Lab record was retrieved successfully:->>{}", labOptional.get());
            responseDTO = AppUtils.getResponseDto("Lab record was retrieved successfully",HttpStatus.OK,labOptional.get());
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @description This method is used to add new lab record
     * @param lab The payload of the lab record to be added
     * @return ResponseEntity containing the lab record and status info
     * @auther Emmanuel Yidana
     * @createdAt 18th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> addLab(Lab lab) {
        try{
            ResponseDTO responseDTO;
            log.info("In add lab record method:->>{}", lab);
            /**
             * check if patient record exist
             */
            log.info("About to load patient records from db...");
            Optional<Patient> patientOptional = patientMapper.findById(lab.getPatientId());
            if (patientOptional.isEmpty()){
                log.error("Patient record does not exist:->>{}", lab.getPatientId());
                responseDTO = AppUtils.getResponseDto("Patient record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * check if doctor record exist
             */
            log.info("About to load doctor records from db....");
            Optional<User> userOptional = userMapper.findById(lab.getDoctorId());
            if (userOptional.isEmpty()){
                log.info("Doctor record does not exist:->>{}", lab.getDoctorId());
                responseDTO = AppUtils.getResponseDto("Doctor record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * insert record and check if it was inserted
             */
            log.info("About to insert lab record");
            String id = UUID.randomUUID().toString();
            lab.setId(id);
            lab.setCreatedBy(UUID.randomUUID().toString());
            lab.setCreatedAt(LocalDate.now());
            lab.setStatus(AppConstants.AWAITING_PAYMENT);
            Integer affectedRows = labMapper.addLab(lab);
            if (affectedRows<0){
                log.error("Lab record failed to insert");
                responseDTO = AppUtils.getResponseDto("Lab record failed to insert", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve saved record
             */
            log.info("About to retrieve saved record....");
            Optional<Lab> savedLabOptional = labMapper.fetchLabRecordById(id);
            if (savedLabOptional.isEmpty()){
                log.error("Saved record cannot be found:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Saved record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Lab record was inserted successfully:->>{}", savedLabOptional.get());
            responseDTO = AppUtils.getResponseDto("Lab record added successfully", HttpStatus.CREATED, savedLabOptional.get());
            return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update a lab record by id
     * @param lab The payload of the lab record to be updated
     * @return ResponseEntity containing the updated lab record and status info
     * @auther Emmanuel Yidana
     * @createdAt 18th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateById(Lab lab) {
        try {
            ResponseDTO responseDTO;
            log.info("In updated lab record by id:->>{}",lab);
            /**
             * check if record exist
             */
            log.info("About to load lab record from db...");
            Optional<Lab> labOptional = labMapper.findById(lab.getId());
            if (labOptional.isEmpty()){
                log.error("Lab record does not exist:->>{}", lab.getId());
                responseDTO = AppUtils.getResponseDto("Lab record does not exist",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * populated updated record
             */
            Lab existingData = labOptional.get();
            existingData.setUpdatedAt(LocalDate.now());
            existingData.setUpdatedBy(UUID.randomUUID().toString());
            existingData.setTestName(lab.getTestName()!=null? lab.getTestName() : existingData.getTestName());
            existingData.setResult(lab.getResult()!=null? lab.getResult() : existingData.getResult());
            if (lab.getResult()!=null && !lab.getResult().isEmpty()){
                existingData.setStatus(AppConstants.READY);
            }
            /**
             * save changes and check if it was inserted
             */
            log.info("About to save updated values:->>{}", existingData);
            Integer affectedRows = labMapper.updateById(existingData);
            if (affectedRows<0){
                log.error("Lab record failed to update");
                responseDTO = AppUtils.getResponseDto("Lab record failed to update", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve updated record
             */
            log.info("About to retrieve updated record....");
            Optional<Lab> updatedLabOptional = labMapper.fetchLabRecordById(lab.getId());
            if (updatedLabOptional.isEmpty()){
                log.error("Updated record cannot be found:->>{}", lab.getId());
                responseDTO = AppUtils.getResponseDto("Updated record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Lab record was updated successfully:->>{}", updatedLabOptional.get());
            responseDTO = AppUtils.getResponseDto("Lab record updated successfully", HttpStatus.OK,updatedLabOptional.get());
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @description This method is used to remove an lab record
     * @param id The id of the lab record to be removed
     * @return ResponseEntity containing message and status info
     * @auther Emmanuel Yidana
     * @createdAt 18th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> deleteById(String id) {
        try {
            ResponseDTO responseDTO;
            log.info("In delete lab record by id method:->>{}",id);
            /**
             * check if record exist
             */
            log.info("About to load lab record from db...");
            Optional<Lab> labOptional = labMapper.findById(id);
            if (labOptional.isEmpty()){
                log.error("Lab record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Lab record does not exist",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * delete record and check if it was deleted
             */
            log.info("About to delete lab record:->>{}", id);
            Integer affectedRows = labMapper.deleteById(id);
            if (affectedRows<0){
                log.error("Lab record failed to delete");
                responseDTO = AppUtils.getResponseDto("Lab record failed to delete", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * return response on success
             */
            log.info("Lab record was deleted successfully:->>{}", id);
            responseDTO = AppUtils.getResponseDto("Lab record was deleted successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
