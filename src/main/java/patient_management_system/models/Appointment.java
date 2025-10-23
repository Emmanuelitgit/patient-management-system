package patient_management_system.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    private String id;
    @NotBlank(message = "Patient id cannot be null or empty")
    private String patientId;
    @NotBlank(message = "Doctor id cannot be null or empty")
    private String doctorId;
    @NotNull(message = "Appointment date cannot be null")
    private LocalDate date;
    @NotBlank(message = "Appointment charge cannot be null or empty")
    private String appointmentChargeId;
    @NotNull(message = "Appointment time cannot be null")
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String remarks;
    private String createdBy;
    private LocalDate createdAt;
    private String updatedBy;
    private LocalDate updatedAt;
}
