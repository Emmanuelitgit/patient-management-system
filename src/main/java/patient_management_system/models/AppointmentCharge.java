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
public class AppointmentCharge {
    private String id;
    @NotBlank(message = "charge name cannot be null or empty")
    private String name;
    @NotBlank(message = "Price cannot be null or empty")
    private Double price;
    private Boolean enabled;
    private String createdBy;
    private LocalDate createdAt;
    private String updatedBy;
    private LocalDate updatedAt;

}
