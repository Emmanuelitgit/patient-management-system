package patient_management_system.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patient_management_system.dao.AppointmentMapper;
import patient_management_system.dao.PatientMapper;
import patient_management_system.dao.PrescriptionMapper;
import patient_management_system.dao.UserMapper;
import patient_management_system.dto.PrescriptionDTO;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Appointment;
import patient_management_system.models.Prescription;
import patient_management_system.models.User;
import patient_management_system.service.PrescriptionService;
import patient_management_system.util.AppConstants;
import patient_management_system.util.AppUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionMapper prescriptionMapper;
    private final AppointmentMapper appointmentMapper;
    private final PatientMapper patientMapper;
    private final UserMapper userMapper;

    @Autowired
    public PrescriptionServiceImpl(PrescriptionMapper prescriptionMapper, AppointmentMapper appointmentMapper, PatientMapper patientMapper, UserMapper userMapper) {
        this.prescriptionMapper = prescriptionMapper;
        this.appointmentMapper = appointmentMapper;
        this.patientMapper = patientMapper;
        this.userMapper = userMapper;
    }


    /**
     * @description This method is used to fetch all prescriptions from the db
     * @return ResponseEntity containing the retrieved prescriptions and status info
     * @auther Emmanuel Yidana
     * @createdAt 15th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page) {
        try {
            ResponseDTO responseDTO;
            log.info("In get all prescriptions method");
            /**
             * load prescriptions from db
             */
            log.info("About to load prescriptions from db...");
            Integer offset = (page-1)*size;
            List<PrescriptionDTO> prescriptions = prescriptionMapper.findAll(search, size,offset);
            if (prescriptions.isEmpty()){
                log.info("No prescription record found");
                responseDTO = AppUtils.getResponseDto("No prescription record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Prescriptions was fetched successfully");
            responseDTO = AppUtils.getResponseDto("Prescription fetched successfully", HttpStatus.OK,prescriptions);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to retrieve prescription record given the id
     * @param id The id of the prescription to be retrieved
     * @return ResponseEntity containing the retrieved prescription and status info
     * @auther Emmanuel Yidana
     * @createdAt 15th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findById(String id) {
        try {
            ResponseDTO responseDTO;
            log.info("In get prescriptions by id method");
            /**
             * check if prescription record exist
             */
            log.info("About to load prescription from db...");
            Optional<PrescriptionDTO> prescriptionOptional = prescriptionMapper.fetchPrescriptionById(id);
            if (prescriptionOptional.isEmpty()){
                log.error("Prescription record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Prescription record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Prescription record was retrieved successfully:->>{}", prescriptionOptional.get());
            responseDTO = AppUtils.getResponseDto("Prescription fetched successfully", HttpStatus.OK, prescriptionOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to add new prescription record
     * @param prescription The payload of the prescription to be added
     * @return ResponseEntity containing the saved prescription record and status info
     * @auther Emmanuel Yidana
     * @createdAt 15th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> addPrescription(Prescription prescription) {
        try {
            ResponseDTO responseDTO;
            log.info("In add prescription method:->>{}", prescription);
            /**
             * check if appointment record exist
             */
            Optional<Appointment> appointmentOptional = appointmentMapper.findById(prescription.getAppointmentId());
            if (appointmentOptional.isEmpty()){
                log.error("Appointment record does not exist:->>{}", prescription.getAppointmentId());
                responseDTO = AppUtils.getResponseDto("Appointment record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * insert record and check if it was inserted
             */
            log.info("About to insert prescription:->>{}", prescription);
            String id = UUID.randomUUID().toString();
            prescription.setId(id);
            prescription.setCreatedAt(LocalDate.now());
            prescription.setCreatedBy(UUID.randomUUID().toString());
            prescription.setDoctorId(appointmentOptional.get().getDoctorId());
            prescription.setPatientId(appointmentOptional.get().getPatientId());
            prescription.setStatus(AppConstants.PENDING);
            Integer affectedRows = prescriptionMapper.addPrescription(prescription);
            if (affectedRows<0){
                log.error("Prescription record failed to insert");
                responseDTO = AppUtils.getResponseDto("Prescription record failed to insert", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve saved record
             */
            log.info("About to retrieve saved prescription");
            Optional<Prescription> prescriptionOptional = prescriptionMapper.findById(id);
            if (prescriptionOptional.isEmpty()){
                log.error("No prescription record found:->>{}", id);
                responseDTO = AppUtils.getResponseDto("No prescription record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Prescription record was inserted successfully:->>{}", prescriptionOptional.get());
            responseDTO = AppUtils.getResponseDto("Prescription record added successfully", HttpStatus.CREATED, prescriptionOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update a prescription record by id
     * @param prescription The payload of the prescription to be updated
     * @return ResponseEntity containing the updated prescription record and status info
     * @auther Emmanuel Yidana
     * @createdAt 8th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateById(Prescription prescription) {
        try{
            ResponseDTO responseDTO;
            log.info("In update prescription method:->>{}", prescription);
            /**
             * check if prescription record exist
             */
            log.info("About to load prescription record...");
            Optional<Prescription> prescriptionOptional = prescriptionMapper.findById(prescription.getId());
            if (prescriptionOptional.isEmpty()){
                log.error("Prescription record does not exist:->>{}", prescription.getId());
                responseDTO = AppUtils.getResponseDto("Prescription record does not exist",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * populated updated fields
             */
            Prescription existingData = prescriptionOptional.get();
            existingData.setMedication(prescription.getMedication()!=null? prescription.getMedication() : existingData.getMedication());
            existingData.setDosage(prescription.getDosage()!=null? prescription.getDosage() : existingData.getDosage());
            existingData.setInstructions(prescription.getInstructions()!=null? prescription.getInstructions() : existingData.getInstructions());
            existingData.setUpdatedAt(LocalDate.now());
            existingData.setUpdatedBy(UUID.randomUUID().toString());
            /**
             * insert updated record and check if it was inserted/updated
             */
            log.info("About to insert updated record:->>{}", existingData);
            Integer affectedRows = prescriptionMapper.updateById(existingData);
            if (affectedRows<0){
                log.error("Prescription record deletion was not successfully");
                responseDTO =AppUtils.getResponseDto("Prescription record deletion was not successfully", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve updated record from db
             */
            log.info("About to retrieve updated record from db...");
            Optional<Prescription> updatedOptional = prescriptionMapper.findById(prescription.getId());
            if (updatedOptional.isEmpty()){
                log.error("Updated record cannot be found:->>{}", prescription.getId());
                responseDTO = AppUtils.getResponseDto("Updated record cannot be found",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Prescription record was updated successfully:->>{}", updatedOptional.get());
            responseDTO = AppUtils.getResponseDto("Prescription record was updated successfully",HttpStatus.OK,prescriptionOptional.get());
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to delete a prescription record by id
     * @param id The id of the prescription to be deleted
     * @return ResponseEntity containing message and status info
     * @auther Emmanuel Yidana
     * @createdAt 15th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> deleteById(String id) {
       try {
           ResponseDTO responseDTO;
           log.info("In delete prescription by id method:->>{}", id);
           /**
            * check if prescription record exist
            */
           log.info("About to load prescription record");
           Optional<Prescription> prescriptionOptional = prescriptionMapper.findById(id);
           if (prescriptionOptional.isEmpty()){
               log.error("No prescription record found:->>{}", id);
               responseDTO = AppUtils.getResponseDto("No prescription record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
           }
           /**
            * delete record and check if it was deleted
            */
           Integer affectedRows = prescriptionMapper.deleteById(id);
           if (affectedRows<0){
               log.error("Prescription record failed to insert");
               responseDTO = AppUtils.getResponseDto("Prescription record failed to insert", HttpStatus.BAD_REQUEST);
               return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
           }
           /**
            * return response on success
            */
           log.info("Prescription record was deleted successfully:->>{}", id);
           responseDTO = AppUtils.getResponseDto("Prescription record deleted successfully", HttpStatus.OK);
           return new ResponseEntity<>(responseDTO, HttpStatus.OK);

       }catch (Exception e) {
           log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
           ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
           return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
     * @description This method is used to delete a prescription record by id
     * @param practitionerId The id of the logged-in practitioner(Doctor, Pharmacist)
     * @param search The item to be searched for
     * @return ResponseEntity containing message and status info
     * @auther Emmanuel Yidana
     * @createdAt 15th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> fetchPrescriptionsForPractitioner(String practitionerId, String search, Integer size, Integer page) {
        try {
            ResponseDTO responseDTO;
            log.info("In fetch prescriptions for practitioners method:->>{}", practitionerId);
            /**
             * check if practitioner exist
             */
            log.info("About to load practitioner records...");
            Optional<User> userOptional = userMapper.findById(practitionerId);
            if (userOptional.isEmpty()){
                log.error("Practitioner record does not exist:->>{}", practitionerId);
                responseDTO = AppUtils.getResponseDto("Practitioner record does not exist",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * load prescriptions from db
             */
            log.info("About to load prescriptions from db...");
            Integer offset = (page-1)*size;
            List<PrescriptionDTO> prescriptions = prescriptionMapper.fetchPrescriptionsForPractitioner(practitionerId,search,size,offset);
            if (prescriptions.isEmpty()){
                log.error("No prescription record found for practitioner:->>{}", practitionerId);
                responseDTO = AppUtils.getResponseDto("No prescription found for practitioner", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Prescriptions was fetched successfully");
            responseDTO = AppUtils.getResponseDto("Prescriptions fetched successfully", HttpStatus.OK, prescriptions);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update prescription status record by id
     * @param id The id of the prescription to be updated
     * @return ResponseEntity containing the updated record and status info
     * @auther Emmanuel Yidana
     * @createdAt 16th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updatePrescriptionStatus(String id) {
        try {
            ResponseDTO responseDTO;
            log.info("In update prescription status method:->>{}", id);
            /**
             * check if prescription record exist
             */
            log.info("About to load prescription record...");
            Optional<Prescription> prescriptionOptional = prescriptionMapper.findById(id);
            if (prescriptionOptional.isEmpty()){
                log.error("Prescription record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Prescription record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * change status to completed
             */
            Prescription existingData = prescriptionOptional.get();
            existingData.setStatus(AppConstants.COMPLETED);
            existingData.setUpdatedBy(UUID.randomUUID().toString());
            existingData.setUpdatedAt(LocalDate.now());
            /**
             * update record and check if it was updated
             */
            log.info("About to insert updated record:->>{}", existingData);
            Integer affectedRows = prescriptionMapper.updateById(existingData);
            if (affectedRows<0){
                log.error("Prescription record deletion was not successfully");
                responseDTO =AppUtils.getResponseDto("Prescription record deletion was not successfully", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve updated record from db
             */
            log.info("About to retrieve updated record from db...");
            Optional<Prescription> updatedOptional = prescriptionMapper.findById(id);
            if (updatedOptional.isEmpty()){
                log.error("Updated record cannot be found:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Updated record cannot be found",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Prescription record was updated successfully:->>{}", updatedOptional.get());
            responseDTO = AppUtils.getResponseDto("Prescription record was updated successfully",HttpStatus.OK,prescriptionOptional.get());
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
