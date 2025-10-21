package patient_management_system.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patient_management_system.dao.LabChargeMapper;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.LabCharge;
import patient_management_system.service.LabChargeService;
import patient_management_system.util.AppUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class LabChargeServiceImpl implements LabChargeService {
    private final LabChargeMapper labChargeMapper;

    public LabChargeServiceImpl(LabChargeMapper labChargeMapper) {
        this.labChargeMapper = labChargeMapper;
    }

    /**
     * @description This method is used to fetch all lab charges from the db
     * @return ResponseEntity containing the retrieved charges and status info
     * @auther Emmanuel Yidana
     * @createdAt 21st October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page) {
        try {
            ResponseDTO responseDTO;
            log.info("In find all lab charges method");
            /**
             * load charges from db
             */
            log.info("About to load lab charges from db...");
            if (page<=0){
                page=1;
            }
            Integer offset = (page-1)*size;
            List<LabCharge> labCharges = labChargeMapper.findAll(search, size, offset);
            if (labCharges.isEmpty()){
                log.error("No lab charge record found");
                responseDTO = AppUtils.getResponseDto("No lab charge record found",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Lab charges was fetched successfully");
            responseDTO = AppUtils.getResponseDto("Lab charges fetched successfully",HttpStatus.OK,labCharges);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to retrieve la charge record by id
     * @param id The id of the lab charge record to be retrieved
     * @return ResponseEntity containing the retrieved lab charge and status info
     * @auther Emmanuel Yidana
     * @createdAt 21st October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findById(String id) {
        try {
            ResponseDTO responseDTO;
            log.info("In get lab charge by id method:->>{}", id);
            /**
             * load lab charge record from db
             */
            log.info("About to load lab charge from db...");
            Optional<LabCharge> labChargeOptional = labChargeMapper.findById(id);
            if (labChargeOptional.isEmpty()){
                log.error("Lab charge record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Lab charge record does not exist",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Lab charge was fetched successfully:->>{}", labChargeOptional.get());
            responseDTO = AppUtils.getResponseDto("Lab charge fetched successfully", HttpStatus.OK,labChargeOptional.get());
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to add new lab charge record
     * @param labCharge The payload of the lab charge record to be added
     * @return ResponseEntity containing the saved lab charge record and status info
     * @auther Emmanuel Yidana
     * @createdAt 21st October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> addLabCharge(LabCharge labCharge) {
        try {
            ResponseDTO responseDTO;
            log.info("In add lab charge method:->>{}", labCharge);
            /**
             * check if lab charge already exist
             */
            log.info("About to check if lab charge already exist");
            Optional<LabCharge> labChargeOptional = labChargeMapper.findByName(labCharge.getName());
            if (labChargeOptional.isPresent()){
                log.error("Lab charge record already exist:->>{}", labCharge.getName());
                responseDTO = AppUtils.getResponseDto("Lab charge record already exist", HttpStatus.ALREADY_REPORTED);
                return new ResponseEntity<>(responseDTO, HttpStatus.ALREADY_REPORTED);
            }
            /**
             * insert record and check if it was inserted
             */
            log.info("About to insert new lab charge record...");
            String id = UUID.randomUUID().toString();
            labCharge.setId(id);
            labCharge.setCreatedAt(LocalDate.now());
            labCharge.setCreatedBy(UUID.randomUUID().toString());
            Integer affectedRows = labChargeMapper.addLabCharge(labCharge);
            if (affectedRows<0){
                log.error("Lab charge record failed to insert");
                responseDTO = AppUtils.getResponseDto("Lab charge record failed to insert", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve inserted record
             */
            log.info("About to retrieve saved record");
            Optional<LabCharge> savedRecord= labChargeMapper.findById(id);
            if (savedRecord.isEmpty()){
                log.error("Inserted record cannot be found:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Inserted record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Lab charge wad added successfully:->>{}", savedRecord);
            responseDTO = AppUtils.getResponseDto("Lab charge added successfully", HttpStatus.CREATED,savedRecord);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update a lab charge record by id
     * @param labCharge The payload of the lab charge record to be updated
     * @return ResponseEntity containing the updated lab charge record and status info
     * @auther Emmanuel Yidana
     * @createdAt 21st October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateById(LabCharge labCharge) {
        try {
            ResponseDTO responseDTO;
            log.info("In update lab charge method:->>{}", labCharge);
            /**
             * load lab charge record
             */
            log.info("About to load lab charge record from db...");
            Optional<LabCharge> labChargeOptional = labChargeMapper.findById(labCharge.getId());
            if (labChargeOptional.isEmpty()){
                log.error("Lab charge record does not exist:->>{}", labCharge.getId());
                responseDTO = AppUtils.getResponseDto("Lab charge record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * populate updated values
             */
            LabCharge existingData = labChargeOptional.get();
            existingData.setName(labCharge.getName()!=null? labCharge.getName() : existingData.getName());
            existingData.setEnabled(labCharge.getEnabled()!=null?labCharge.getEnabled():existingData.getEnabled());
            existingData.setPrice(labCharge.getPrice()!=null? labCharge.getPrice() : existingData.getPrice());
            existingData.setUpdatedAt(LocalDate.now());
            existingData.setUpdatedBy(UUID.randomUUID().toString());
            /**
             * insert updated record and if it was inserted
             */
            Integer affectedRows = labChargeMapper.updateById(existingData);
            if (affectedRows<0){
                log.error("Lab charge record failed to insert");
                responseDTO = AppUtils.getResponseDto("Lab charge record failed to insert", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve updated record
             */
            log.info("About to retrieve updated record...");
            Optional<LabCharge> updatedOptional = labChargeMapper.findById(labCharge.getId());
            if (updatedOptional.isEmpty()){
                log.error("Updated lab charge cannot be found:->>{}", labCharge.getId());
                responseDTO = AppUtils.getResponseDto("Updated lab charge cannot be found",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Lab charge was updated successfully:->>{}", HttpStatus.OK);
            responseDTO = AppUtils.getResponseDto("Lab charge record updated successfully", HttpStatus.OK,updatedOptional.get());
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @description This method is used to remove a lab charge record
     * @param id The id of the lab charge record to be removed
     * @return ResponseEntity containing message and status info
     * @auther Emmanuel Yidana
     * @createdAt 21st October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> deleteById(String id) {
        try {
            ResponseDTO responseDTO;
            log.warn("In delete lab charge record by id method:->>{}", id);
            /**
             * load lab charge record from db
             */
            log.info("About to load lab record from db...");
            Optional<LabCharge> labChargeOptional = labChargeMapper.findById(id);
            if (labChargeOptional.isEmpty()){
                log.error("Lab charge record cannot be found:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Lab charge record cannot be found",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * delete record and check if it was deleted
             */
            log.warn("About to delete lab charge record");
            Integer affectedRows = labChargeMapper.deleteById(id);
            if (affectedRows<0){
                log.error("Lab charge record failed to delete");
                responseDTO = AppUtils.getResponseDto("Lab charge record failed to delete", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * return response on success
             */
            log.info("Lab charge record was deleted successfully");
            responseDTO = AppUtils.getResponseDto("Lab charge record deleted successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
