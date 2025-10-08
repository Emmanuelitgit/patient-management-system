package patient_management_system.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.service.PatientService;

@Tag(name = "Patient Management")
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "This endpoint is used to fetch all patients")
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(){
        return patientService.findAll();
    }

    @Operation(summary = "This endpoint is used to fetch patient records by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id){
        return patientService.findById(id);
    }
}
