package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Appointment;
import patient_management_system.service.AppointmentService;

@Tag(name = "Appointment Management")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentRest {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentRest(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "This endpoint is used to fetch all appointments records")
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestParam(name = "search", required = false) String search){
        return appointmentService.findAll(search);
    }

    @Operation(summary = "This endpoint is used to fetched appointment record by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id){
        return appointmentService.findById(id);
    }

    @Operation(summary = "This endpoint is used to schedule an appointment")
    @PostMapping
    public ResponseEntity<ResponseDTO> addAppointment(@RequestBody @Valid Appointment appointment){
        return appointmentService.addAppointment(appointment);
    }

    @Operation(summary = "This endpoint is used to update an appointment record by id")
    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody Appointment appointment){
        return appointmentService.updateById(appointment);
    }

    @Operation(summary = "This endpoint is used to deleted an appointment record by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id){
        return appointmentService.deleteById(id);
    }

    @GetMapping("/for-doctor")
    public ResponseEntity<ResponseDTO> fetchAppointmentsForDoctor(@RequestParam(name = "doctorId") String doctorId){
        return appointmentService.fetchAppointmentsForDoctor(doctorId);
    }
}
