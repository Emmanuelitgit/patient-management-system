package patient_management_system.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {
    private String id;
    @NotBlank(message = "Appointment cannot be null or empty")
    private String appointmentId;
    private String patientId;
    private String doctorId;
    @NotBlank(message = "Medication cannot be null or empty")
    private String medication;
    @NotBlank(message = "Dosage cannot be null or empty")
    private String dosage;
    @NotBlank(message = "Status cannot be null or empty")
    private String status;
    @NotBlank(message = "Instructions cannot be null or empty")
    private String instructions;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;
}
