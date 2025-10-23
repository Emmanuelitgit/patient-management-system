package patient_management_system.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patient_management_system.dao.AppointmentChargeMapper;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.AppointmentCharge;
import patient_management_system.service.AppointmentChargeService;
import patient_management_system.util.AppUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AppointmentChargeServiceImpl implements AppointmentChargeService {

    private final AppointmentChargeMapper appointmentChargeMapper;
    @Autowired
    public AppointmentChargeServiceImpl(AppointmentChargeMapper appointmentChargeMapper) {
        this.appointmentChargeMapper = appointmentChargeMapper;
    }

    /**
     * @description This method is used to fetch all appointment charges from the db
     * @return ResponseEntity containing the retrieved charges and status info
     * @auther Emmanuel Yidana
     * @createdAt 23rd October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page) {
        try {
            ResponseDTO responseDTO;
            log.info("In get all appointment charges method");
            /**
             * load charges from db
             */
            log.info("About to load appointment charges...");
            if (page<=0){
                page = 1;
            }
            Integer offset = (page-1)*size;
            List<AppointmentCharge> appointmentCharges = appointmentChargeMapper.findAll(search, size, offset);
            if (appointmentCharges.isEmpty()){
                log.error("No appointment charge record found");
                responseDTO = AppUtils.getResponseDto("No appointment charge record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Appointment charges was fetched successfully");
            responseDTO = AppUtils.getResponseDto("Appointment charges fetched successfully", HttpStatus.OK,appointmentCharges);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to retrieve appointment charge record by id
     * @param id The id of the appointment record to be retrieved
     * @return ResponseEntity containing the retrieved appointment charge and status info
     * @auther Emmanuel Yidana
     * @createdAt 23rd October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findById(String id) {
        try {
            ResponseDTO responseDTO;
            log.info("In find appointment charge by id method:->>{}", id);
            /**
             * load prescription charge
             */
            log.info("About to load appointment charge from db....");
            Optional<AppointmentCharge> appointmentChargeOptional = appointmentChargeMapper.findById(id);
            if (appointmentChargeOptional.isEmpty()){
                log.error("Appointment charge record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Appointment charge record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Appointment charge was fetched successfully:->>{}", appointmentChargeOptional.get());
            responseDTO = AppUtils.getResponseDto("Appointment charge fetched successfully", HttpStatus.OK,appointmentChargeOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to add new appointment charge record
     * @param appointmentCharge The payload of the appointment charge record to be added
     * @return ResponseEntity containing the saved appointment charge record and status info
     * @auther Emmanuel Yidana
     * @createdAt 20th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> addAppointmentCharge(AppointmentCharge appointmentCharge) {
        try {
            ResponseDTO responseDTO;
            log.info("In add appointment charge method:->>{}", appointmentCharge);
            /**
             * check if charge already exist by name
             */
            log.info("About to check if name already exist...");
            Optional<AppointmentCharge> appointmentChargeOptional = appointmentChargeMapper.findByName(appointmentCharge.getName());
            if (appointmentChargeOptional.isPresent()){
                log.info("Appointment charge already exist:->>{}", appointmentCharge.getName());
                responseDTO = AppUtils.getResponseDto("Appointment charge already exist", HttpStatus.ALREADY_REPORTED);
                return new ResponseEntity<>(responseDTO, HttpStatus.ALREADY_REPORTED);
            }
            /**
             * insert and check if it was inserted
             */
            log.info("About to insert appointment charge record...");
            String id = UUID.randomUUID().toString();
            appointmentCharge.setEnabled(Boolean.TRUE);
            appointmentCharge.setCreatedAt(LocalDate.now());
            appointmentCharge.setCreatedBy(UUID.randomUUID().toString());
            appointmentCharge.setId(id);
            Integer affectedRows = appointmentChargeMapper.addAppointmentCharge(appointmentCharge);
            if (affectedRows<0){
                log.error("Appointment charge record failed to insert");
                responseDTO = AppUtils.getResponseDto("Appointment charge record failed to insert", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve saved record
             */
            log.info("About to retrieve saved record...");
            Optional<AppointmentCharge> savedRecord = appointmentChargeMapper.findById(id);
            if (savedRecord.isEmpty()){
                log.error("Saved record cannot be found:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Saved record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Appointment charge was added successfully:->>{}", savedRecord.get());
            responseDTO = AppUtils.getResponseDto("Appointment charge was added successfully", HttpStatus.CREATED, savedRecord.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update appointment charge record by id
     * @param appointmentCharge The payload of the appointment charge record to be updated
     * @return ResponseEntity containing the updated appointment charge record and status info
     * @auther Emmanuel Yidana
     * @createdAt 23rd October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateById(AppointmentCharge appointmentCharge) {
        try{
            ResponseDTO responseDTO;
            log.info("In update appointment charge method:->>{}", appointmentCharge);
            /**
             * load appointment charge from db
             */
            log.info("About to load appointment charge from db...");
            Optional<AppointmentCharge> appointmentChargeOptional = appointmentChargeMapper.findById(appointmentCharge.getId());
            if (appointmentChargeOptional.isEmpty()){
                log.error("Appointment charge does not exist:->>{}", appointmentCharge.getId());
                responseDTO = AppUtils.getResponseDto("Appointment charge does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * populate updated values
             */
            AppointmentCharge existingData = appointmentChargeOptional.get();
            existingData.setName(appointmentCharge.getName()!=null? appointmentCharge.getName() : existingData.getName());
            existingData.setPrice(appointmentCharge.getPrice()!=null? appointmentCharge.getPrice() : existingData.getPrice());
            existingData.setEnabled(appointmentCharge.getEnabled()!=null? appointmentCharge.getEnabled() : existingData.getEnabled());
            existingData.setUpdatedAt(LocalDate.now());
            existingData.setUpdatedBy(UUID.randomUUID().toString());
            /**
             * update and check if it was updated
             */
            Integer affectedRows = appointmentChargeMapper.updateById(existingData);
            if (affectedRows<0){
                log.error("Appointment charge record failed to update");
                responseDTO = AppUtils.getResponseDto("Appointment charge record failed to update", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve updated record
             */
            log.info("About to retrieve updated record...");
            Optional<AppointmentCharge> updatedOptional = appointmentChargeMapper.findById(appointmentCharge.getId());
            if (updatedOptional.isEmpty()){
                log.error("Updated record cannot found:->>{}", appointmentCharge.getId());
                responseDTO = AppUtils.getResponseDto("Updated record cannot found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Appointment charge was updated successfully:->>{}", updatedOptional.get());
            responseDTO = AppUtils.getResponseDto("Appointment updated successfully", HttpStatus.OK, updatedOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to remove appointment charge record
     * @param id The id of the appointment charge record to be removed
     * @return ResponseEntity containing message and status info
     * @auther Emmanuel Yidana
     * @createdAt 23rd October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> deleteById(String id) {
        try{
            ResponseDTO responseDTO;
            log.warn("In delete appointment charge by id method:->>{}", id);
            /**
             * load appointment charge record from db
             */
            log.info("About to load appointment charge record from db...");
            Optional<AppointmentCharge> appointmentChargeOptional = appointmentChargeMapper.findById(id);
            if (appointmentChargeOptional.isEmpty()){
                log.error("Appointment charge record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Appointment charge record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * delete record
             */
            log.warn("About to delete appointment charge record");
            Integer affectedRows = appointmentChargeMapper.deleteById(id);
            if (affectedRows<0){
                log.error("Appointment charge record failed to delete");
                responseDTO = AppUtils.getResponseDto("Appointment charge record failed to delete", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * return on success
             */
            log.info("Appointment charge deleted successfully");
            responseDTO = AppUtils.getResponseDto("Appointment charge deleted successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
