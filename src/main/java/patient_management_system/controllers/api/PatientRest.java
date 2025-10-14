package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Patient;
import patient_management_system.service.PatientService;

@Tag(name = "Patient Management")
@RestController
@RequestMapping("/api/patients")
public class PatientRest {

    private final PatientService patientService;

    @Autowired
    public PatientRest(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "This endpoint is used to fetch all patients")
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestParam(name = "paginate", defaultValue = "false") boolean paginate,
                                               @RequestParam(name = "page", defaultValue = "1") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size){
        if (paginate){
            return patientService.getPaginatedPatients(size, page);
        }
        return patientService.findAll();
    }

    @Operation(summary = "This endpoint is used to fetch patient records by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id){
        return patientService.findById(id);
    }

    @Operation(summary = "This endpoint is used to add new patient")
    @PostMapping
    public ResponseEntity<ResponseDTO> addPatient(@RequestBody @Valid Patient patient){
        return patientService.addPatient(patient);
    }

    @Operation(summary = "This method is used to update patient record by id")
    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody Patient patient){
        return patientService.updateById(patient);
    }

    @Operation(summary = "This method is used to delete a patient record by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id){
        return patientService.deleteById(id);
    }
}
