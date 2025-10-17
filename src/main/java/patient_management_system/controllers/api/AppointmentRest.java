package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Appointment;
import patient_management_system.serviceImpl.AppointmentServiceImpl;

@Tag(name = "Appointment Management")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentRest {
    private final AppointmentServiceImpl appointmentServiceImpl;

    @Autowired
    public AppointmentRest(AppointmentServiceImpl appointmentServiceImpl) {
        this.appointmentServiceImpl = appointmentServiceImpl;
    }

    @Operation(summary = "This endpoint is used to fetch all appointments records")
    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestParam(name = "search", required = false) String search,
                                               @RequestParam(name = "startTime", required = false)String startTime,
                                               @RequestParam(name = "endTime", required = false) String endTime,
                                               @RequestParam(name = "size", required = false,defaultValue = "10") Integer size,
                                               @RequestParam(name = "page",required = false, defaultValue = "1") Integer page){
        return appointmentServiceImpl.findAll(search,startTime,endTime,size,page);
    }

    @Operation(summary = "This endpoint is used to fetched appointment record by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id){
        return appointmentServiceImpl.findById(id);
    }

    @Operation(summary = "This endpoint is used to schedule an appointment")
    @PostMapping
    public ResponseEntity<ResponseDTO> addAppointment(@RequestBody @Valid Appointment appointment){
        return appointmentServiceImpl.addAppointment(appointment);
    }

    @Operation(summary = "This endpoint is used to update an appointment record by id")
    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody Appointment appointment){
        return appointmentServiceImpl.updateById(appointment);
    }

    @Operation(summary = "This endpoint is used to deleted an appointment record by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id){
        return appointmentServiceImpl.deleteById(id);
    }

    @Operation(summary = "This endpoint is used to fetch appointments for logged-in doctor")
    @GetMapping("/for-doctor")
    public ResponseEntity<ResponseDTO> fetchAppointmentsForDoctor(@RequestParam(name = "doctorId") String doctorId,
                                                                  @RequestParam(name = "search",required = false) String search,
                                                                  @RequestParam(name = "startTime",required = false) String startTime,
                                                                  @RequestParam(name = "endTime",required = false) String endTime,
                                                                  @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                                                  @RequestParam(name = "page", required = false, defaultValue = "1") Integer page){
        return appointmentServiceImpl.fetchAppointmentsForDoctor(doctorId,search,startTime,endTime,size,page);
    }

    @Operation(summary = "This endpoint is used to update appointment status")
    @PutMapping("/status{id}")
    public ResponseEntity<ResponseDTO> updateAppointmentStatus(@PathVariable String id,
                                                               @RequestParam(name = "status") String status){
        return appointmentServiceImpl.updateAppointmentStatus(id,status);
    }
}
