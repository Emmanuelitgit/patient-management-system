package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.AppointmentCharge;
import patient_management_system.serviceImpl.AppointmentChargeServiceImpl;

@Tag(name = "Appointment Charges")
@RestController
@RequestMapping("/api/appointment-charges")
public class AppointmentChargeRest{

    private final AppointmentChargeServiceImpl appointmentChargeService;

    @Autowired
    public AppointmentChargeRest(AppointmentChargeServiceImpl appointmentChargeService) {
        this.appointmentChargeService = appointmentChargeService;
    }

    @Operation(summary = "This endpoint is used to fetch all appointment charges")
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestParam(name = "search",required = false) String search,
                                               @RequestParam(name = "size",required = false,defaultValue = "10") Integer size,
                                               @RequestParam(name = "page",required = false,defaultValue = "1") Integer page) {
        return appointmentChargeService.findAll(search, size, page);
    }

    @Operation(summary = "This endpoint is used to fetch appointment charge by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id) {
        return appointmentChargeService.findById(id);
    }

    @Operation(summary = "This endpoint is used to update appointment charge record")
    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody AppointmentCharge appointmentCharge) {
        return appointmentChargeService.updateById(appointmentCharge);
    }

    @Operation(summary = "This endpoint is used to delete appointment charge record by id")
    @DeleteMapping
    public ResponseEntity<ResponseDTO> deleteById(String id) {
        return appointmentChargeService.deleteById(id);
    }

    @Operation(summary = "This endpoint is used to add new appointment charge record")
    @PostMapping
    public ResponseEntity<ResponseDTO> addAppointmentCharge(@RequestBody AppointmentCharge appointmentCharge) {
        return appointmentChargeService.addAppointmentCharge(appointmentCharge);
    }
}
