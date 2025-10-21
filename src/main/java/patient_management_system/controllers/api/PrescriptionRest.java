package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Prescription;
import patient_management_system.serviceImpl.PrescriptionServiceImpl;

@Tag(name = "Prescription Management")
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionRest {
    private final PrescriptionServiceImpl prescriptionService;

    @Autowired
    public PrescriptionRest(PrescriptionServiceImpl prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @Operation(summary = "This is endpoint is used to fetch all prescriptions")
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestParam(name = "search",required = false) String search,
                                               @RequestParam(value = "size",required = false,defaultValue = "10") Integer size,
                                               @RequestParam(name = "page",required = false,defaultValue = "1") Integer page){
        return prescriptionService.findAll(search,size,page);
    }

    @Operation(summary = "This endpoint is used to fetch prescriptions by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id){
        return prescriptionService.findById(id);
    }

    @Operation(summary = "This endpoint is used to add new prescription")
    @PostMapping
    public ResponseEntity<ResponseDTO> addPrescription(@RequestBody @Valid Prescription prescription){
        return prescriptionService.addPrescription(prescription);
    }

    @Operation(summary = "This endpoint is used to update prescription record given id")
    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody Prescription prescription){
        return prescriptionService.updateById(prescription);
    }

    @Operation(summary = "This endpoint is used to delete prescription record")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id){
        return prescriptionService.deleteById(id);
    }

    @Operation(summary = "This endpoint is used to fetch prescriptions for logged-in practitioner")
    @GetMapping("/prescription-for-practitioner/{practitionerId}")
    public ResponseEntity<ResponseDTO> fetchPrescriptionsForPractitioner(@PathVariable String practitionerId,
                                                                         @RequestParam(name = "search",required = false) String search,
                                                                         @RequestParam(name = "size", required = false,defaultValue = "10") Integer size,
                                                                         @RequestParam(name = "page", required = false,defaultValue = "1") Integer page){
        return prescriptionService.fetchPrescriptionsForPractitioner(practitionerId,search,size,page);

    }

    @Operation(summary = "This endpoint is used to update prescription status")
    @PutMapping("/status/{id}")
    public ResponseEntity<ResponseDTO> updatePrescriptionStatus(@PathVariable String id,
                                                                @RequestParam(name = "status") String status){
        return prescriptionService.updatePrescriptionStatus(id,status);
    }
}
