package patient_management_system.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patient_management_system.dao.PrescriptionChargeMapper;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.PrescriptionCharge;
import patient_management_system.service.PrescriptionChargeService;
import patient_management_system.util.AppUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PrescriptionChargeServiceImpl implements PrescriptionChargeService {
    private final PrescriptionChargeMapper prescriptionChargeMapper;

    @Autowired
    public PrescriptionChargeServiceImpl(PrescriptionChargeMapper prescriptionChargeMapper) {
        this.prescriptionChargeMapper = prescriptionChargeMapper;
    }

    /**
     * @description This method is used to fetch all prescriptions charges from the db
     * @return ResponseEntity containing the retrieved charges and status info
     * @auther Emmanuel Yidana
     * @createdAt 20th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page) {
        try {
            ResponseDTO responseDTO;
            log.info("In get all prescriptions charges method");
            /**
             * load charges from db
             */
            log.info("About to load prescription charges...");
            List<PrescriptionCharge> prescriptionCharges = prescriptionChargeMapper.findAll(search, size, page);
            if (prescriptionCharges.isEmpty()){
                log.error("No prescription record found");
                responseDTO = AppUtils.getResponseDto("No prescription record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Prescription charges was fetched successfully");
            responseDTO = AppUtils.getResponseDto("Prescription charges fetched successfully", HttpStatus.OK,prescriptionCharges);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to retrieve prescription charge record by id
     * @param id The id of the prescription record to be retrieved
     * @return ResponseEntity containing the retrieved prescription charge and status info
     * @auther Emmanuel Yidana
     * @createdAt 20th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findById(String id) {
        try {
            ResponseDTO responseDTO;
            log.info("In find prescription charge by id method:->>{}", id);
            /**
             * load prescription charge
             */
            log.info("About to load prescription charge from db....");
            Optional<PrescriptionCharge> prescriptionChargeOptional = prescriptionChargeMapper.findById(id);
            if (prescriptionChargeOptional.isEmpty()){
                log.error("Prescription charge record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Prescription charge record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Prescription charge was fetched successfully:->>{}", prescriptionChargeOptional.get());
            responseDTO = AppUtils.getResponseDto("Prescription charge fetched successfully", HttpStatus.OK,prescriptionChargeOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to add new prescription charge record
     * @param prescriptionCharge The payload of the prescription charge record to be added
     * @return ResponseEntity containing the saved prescription charge record and status info
     * @auther Emmanuel Yidana
     * @createdAt 20th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> addPrescriptionCharge(PrescriptionCharge prescriptionCharge) {
        try {
            ResponseDTO responseDTO;
            log.info("In add prescription charge method:->>{}", prescriptionCharge);
            /**
             * check if charge already exist by name
             */
            log.info("About to check if name already exist...");
            Optional<PrescriptionCharge> prescriptionChargeOptional = prescriptionChargeMapper.findByName(prescriptionCharge.getName());
            if (prescriptionChargeOptional.isPresent()){
                log.info("Prescription charge already exist:->>{}", prescriptionCharge.getName());
                responseDTO = AppUtils.getResponseDto("Prescription charge already exist", HttpStatus.ALREADY_REPORTED);
                return new ResponseEntity<>(responseDTO, HttpStatus.ALREADY_REPORTED);
            }
            /**
             * insert and check if it was inserted
             */
            log.info("About to insert prescription charge record...");
            String id = UUID.randomUUID().toString();
            prescriptionCharge.setEnabled(true);
            prescriptionCharge.setCreatedAt(LocalDate.now());
            prescriptionCharge.setCreatedBy(UUID.randomUUID().toString());
            prescriptionCharge.setId(id);
            Integer affectedRows = prescriptionChargeMapper.addPrescriptionCharge(prescriptionCharge);
            if (affectedRows<0){
                log.error("Prescription charge record failed to insert");
                responseDTO = AppUtils.getResponseDto("Prescription charge record failed to insert", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve saved record
             */
            log.info("About to retrieve saved record...");
            Optional<PrescriptionCharge> savedRecord = prescriptionChargeMapper.findById(id);
            if (savedRecord.isEmpty()){
                log.error("Saved record cannot be found:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Saved record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Prescription charge was added successfully:->>{}", savedRecord.get());
            responseDTO = AppUtils.getResponseDto("Prescription charge was added successfully", HttpStatus.CREATED, savedRecord.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update a prescription charge record by id
     * @param prescriptionCharge The payload of the prescription charge record to be updated
     * @return ResponseEntity containing the updated lab record and status info
     * @auther Emmanuel Yidana
     * @createdAt 18th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateById(PrescriptionCharge prescriptionCharge) {
        try{
            ResponseDTO responseDTO;
            log.info("In update prescription charge method:->>{}", prescriptionCharge);
            /**
             * load prescription charge from db
             */
            log.info("About to load prescription charge from db...");
            Optional<PrescriptionCharge> prescriptionChargeOptional = prescriptionChargeMapper.findById(prescriptionCharge.getId());
            if (prescriptionChargeOptional.isEmpty()){
                log.error("Prescription charge does not exist:->>{}", prescriptionCharge.getId());
                responseDTO = AppUtils.getResponseDto("Prescription charge does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * populate updated values
             */
            PrescriptionCharge existingData = prescriptionChargeOptional.get();
            existingData.setName(prescriptionCharge.getName()!=null? prescriptionCharge.getName() : existingData.getName());
            existingData.setUpdatedAt(LocalDate.now());
            existingData.setUpdatedBy(UUID.randomUUID().toString());
            /**
             * update and check if it was updated
             */
            Integer affectedRows = prescriptionChargeMapper.updateById(existingData);
            if (affectedRows<0){
                log.error("Prescription charge record failed to update");
                responseDTO = AppUtils.getResponseDto("Prescription charge record failed to update", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve updated record
             */
            log.info("About to retrieve updated record...");
            Optional<PrescriptionCharge> updatedOptional = prescriptionChargeMapper.findById(prescriptionCharge.getId());
            if (updatedOptional.isEmpty()){
                log.error("Updated record cannot found:->>{}", prescriptionCharge.getId());
                responseDTO = AppUtils.getResponseDto("Updated record cannot found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Prescription charge was updated successfully:->>{}", updatedOptional.get());
            responseDTO = AppUtils.getResponseDto("Prescription updated successfully", HttpStatus.OK, updatedOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to remove a prescription charge record
     * @param id The id of the prescription charge record to be removed
     * @return ResponseEntity containing message and status info
     * @auther Emmanuel Yidana
     * @createdAt 20th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> deleteById(String id) {
        try{
            ResponseDTO responseDTO;
            log.warn("In delete prescription charge by id method:->>{}", id);
            /**
             * load prescription charge record from db
             */
            log.info("About to load prescription charge record from db...");
            Optional<PrescriptionCharge> prescriptionChargeOptional = prescriptionChargeMapper.findById(id);
            if (prescriptionChargeOptional.isEmpty()){
                log.error("Prescription charge record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Prescription charge record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * delete record
             */
            log.warn("About to delete prescription charge record");
            Integer affectedRows = prescriptionChargeMapper.deleteById(id);
            if (affectedRows<0){
                log.error("Prescription charge record failed to delete");
                responseDTO = AppUtils.getResponseDto("Prescription charge record failed to delete", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * return on success
             */
            log.info("Prescription charge deleted successfully");
            responseDTO = AppUtils.getResponseDto("Prescription charge deleted successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
