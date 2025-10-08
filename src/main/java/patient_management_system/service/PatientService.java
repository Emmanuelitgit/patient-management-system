package patient_management_system.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.dao.PatientMapper;
import patient_management_system.models.Patient;
import patient_management_system.util.AppUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PatientService {

    private final PatientMapper patientMapper;

    @Autowired
    public PatientService(PatientMapper patientMapper){
        this.patientMapper=patientMapper;
}


    /**
     * @description This method is used to fetch all patients from the db
     * @return ResponseEntity containing the retrieved patients and status info
     * @auther Emmanuel Yidana
     * @createdAt 7th October 2025
     */
    public ResponseEntity<ResponseDTO> findAll() {
        try {
            ResponseDTO responseDTO;
            log.info("In find all patients method");

            /**
             * loading patients from db
             */
            log.info("About to load patients from db");
            List<Patient> patients = patientMapper.findAll();
            if (patients.isEmpty()){
                log.error("No patient record found");
                responseDTO = AppUtils.getResponseDto("No patient record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }

            /**
             * return results on success
             */
            log.info("Patients fetched successfully");
            responseDTO = AppUtils.getResponseDto("Patients", HttpStatus.OK, patients);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to retrieve patient id given the id
     * @param id The id of the patient to be retrived
     * @return ResponseEntity containing the retrieved patient and status info
     * @auther Emmanuel Yidana
     * @createdAt 8th October 2025
     */
    public ResponseEntity<ResponseDTO> findById(String id){
        try {
            ResponseDTO responseDTO;
            log.info("In get patients by id method");

            /**
             * load patient record from db
             */
            log.info("About to load patient records");
            Optional<Patient> patient = patientMapper.findById(id);
            log.info("DATA:->>{}", patient);
            if (patient.isEmpty()){
                log.error("Patient record not found:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Patient record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }

            /**
             * return retrieved records on success
             */
            log.info("Patient records retrieved successfully:->>{}", patient.get());
            responseDTO = AppUtils.getResponseDto("Patients records retrieved successfully", HttpStatus.OK, patient.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to add new patient record
     * @param patient The payload of the patient to be added
     * @return ResponseEntity containing the saved patient record and status info
     * @auther Emmanuel Yidana
     * @createdAt 8th October 2025
     */
    public ResponseEntity<ResponseDTO> addPatient(Patient patient){
        try {
            ResponseDTO responseDTO;
            log.info("In add patient method:->>{}", patient);

            /**
             * check if user already exist
             */
            log.info("Checking if patient already exist....");
            Optional<Patient> patientAlreadyExist = patientMapper.findByEmail(patient.getEmail());
            log.info("DATA:->>{}", patientAlreadyExist);
            if (patientAlreadyExist.isPresent()){
                log.error("Patient already exist with the email:->>{}", patient.getEmail());
                responseDTO = AppUtils.getResponseDto("Patient already exist", HttpStatus.ALREADY_REPORTED);
                return new ResponseEntity<>(responseDTO, HttpStatus.ALREADY_REPORTED);
            }

            /**
             * check if record was inserted
             */
            patient.setId(UUID.randomUUID().toString());
            patient.setCreatedAt(LocalDate.now());
            patient.setCreatedBy(UUID.randomUUID().toString());
            Integer affectedRows = patientMapper.addPatient(patient);
            if (affectedRows<0){
                log.error("Patient record failed to insert");
                responseDTO = AppUtils.getResponseDto("Patient record failed to insert", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            /**
             * return response on success
             */
            log.info("Patient record added successfully:->>{}", patient);
            responseDTO = AppUtils.getResponseDto("Patient record added successfully", HttpStatus.CREATED);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseDTO> updateById(Patient patient){
        try {
            log.info("In update patient by id method:->>{}");
            ResponseDTO responseDTO;

            /**
             * load patient records
             */
            log.info("About to load patient records");
            Optional<Patient> patientOptional = patientMapper.findById(patient.getId());
            if (patientOptional.isEmpty()){
                log.error("Patient records not found:->>{}", patient.getId());
                responseDTO = AppUtils.getResponseDto("Patient record not found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }

            /**
             * populating updated values
             */
            Patient existingData = patientOptional.get();
            existingData.setDob(patient.getDob()!=null?patient.getDob():existingData.getDob());
            existingData.setEmail(patient.getEmail()!=null?patient.getEmail(): existingData.getEmail());
            existingData.setAddress(patient.getAddress()!=null?patient.getAddress(): existingData.getAddress());
            existingData.setMedicalHistory(patient.getMedicalHistory()!=null? patient.getMedicalHistory() : existingData.getMedicalHistory());
            existingData.setGender(patient.getGender()!=null? patient.getGender() : existingData.getGender());
            existingData.setPhone(patient.getPhone()!=null? patient.getPhone() : existingData.getPhone());
            Integer affectedRows = patientMapper.updateById(existingData);

            /**
             * check if record was updated
             */
            if (affectedRows<0){
                log.error("Patient record update was not successfully");
                responseDTO =AppUtils.getResponseDto("Patient record update was not successfully", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }

            /**
             * return response on success
             */
            log.info("Patient records was updated successfully:->>{}", existingData);
            responseDTO = AppUtils.getResponseDto("Patient record was updated successfully", HttpStatus.OK,existingData);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
