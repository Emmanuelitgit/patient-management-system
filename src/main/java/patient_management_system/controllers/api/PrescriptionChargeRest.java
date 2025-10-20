package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.PrescriptionCharge;
import patient_management_system.serviceImpl.PrescriptionChargeServiceImpl;

@Tag(name = "Prescription Charges")
@RestController
@RequestMapping("/api/prescription-charges")
public class PrescriptionChargeRest {

    private final PrescriptionChargeServiceImpl prescriptionChargeService;

    @Autowired
    public PrescriptionChargeRest(PrescriptionChargeServiceImpl prescriptionChargeService) {
        this.prescriptionChargeService = prescriptionChargeService;
    }

    @Operation(summary = "This endpoint is used to fetch all prescription charges")
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestParam(name = "search", required = false) String search,
                                               @RequestParam(name = "size", required = false) Integer size,
                                               @RequestParam(name = "page",required = false) Integer page) {
        return prescriptionChargeService.findAll(search, size, page);
    }

    @Operation(summary = "This endpoint is used to fetch prescription charge by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id) {
        return prescriptionChargeService.findById(id);
    }

    @Operation(summary = "This endpoint is used to add new prescription charge")
    @PostMapping
    public ResponseEntity<ResponseDTO> addPrescriptionCharge(@RequestBody PrescriptionCharge prescriptionCharge){
        return prescriptionChargeService.addPrescriptionCharge(prescriptionCharge);
    }

    @Operation(summary = "This endpoint is used to add new prescription charge record")
    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody PrescriptionCharge prescriptionCharge) {
        return prescriptionChargeService.updateById(prescriptionCharge);
    }

    @Operation(summary = "This endpoint is used to delete prescription charge by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id) {
        return prescriptionChargeService.deleteById(id);
    }
}