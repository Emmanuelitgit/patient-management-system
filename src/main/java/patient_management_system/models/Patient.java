package patient_management_system.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    private String id;
    @NotBlank(message = "Patient name cannot be null or blank")
    private String name;
    @NotBlank(message = "Patient email cannot null or blank")
    private String email;
    @NotNull(message = "Patient date of birth cannot be null")
    private LocalDate dob;
    @NotBlank(message = "Patient phone number cannot b null or blank")
    private String phone;
    @NotBlank(message = "Patient gender cannot be null or blank")
    private String gender;
    @NotBlank(message = "Patient address cannot be null or blank")
    private String address;
    private String medicalHistory;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;
}
