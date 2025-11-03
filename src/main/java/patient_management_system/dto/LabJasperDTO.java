package patient_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabJasperDTO {
    private String patientName;
    private String doctorName;
    private String testName;
    private String result;
    private String date;
}
