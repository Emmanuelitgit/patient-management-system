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
public class Lab {
    private String id;
    @NotBlank(message = "Patient id cannot be null or empty")
    private String patientId;
    @NotBlank(message = "Doctor id cannot be null or empty")
    private String doctorId;
    @NotBlank(message = "Test name cannot be null or empty")
    private String testName;
    @NotBlank(message = "Result cannot be null or empty")
    private String result;
    private String status;
    private String createdBy;
    private String updatedBy;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
