package patient_management_system.service;

import org.springframework.http.ResponseEntity;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.dto.WebHookPayload;
import patient_management_system.models.Lab;
import patient_management_system.models.Payment;

public interface PaymentService {
    ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page);
    ResponseEntity<ResponseDTO> findById(String id);
    ResponseEntity<ResponseDTO> updateById(Payment payment);
    ResponseEntity<ResponseDTO> deleteById(String id);
    ResponseEntity<ResponseDTO> generateInvoice(Payment payment);
    ResponseEntity<Object> getWebhookData(WebHookPayload webHookPayload);
}
