package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.dto.WebHookPayload;
import patient_management_system.models.Payment;
import patient_management_system.serviceImpl.PaymentServiceImpl;

@Tag(name = "Payment Management")
@RestController
@RequestMapping("/api/payments")
public class PaymentRest {

    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentRest(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestParam(name = "search", required = false) String search,
                                               @RequestParam(name = "size", required = false) Integer size,
                                               @RequestParam(name = "page", required = false) Integer page) {
        return paymentService.findAll(search, size, page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id) {
        return paymentService.deleteById(id);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody Payment payment) {
        return paymentService.updateById(payment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id) {
        return paymentService.deleteById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> generateInvoice(Payment payment) {
        return paymentService.generateInvoice(payment);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Object> getWebhookData(@RequestBody WebHookPayload webHookPayload) {
        return paymentService.getWebhookData(webHookPayload);
    }
}
