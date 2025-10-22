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
public class LabCharge {
    private String id;
    @NotBlank(message = "Lab charge name cannot be null or empty")
    private String name;
    @NotBlank(message = "Price cannot be null or empty")
    private Double price;
    private Double enabled;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;
}
