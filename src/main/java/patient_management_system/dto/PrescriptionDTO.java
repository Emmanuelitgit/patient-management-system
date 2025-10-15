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
    private Float dosage;
    private String instructions;
    private LocalDate createAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;
}
