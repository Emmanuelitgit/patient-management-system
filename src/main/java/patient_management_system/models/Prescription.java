package patient_management_system.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {
    private String id;
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String medication;
    private Float dosage;
    private String instructions;
    private LocalDate createAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;
}
