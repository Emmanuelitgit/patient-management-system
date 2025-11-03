package patient_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionJasperDTO {
    private String patientName;
    private String doctorName;
    private String date;
    private String medication;
    private String instructions;
    private String dosage;

}
