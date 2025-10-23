package patient_management_system.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import patient_management_system.dao.*;
import patient_management_system.dto.AppointmentDTO;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.*;
import patient_management_system.service.AppointmentService;
import patient_management_system.util.AppConstants;
import patient_management_system.util.AppUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final UserMapper userMapper;
    private final PatientMapper patientMapper;
    private final AppointmentChargeMapper appointmentChargeMapper;
    private final PaymentServiceImpl paymentServiceImpl;
    private final PaymentMapper paymentMapper;

    @Autowired
    public AppointmentServiceImpl(AppointmentMapper appointmentMapper, UserMapper userMapper, PatientMapper patientMapper, AppointmentChargeMapper appointmentChargeMapper, PaymentServiceImpl paymentServiceImpl, PaymentMapper paymentMapper) {
        this.appointmentMapper = appointmentMapper;
        this.userMapper = userMapper;
        this.patientMapper = patientMapper;
        this.appointmentChargeMapper = appointmentChargeMapper;
        this.paymentServiceImpl = paymentServiceImpl;
        this.paymentMapper = paymentMapper;
    }


    /**
     * @description This method is used to fetch all appointments from the db
     * @return ResponseEntity containing the retrieved appointments and status info
     * @auther Emmanuel Yidana
     * @createdAt 10th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll(String search,String startTime,String endTime,Integer size,Integer page){
        try {
            ResponseDTO responseDTO;
            log.info("In ge all appointments method");
            /**
             * cast time if provided
             */
            LocalTime convertedStartTime = null;
            LocalTime convertedEndTime = null;
            if (startTime!=null && !startTime.isEmpty()){
                convertedStartTime = AppUtils.convertStringToLocalTime(startTime);
            }
            if (endTime !=null && !endTime.isEmpty()){
                convertedEndTime = AppUtils.convertStringToLocalTime(endTime);
            }

            /**
             * load data from db
             */
            log.info("About to load appointments from db...");
            if (page <=0){
                page=1;
            }
            Integer offset = (page-1)*size;
            List<AppointmentDTO> appointments = appointmentMapper.findAll(search,convertedStartTime,convertedEndTime,size,offset);
            if (appointments.isEmpty()){
                log.error("No appointment record found");
                responseDTO = AppUtils.getResponseDto("No appointment record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Appointments fetched successfully");
            responseDTO = AppUtils.getResponseDto("Appointments fetched successfully", HttpStatus.OK, appointments);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to retrieve appointment record by id
     * @param id The id of the appointment record to be retrieved
     * @return ResponseEntity containing the retrieved appointment and status info
     * @auther Emmanuel Yidana
     * @createdAt 10th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findById(String id){
        try{
            ResponseDTO responseDTO;
            log.info("In find appointment by id method:->>{}", id);

            /**
             * loading record from db
             */
            log.info("About to load appointment record from db");
            Optional<AppointmentDTO> appointmentOptional = appointmentMapper.fetchAppointmentById(id);
            if (appointmentOptional.isEmpty()){
                log.error("Appointment record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Appointment record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Appointment record fetched successfully:->>{}", appointmentOptional.get());
            responseDTO = AppUtils.getResponseDto("Appointment record fetched successfully", HttpStatus.OK, appointmentOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @description This method is used to schedule an appointment
     * @param appointment The payload of the appointment to be added
     * @return ResponseEntity containing the saved appointment record and status info
     * @auther Emmanuel Yidana
     * @createdAt 10th October 2025
     */
    @Transactional
    @Override
    public ResponseEntity<ResponseDTO> addAppointment(Appointment appointment){
        try {
            ResponseDTO responseDTO;
            log.info("In create appointment method:->>{}", appointment);

            /**
             * check if patient record exist
             */
            log.info("About to load patient records");
            Optional<Patient> patientOptional = patientMapper.findById(appointment.getPatientId());
            if (patientOptional.isEmpty()){
                log.error("Patient record does not exist:->>{}", appointment.getPatientId());
                responseDTO = AppUtils.getResponseDto("Patient record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * load doctor record from db
             */
            log.info("About to load doctor records");
            Optional<User> userOptional = userMapper.findById(appointment.getDoctorId());
            if (userOptional.isEmpty()){
                log.error("Doctor record cannot be found:->>{}", appointment.getDoctorId());
                responseDTO = AppUtils.getResponseDto("Doctor record cannot be found",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            User doctor = userOptional.get();
            /**
             * load appointment charge record by id
             */
            log.info("About to load appointment charge record...");
            Optional<AppointmentCharge> appointmentChargeOptional = appointmentChargeMapper.findById(appointment.getAppointmentChargeId());
            if (appointmentChargeOptional.isEmpty()){
                log.error("Appointment charge record does not exist:->>{}", appointment.getAppointmentChargeId());
                responseDTO = AppUtils.getResponseDto("Appointment charge record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * check for doctor capacity
             */
            log.info("About to check doctor capacity...");
            Integer doctorAppointments = appointmentMapper
                    .countAppointmentsByDate(appointment.getDate(), doctor.getId());
            if (doctorAppointments>=doctor.getCapacity()){
                log.error("Doctor capacity is full");
                responseDTO = AppUtils.getResponseDto("Doctor capacity is full", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * check overlapping
             */
            log.info("About to check overlapping...");
            appointment.setEndTime(appointment.getStartTime().plusHours(1));
            Integer count = appointmentMapper
                    .checkOverLapping(appointment.getDate(),
                            appointment.getStartTime(),
                            appointment.getEndTime(),
                            doctor.getId(),
                            null);
            if (count>0){
                log.error("Selected date and time already exist");
                responseDTO = AppUtils.getResponseDto("Selected date and time already exist", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * validate date(weekends not allowed)
             */
            log.info("About to validate date:->>{}", appointment.getDate());
            if (appointment.getDate().getDayOfWeek()== DayOfWeek.SATURDAY ||
                appointment.getDate().getDayOfWeek()==DayOfWeek.SUNDAY){
                log.info("Consultation not allowed during weekends:->>{}", appointment.getDate());
                responseDTO = AppUtils.getResponseDto("Consultation not allowed during weekends", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * validate date(today and upward is allowed)
             */
            log.info("About to check if date is before current date(today)...");
            if (appointment.getDate().isBefore(LocalDate.now())){
                log.error("Appointment date cannot be before the current date(today):->>{}", appointment.getDate());
                responseDTO = AppUtils.getResponseDto("Appointment date cannot be before the current date(today)", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO,HttpStatus.BAD_REQUEST);
            }
            /**
             * validate time(6am-6pm allowed)
             */
            log.info("About to validate time...");
            LocalTime morningBoundary = LocalTime.of(6, 0);
            LocalTime eveningBoundary = LocalTime.of(18, 0);
            if (appointment.getStartTime().isBefore(morningBoundary) || appointment.getStartTime().isAfter(eveningBoundary)){
                log.error("Consultation not allow within the selected time:->>{}", appointment.getStartTime());
                responseDTO = AppUtils.getResponseDto("Consultation not allow within the selected time", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * insert record and check if it was inserted
             */
            String id = UUID.randomUUID().toString();
            appointment.setCreatedAt(LocalDate.now());
            appointment.setCreatedBy(id);
            appointment.setStatus(AppConstants.PENDING);
            appointment.setId(id);
            log.info("About to insert record:->>{}", appointment);
            Integer affectedRows = appointmentMapper.addAppointment(appointment);
            if (affectedRows<0){
                log.error("Appointment record failed to insert");
                responseDTO = AppUtils.getResponseDto("Appointment record failed to insert", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * generate invoice for appointment
             */
            Payment payment = Payment
                    .builder()
                    .amount(appointmentChargeOptional.get().getPrice())
                    .entityId(id)
                    .patientId(appointment.getPatientId())
                    .serviceType(AppConstants.APPOINTMENT)
                    .build();
            ResponseEntity<ResponseDTO> invoiceResponse = paymentServiceImpl.generateInvoice(payment);
            if (!invoiceResponse.getStatusCode().is2xxSuccessful()){
                log.error(invoiceResponse.getBody().getMessage());
                responseDTO = AppUtils.getResponseDto(invoiceResponse.getBody().getMessage(), (HttpStatus) invoiceResponse.getStatusCode());
                return new ResponseEntity<>(responseDTO, (HttpStatus) invoiceResponse.getStatusCode());
            }
            /**
             * retrieve saved record and return to client
             */
            log.info("About to retrieve saved record from db");
            Optional<Appointment> appointmentOptional = appointmentMapper.findById(id);
            if (appointmentOptional.isEmpty()){
                log.error("Appointment record does not exist");
                responseDTO = AppUtils.getResponseDto("Appointment record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Appointment schedule was successfully:->>{}", appointmentOptional.get());
            responseDTO = AppUtils.getResponseDto("Appointment schedule was successfully", HttpStatus.CREATED, appointmentOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update an appointment by id
     * @param appointment The payload of the appointment to be updated
     * @return ResponseEntity containing the updated appointment record and status info
     * @auther Emmanuel Yidana
     * @createdAt 14th October 2025
     */
    @Transactional
    @Override
    public ResponseEntity<ResponseDTO> updateById(Appointment appointment){
        try {
            ResponseDTO responseDTO;
            log.info("In update appointment by id method");
            /**
             * check if appointment record exist
             */
            log.info("About to load appointment record from db");
            Optional<Appointment> appointmentOptional = appointmentMapper.findById(appointment.getId());
            if (appointmentOptional.isEmpty()){
                log.info("Appointment record does not exist");
                responseDTO = AppUtils.getResponseDto("Appointment record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * load doctor record from db
             */
            log.info("About to load doctor records");
            Optional<User> userOptional = userMapper.findById(appointment.getDoctorId());
            if (userOptional.isEmpty()){
                log.error("Doctor record cannot be found:->>{}", appointment.getDoctorId());
                responseDTO = AppUtils.getResponseDto("Doctor record cannot be found",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            User doctor = userOptional.get();
            /**
             * load appointment charge record by id
             */
            log.info("About to load appointment charge record...");
            Optional<AppointmentCharge> appointmentChargeOptional = appointmentChargeMapper.findById(appointment.getAppointmentChargeId());
            if (appointmentChargeOptional.isEmpty()){
                log.error("Appointment charge record does not exist:->>{}", appointment.getAppointmentChargeId());
                responseDTO = AppUtils.getResponseDto("Appointment charge record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * check overlapping
             */
            log.info("About to check for overlapping");
            appointment.setEndTime(appointment.getStartTime().plusHours(1));
            Integer count = appointmentMapper
                    .checkOverLapping(appointment.getDate(),
                            appointment.getStartTime(),
                            appointment.getEndTime(),
                            doctor.getId(),
                            appointmentOptional.get().getId());
            if (count>0){
                log.error("Selected date and time already exist");
                responseDTO = AppUtils.getResponseDto("Selected date and time already exist", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * validate date(weekends not allowed)
             */
            log.info("About to validate date:->>{}", appointment.getDate());
            if (appointment.getDate().getDayOfWeek()== DayOfWeek.SATURDAY ||
                    appointment.getDate().getDayOfWeek()==DayOfWeek.SUNDAY){
                log.info("Consultation not allowed during weekends:->>{}", appointment.getDate());
                responseDTO = AppUtils.getResponseDto("Consultation not allowed during weekends", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * validate date(today and upward is allowed)
             */
            log.info("About to check if date is before current date(today)...");
            if (appointment.getDate().isBefore(LocalDate.now())){
                log.error("Appointment date cannot be before the current date(today):->>{}", appointment.getDate());
                responseDTO = AppUtils.getResponseDto("Appointment date cannot be before the current date(today)", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO,HttpStatus.BAD_REQUEST);
            }
            /**
             * validate time(6am-6pm allowed)
             */
            log.info("About to validate time");
            LocalTime morningBoundary = LocalTime.of(6, 0);
            LocalTime eveningBoundary = LocalTime.of(18, 0);
            if (appointment.getStartTime().isBefore(morningBoundary) || appointment.getStartTime().isAfter(eveningBoundary)){
                log.error("Consultation not allow within the selected time:->>{}", appointment.getStartTime());
                responseDTO = AppUtils.getResponseDto("Consultation not allow within the selected time", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * populate updated fields
             */
            log.info("About to populate updated fields");
            Appointment existingData = appointmentOptional.get();
            existingData.setDate(appointment.getDate()!=null?appointment.getDate():existingData.getDate());
            if (appointment.getStartTime()!=null){
                existingData.setStartTime(appointment.getStartTime());
                existingData.setEndTime(appointment.getStartTime().plusHours(1));
            }
            existingData.setDoctorId(appointment.getDoctorId()!=null? appointment.getDoctorId() : existingData.getDoctorId());
            existingData.setRemarks(appointment.getRemarks()!=null?appointment.getRemarks(): existingData.getRemarks());
            existingData.setUpdatedBy(UUID.randomUUID().toString());
            existingData.setUpdatedAt(LocalDate.now());
            /**
             * update record and check if it was updated
             */
            log.info("About to retrieve updated record from db...");
            Integer affectedRows = appointmentMapper.updateById(existingData);
            if (affectedRows<0){
                log.error("Appointment record failed to update");
                responseDTO = AppUtils.getResponseDto("Appointment record failed to update", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * update invoice if appointment type changes
             */
            if (!Objects.equals(appointment.getAppointmentChargeId(), appointmentOptional.get().getAppointmentChargeId())){
                Optional<Payment> paymentOptional = paymentMapper.findByEntityId(appointment.getId());
                if (paymentOptional.isEmpty()){
                    log.error("Payment record does not exist");
                    responseDTO = AppUtils.getResponseDto("Payment record cannot be found", HttpStatus.NOT_FOUND);
                    return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
                }
                Payment existingPaymentData = paymentOptional.get();
                existingPaymentData.setAmount(appointmentChargeOptional.get().getPrice());
                ResponseEntity<ResponseDTO> invoiceResponse = paymentServiceImpl.updateById(existingPaymentData);
                if (!invoiceResponse.getStatusCode().is2xxSuccessful()){
                    log.error(invoiceResponse.getBody().getMessage());
                    responseDTO = AppUtils.getResponseDto(invoiceResponse.getBody().getMessage(), (HttpStatus) invoiceResponse.getStatusCode());
                    return new ResponseEntity<>(responseDTO, (HttpStatus) invoiceResponse.getStatusCode());
                }
            }
            /**
             * retrieve saved record and return to client
             */
            log.info("About to retrieve saved record from db");
            Optional<Appointment> updatedRecord = appointmentMapper.findById(appointment.getId());
            if (updatedRecord.isEmpty()){
                log.error("Appointment record does not exist");
                responseDTO = AppUtils.getResponseDto("Appointment record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Appointment was updated successfully:->>{}", updatedRecord.get());
            responseDTO = AppUtils.getResponseDto("Appointment was updated successfully", HttpStatus.OK, updatedRecord.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to remove an appointment record
     * @param id The id of the appointment to be removed
     * @return ResponseEntity containing message and status info
     * @auther Emmanuel Yidana
     * @createdAt 10th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> deleteById(String id){
        try {
            ResponseDTO responseDTO;
            log.info("In delete appointment by id method:->>{}", id);
            /**
             * check if record exist
             */
            Optional<Appointment> appointmentOptional = appointmentMapper.findById(id);
            if (appointmentOptional.isEmpty()){
                log.error("Appointment record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Appointment record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * delete and check if record was deleted
             */
            Integer affectedRows = appointmentMapper.deleteById(id);
            if (affectedRows<0){
                log.error("Appointment record failed to delete");
                responseDTO = AppUtils.getResponseDto("Appointment record failed to deleted", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * return response on success
             */
            log.info("Appointment record was deleted successfully");
            responseDTO = AppUtils.getResponseDto("Appointment record was deleted successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @description This method is used to fetch appointments for logged-in doctor
     * @param doctorId The id of the logged-in doctor
     * @return ResponseEntity containing the retrieved data and status info
     * @auther Emmanuel Yidana
     * @createdAt 14th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> fetchAppointmentsForDoctor(String doctorId,String search,String startTime,String endTime,Integer size, Integer page){
        try {
            ResponseDTO responseDTO;
            log.info("In fetch appointments for doctor method:->>{}", doctorId);
            /**
             * cast time if provided
             */
            LocalTime convertedStartTime = null;
            LocalTime convertedEndTime = null;
            if (startTime!=null && !startTime.isEmpty()){
                convertedStartTime = AppUtils.convertStringToLocalTime(startTime);
            }
            if (endTime !=null && !endTime.isEmpty()){
                convertedEndTime = AppUtils.convertStringToLocalTime(endTime);
            }
            /**
             * check if record exist
             */
            log.info("About to load doctor record from db");
            Optional<User> userOptional = userMapper.findById(doctorId);
            if (userOptional.isEmpty()){
                log.error("Doctor record does not exist");
                responseDTO = AppUtils.getResponseDto("Doctor record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * load appointments for doctor
             */
            log.info("About to load appointments for doctor");
            if (page <=0){
                page=1;
            }
            Integer offset = (page-1)*size;
            List<AppointmentDTO> appointments = appointmentMapper.fetchAppointmentsForDoctor(doctorId,search,convertedStartTime,convertedEndTime,size,offset);
            if (appointments.isEmpty()){
                log.error("No appointment record found for logged-in doctor");
                responseDTO = AppUtils.getResponseDto("No appointment record found for logged-in doctor", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Appointments for logged-in doctor was loaded successfully");
            responseDTO = AppUtils.getResponseDto("Appointments fetched successfully", HttpStatus.OK, appointments);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @description This method is used to update appointment status(COMPLETED,CANCELLED)
     * @param id The id of the appointment record to be updated
     * @return ResponseEntity containing the retrieved data and status info
     * @auther Emmanuel Yidana
     * @createdAt 17th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateAppointmentStatus(String id, String status){
        try {
            ResponseDTO responseDTO;
            log.info("In update appointment status:->>{}", status);
            /**
             * load appointment record from db
             */
            log.info("About to load appointment record from db...");
            Optional<Appointment> appointmentOptional = appointmentMapper.findById(id);
            if (appointmentOptional.isEmpty()){
                log.error("Appointment record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Appointment record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * update status
             */
            Appointment existingData = appointmentOptional.get();
            if (AppConstants.COMPLETED.equalsIgnoreCase(status)){
                existingData.setStatus(AppConstants.COMPLETED);
            } else if (AppConstants.CANCELLED.equalsIgnoreCase(status)) {
                existingData.setStatus(AppConstants.CANCELLED);
            }else {
                existingData.setStatus(AppConstants.SCHEDULED);
            }
            /**
             * insert updated record and check if it was inserted
             */
            Integer affectedRows = appointmentMapper.updateById(existingData);
            if (affectedRows<0){
                log.error("Appointment record failed to update");
                responseDTO = AppUtils.getResponseDto("Appointment record failed to update", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve updated record
             */
            log.info("About to retrieve updated record...");
            Optional<Appointment> updatedOptional = appointmentMapper.findById(id);
            if (updatedOptional.isEmpty()){
                log.error("Updated record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Updated record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Appointment record was updated successfully:->>{}", updatedOptional.get());
            responseDTO = AppUtils.getResponseDto("Appointment record updated successfully", HttpStatus.OK, updatedOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
