package patient_management_system.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private String id;
    private String doctorId;
    private String patientId;
    private String doctor;
    private String patient;
    private String status;
    private String remarks;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String createdBy;
    private String updatedBy;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
