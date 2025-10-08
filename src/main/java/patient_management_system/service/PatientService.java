package patient_management_system.service;

import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.exception.ServerException;
import patient_management_system.dao.PatientMapper;
import patient_management_system.models.Patient;
import patient_management_system.util.AppUtils;

import java.util.List;
import java.util.Optional;

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
            log.error("Unexpected error occurred");
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * @description This method is used to retrieve patient id given the id
     * @param id The id of the patient to be retrived
     * @return ResponseEntity containing the retrieved patient and status info
     * @auther Emmanuel Yidana
     * @createdAt 7th October 2025
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
            log.error("Unexpected error occurred");
            throw new ServerException(e.getMessage());
        }
    }
}
