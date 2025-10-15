package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Patient;
import patient_management_system.serviceImpl.PatientServiceImpl;

@Tag(name = "Patient Management")
@RestController
@RequestMapping("/api/patients")
public class PatientRest {

    private final PatientServiceImpl patientServiceImpl;

    @Autowired
    public PatientRest(PatientServiceImpl patientServiceImpl) {
        this.patientServiceImpl = patientServiceImpl;
    }

    @Operation(summary = "This endpoint is used to fetch all patients")
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestParam(name = "paginate", defaultValue = "false") boolean paginate,
                                               @RequestParam(name = "page", defaultValue = "1") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size){
        if (paginate){
            return patientServiceImpl.getPaginatedPatients(size, page);
        }
        return patientServiceImpl.findAll();
    }

    @Operation(summary = "This endpoint is used to fetch patient records by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id){
        return patientServiceImpl.findById(id);
    }

    @Operation(summary = "This endpoint is used to add new patient")
    @PostMapping
    public ResponseEntity<ResponseDTO> addPatient(@RequestBody @Valid Patient patient){
        return patientServiceImpl.addPatient(patient);
    }

    @Operation(summary = "This method is used to update patient record by id")
    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody Patient patient){
        return patientServiceImpl.updateById(patient);
    }

    @Operation(summary = "This method is used to delete a patient record by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id){
        return patientServiceImpl.deleteById(id);
    }
}
