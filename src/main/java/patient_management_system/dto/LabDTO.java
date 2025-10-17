package patient_management_system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LabDTO {
    private String id;
    private String patient;
    private String doctor;
    private String testName;
    private String result;
    private String status;
    private String createdBy;
    private String updatedBy;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
