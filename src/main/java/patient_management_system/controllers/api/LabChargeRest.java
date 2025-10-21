package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.LabCharge;
import patient_management_system.service.LabChargeService;
import patient_management_system.serviceImpl.LabChargeServiceImpl;

@Tag(name = "Lab Charges")
@RestController
@RequestMapping("/api/lab-charges")
public class LabChargeRest{

    private final LabChargeServiceImpl labChargeService;

    @Autowired
    public LabChargeRest(LabChargeServiceImpl labChargeService) {
        this.labChargeService = labChargeService;
    }

    @Operation(summary = "This endpoint is used to fetch all lab charges")
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestParam(name = "search", required = false) String search,
                                               @RequestParam(name = "size",defaultValue = "10", required = false) Integer size,
                                               @RequestParam(name = "page",defaultValue = "1", required = false) Integer page) {
        return labChargeService.findAll(search, size, page);
    }

    @Operation(summary = "This endpoint is used to fetch a lab charge by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id) {
        return labChargeService.findById(id);
    }

    @Operation(summary = "This endpoint is used to update a lab charge record")
    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody LabCharge labCharge) {
        return labChargeService.updateById(labCharge);
    }

    @Operation(summary = "This endpoint is used to delete a lab charge record by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id) {
        return labChargeService.deleteById(id);
    }

    @Operation(summary = "This endpoint is used to add new lab charge record")
    @PostMapping
    public ResponseEntity<ResponseDTO> addLabCharge(@RequestBody LabCharge labCharge) {
        return labChargeService.addLabCharge(labCharge);
    }
}
