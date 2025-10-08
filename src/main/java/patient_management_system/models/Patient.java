package patient_management_system.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    private String id;
    private String name;
    private String email;

    private void setId(String id){
        this.id= UUID.randomUUID().toString();
    }
}
