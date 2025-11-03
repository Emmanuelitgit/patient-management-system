package patient_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceJasperDTO {
    private String transactionId;
    private String invoiceId;
    private String invoiceDate;
    private String patientName;
    private String itemName;
    private Double amount;
    private String description;
    private String doctorName;
}
