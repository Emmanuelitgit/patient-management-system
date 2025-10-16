package patient_management_system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrescriptionDTO {
    private String id;
    private String appointmentId;
    private String patient;
    private String doctor;
    private String medication;
    private String dosage;
    private String status;
    private String instructions;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;
}
