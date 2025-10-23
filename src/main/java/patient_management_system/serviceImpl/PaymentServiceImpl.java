package patient_management_system.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import patient_management_system.dao.*;
import patient_management_system.dto.InvoiceDTO;
import patient_management_system.dto.InvoiceResponse;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.dto.WebHookPayload;
import patient_management_system.exception.ServerException;
import patient_management_system.models.*;
import patient_management_system.service.PaymentService;
import patient_management_system.util.AppConstants;
import patient_management_system.util.AppUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;
    private final PaymentMapper paymentMapper;
    private final PatientMapper patientMapper;
    private final LabMapper labMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final AppointmentMapper appointmentMapper;

    @Value("${PAYMENT_INITIALIZATION_ENDPOINT}")
    private String PAYMENT_INITIALIZATION_ENDPOINT;
    @Value("${PAYSTACK_SECRET_KEY}")
    private String PAYSTACK_SECRET_KEY;

    @Autowired
    public PaymentServiceImpl(RestTemplate restTemplate, PaymentMapper paymentMapper, PatientMapper patientMapper, LabMapper labMapper, PrescriptionMapper prescriptionMapper, AppointmentMapper appointmentMapper) {
        this.restTemplate = restTemplate;
        this.paymentMapper = paymentMapper;
        this.patientMapper = patientMapper;
        this.labMapper = labMapper;
        this.prescriptionMapper = prescriptionMapper;
        this.appointmentMapper = appointmentMapper;
    }

    /**
     * @description This method is used to fetch all payments from the db
     * @return ResponseEntity containing the retrieved payments and status info
     * @auther Emmanuel Yidana
     * @createdAt 19th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page) {
        try{
            ResponseDTO responseDTO;
         log.info("In get all payment method");
            /**
             * load payments from db
             */
            log.info("About to load payments records from db...");
            List<Payment> payments = paymentMapper.findAll(search, size, page);
            if (payments.isEmpty()){
                log.error("No payment record found");
                responseDTO = AppUtils.getResponseDto("No payment record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Payments records was fetched successfully");
            responseDTO = AppUtils.getResponseDto("Payments records fetched successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to retrieve payment record given the id
     * @param id The id of the payment record to be retrieved
     * @return ResponseEntity containing the retrieved payment and status info
     * @auther Emmanuel Yidana
     * @createdAt 19th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findById(String id) {
        try {
            ResponseDTO responseDTO;
            log.info("In find payment record by id method:->>{}", id);
            /**
             * load record from db
             */
            log.info("About to load payment record from db:->>{}", id);
            Optional<Payment> paymentOptional = paymentMapper.findById(id);
            if (paymentOptional.isEmpty()){
                log.error("Payment record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Payment record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Payment record fetched successfully:->>{}", paymentOptional.get());
            responseDTO = AppUtils.getResponseDto("Payment record fetched successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update a payment record by id
     * @param payment The payload of the payment record to be updated
     * @return ResponseEntity containing the updated payment record and status info
     * @auther Emmanuel Yidana
     * @createdAt 19th October 2025
     */
    @Transactional
    @Override
    public ResponseEntity<ResponseDTO> updateById(Payment payment) {
        try {
            ResponseDTO responseDTO;
            log.info("In update payment record by id method:->>{}", payment);
            /**
             * load payment record from db
             */
            log.info("About to load payment record from db...");
            Optional<Payment> paymentOptional = paymentMapper.findById(payment.getId());
            if (paymentOptional.isEmpty()){
                log.error("Payment record does not exist:->>{}", payment.getId());
                responseDTO = AppUtils.getResponseDto("Payment record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            Payment existingData = paymentOptional.get();
            /**
             * load patient records
             */
            log.info("About to load patient records:->>{}", payment.getPatientId());
            Optional<Patient> patientOptional = patientMapper.findById(payment.getPatientId());
            if (patientOptional.isEmpty()){
                log.error("Patient record does not exist:->>{}", payment.getPatientId());
                responseDTO = AppUtils.getResponseDto("Patient record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * load entity records
             */
            log.info("About to load entity records from db...");
            Optional<Lab> labOptional = labMapper.findById(payment.getEntityId());
            Optional<Prescription> prescriptionOptional = prescriptionMapper.findById(payment.getEntityId());
            Optional<Appointment> appointmentOptional = appointmentMapper.findById(payment.getEntityId());
            if (prescriptionOptional.isEmpty() && labOptional.isEmpty() && appointmentOptional.isEmpty()){
                log.error("Entity record does not exist:->>{}", payment.getEntityId());
                responseDTO = AppUtils.getResponseDto("Entity record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * if price amount changes regenerate invoice
             */
            if (!Objects.equals(payment.getAmount(), paymentOptional.get().getAmount())){
                log.info("About to re-generate invoice with new amount:->>{}", payment.getAmount());
                /**
                 * request headers
                 */
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(PAYSTACK_SECRET_KEY);
                headers.setContentType(MediaType.APPLICATION_JSON);
                /**
                 * prepare invoice payload
                 */
                InvoiceDTO invoiceDTO = InvoiceDTO
                        .builder()
                        .email(patientOptional.get().getEmail())
                        .amount(payment.getAmount()*100)
                        .build();
                HttpEntity entity = new HttpEntity(invoiceDTO,headers);
                /**
                 * make request
                 */
                ResponseEntity<InvoiceResponse> responseEntity = restTemplate.postForEntity(PAYMENT_INITIALIZATION_ENDPOINT,entity, InvoiceResponse.class);
                if (!responseEntity .getStatusCode().is2xxSuccessful()){
                    responseDTO = AppUtils.getResponseDto(responseEntity.getBody().getMessage(), HttpStatus.BAD_REQUEST);
                    return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
                }
                log.info("PayStack response:->>{}", responseEntity.getBody().getData());
                /**
                 * set response
                 */
                existingData.setAuthorizationUrl(responseEntity.getBody().getData().getAuthorization_url());
                existingData.setReferenceNumber(responseEntity.getBody().getData().getReference());
                existingData.setAccessCode(responseEntity.getBody().getData().getAccess_code());
                existingData.setAmount(payment.getAmount());
            }
            /**
             * populate updated values(only status is updatable for now)
             */
            existingData.setUpdatedAt(LocalDate.now());
            existingData.setUpdatedBy(UUID.randomUUID().toString());
            if (payment.getStatus()!=null){
                if (AppConstants.PAID.equalsIgnoreCase(payment.getStatus())){
                    existingData.setStatus(AppConstants.PAID);
                }else{
                    existingData.setStatus(AppConstants.PENDING);
                }
            }
            /**
             * update and check if it was updated/inserted
             */
            log.info("Existing payment data:->>{}", existingData);
            Integer affectedRows = paymentMapper.updateById(existingData);
            if (affectedRows<0){
                log.error("Payment record failed to update");
                responseDTO = AppUtils.getResponseDto("Payment record failed to update", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * retrieve updated record
             */
            log.info("About to retrieve updated record...");
            Optional<Payment> updatedOptional = paymentMapper.findById(payment.getId());
            if (updatedOptional.isEmpty()){
                log.error("Updated record cannot be found:->>{}", payment.getId());
                responseDTO = AppUtils.getResponseDto("Updated record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Payment record was updated successfully:->>{}", updatedOptional.get());
            responseDTO = AppUtils.getResponseDto("Payment record updated successfully", HttpStatus.OK,updatedOptional.get());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @description This method is used to delete a payment record by id
     * @param id The id of the payment to be deleted
     * @return ResponseEntity containing message and status info
     * @auther Emmanuel Yidana
     * @createdAt 19th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> deleteById(String id) {
        try {
         ResponseDTO responseDTO;
         log.warn("In delete payment record by method:->>{}", id);
            /**
             * load payment record from db
             */
            log.info("About to load payment record from db...");
            Optional<Payment> paymentOptional = paymentMapper.findById(id);
            if (paymentOptional.isEmpty()){
                log.error("Payment record does not exist:->>{}", id);
                responseDTO = AppUtils.getResponseDto("Payment record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * delete record and check if it was deleted
             */
            log.warn("About to delete payment record:->>{}", paymentOptional.get());
            Integer affectedRows = paymentMapper.deleteById(id);
            if (affectedRows<0){
                log.error("Payment record failed to delete");
                responseDTO = AppUtils.getResponseDto("Payment record failed to delete", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * return response on success
             */
            log.info("Payment record was deleted successfully:->>{}", paymentOptional.get());
            responseDTO = AppUtils.getResponseDto("Payment record deleted successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to generate an invoice for a service
     * @param payment The payload of the invoice to be generated
     * @return ResponseEntity containing the generated invoice and status info
     * @auther Emmanuel Yidana
     * @createdAt 19th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> generateInvoice(Payment payment) {
        try {
            ResponseDTO responseDTO;
            log.info("In generate invoice method");
            /**
             * load patient records
             */
            log.info("About to load patient records:->>{}", payment.getPatientId());
            Optional<Patient> patientOptional = patientMapper.findById(payment.getPatientId());
            if (patientOptional.isEmpty()){
                log.error("Patient record does not exist:->>{}", payment.getPatientId());
                responseDTO = AppUtils.getResponseDto("Patient record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * load entity records
             */
            log.info("About to load entity records from db...");
            Optional<Lab> labOptional = labMapper.findById(payment.getEntityId());
            Optional<Prescription> prescriptionOptional = prescriptionMapper.findById(payment.getEntityId());
            Optional<Appointment> appointmentOptional = appointmentMapper.findById(payment.getEntityId());
            if (prescriptionOptional.isEmpty() && labOptional.isEmpty() && appointmentOptional.isEmpty()){
                log.error("Entity record does not exist:->>{}", payment.getEntityId());
                responseDTO = AppUtils.getResponseDto("Entity record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * request headers
             */
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(PAYSTACK_SECRET_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);
            /**
             * prepare invoice payload
             */
            InvoiceDTO invoiceDTO = InvoiceDTO
                    .builder()
                    .email(patientOptional.get().getEmail())
                    .amount(payment.getAmount()*100)
                    .build();
            HttpEntity entity = new HttpEntity(invoiceDTO,headers);
            /**
             * make request
             */
            log.info("About to make request to PayStack to generate invoice..");
            ResponseEntity<InvoiceResponse> responseEntity = restTemplate.postForEntity(PAYMENT_INITIALIZATION_ENDPOINT,entity, InvoiceResponse.class);
            if (!responseEntity .getStatusCode().is2xxSuccessful()){
                responseDTO = AppUtils.getResponseDto(responseEntity.getBody().getMessage(), HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            log.info("PayStack response:->>{}", responseEntity.getBody().getData());
            /**
             * save generated invoice and check if it was inserted
             */
            String id = UUID.randomUUID().toString();
            payment.setId(id);
            payment.setCreatedBy(UUID.randomUUID().toString());
            payment.setCreatedAt(LocalDate.now());
            payment.setAuthorizationUrl(responseEntity.getBody().getData().getAuthorization_url());
            payment.setReferenceNumber(responseEntity.getBody().getData().getReference());
            payment.setAccessCode(responseEntity.getBody().getData().getAccess_code());
            payment.setStatus(AppConstants.PENDING);
            payment.setCreatedAt(LocalDate.now());
            payment.setCreatedBy(UUID.randomUUID().toString());
            Integer affectedRows = paymentMapper.addPayment(payment);
            if (affectedRows<0){
                log.error("Invoice record failed to insert");
                responseDTO = AppUtils.getResponseDto("Invoice record failed to insert", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
            }
            /**
             * return response on success
             */
            log.info("Invoice was generated successfully");
            responseDTO = AppUtils.getResponseDto("Invoice generated successfully", HttpStatus.CREATED);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to listen to updates from payStack.
     * @param webHookPayload the object containing the data to be received from payStack.
     * @return ResponseEntity containing the status code of the operation which will serve as acknowledgement of receiving the data.
     * @author Emmanuel Yidana
     * @createdAt 7th, June 2025
     * */
    @Transactional
    @Override
    public ResponseEntity<Object> getWebhookData(WebHookPayload webHookPayload) {
        if (webHookPayload != null){
            WebHookPayload.Data data = webHookPayload.getData();
            /**
             * load payment record by reference number
             */
            Optional<Payment> payment = paymentMapper.findByReference(data.getReference());
            if (payment.isPresent() && data.getStatus().equalsIgnoreCase("success")){
                /**
                 * prepare payload to be updated
                 */
                Payment existingData = payment.get();
                existingData.setPaymentMethod(data.getChannel());
                existingData.setStatus(AppConstants.PAID);
                existingData.setPaidAt(data.getPaid_at());
                existingData.setCurrency(data.getCurrency());

                /**
                 * save updated payment records
                 */
                 Integer affectedRows = paymentMapper.updateById(existingData);
                 if (affectedRows<=0){
                     log.error("Payment records fails to update:->>{}", existingData);
                 }
                /**
                 * update the respective entity
                 */
                Optional<Prescription> prescriptionOptional = prescriptionMapper.findById(existingData.getEntityId());
                Optional<Lab> labOptional = labMapper.findById(existingData.getEntityId());
                if (prescriptionOptional.isPresent()){
                    Prescription prescription = prescriptionOptional.get();
                    prescription.setStatus(AppConstants.PAID);
                    Integer affectedPrescriptionRows = paymentMapper.updateById(existingData);
                    if (affectedPrescriptionRows<=0){
                        log.error("Prescription records failed to update:->>{}", existingData);
                        throw new ServerException("Lab records failed to update");
                    }
                } else if (labOptional.isPresent()) {
                    Lab lab = labOptional.get();
                    lab.setStatus(AppConstants.PAID);
                    Integer affectedLabRows = paymentMapper.updateById(existingData);
                    if (affectedLabRows<=0){
                        log.error("Lab records failed to update:->>{}", existingData);
                        throw new ServerException("Lab records failed to update");
                    }
                }
                /**
                 * send  acknowledgment back to payStack
                 */
                return new ResponseEntity<>(HttpStatus.OK);
            }

        }
        return null;
    }
}
