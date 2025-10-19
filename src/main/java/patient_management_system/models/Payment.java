package patient_management_system.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String id;
    @NotBlank(message = "Patient id cannot be null or empty")
    private String patientId;
    @NotBlank(message = "Entity id cannot be null or empty")
    private String entityId;
    @NotBlank(message = "Amount cannot be null")
    private Double amount;
    private String accessCode;
    private String paidAt;
    private String currency;
    private String status;
    private String paymentMethod;
    private String transactionId;
    private String referenceNumber;
    private String authorizationUrl;
    @NotBlank(message = "Service type cannot be null or empty")
    private String serviceType;
    private String createdBy;
    private LocalDate createdAt;
    private String updatedBy;
    private LocalDate updatedAt;
}
