package patient_management_system.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Lab;
import patient_management_system.service.LabService;
import patient_management_system.serviceImpl.LabServiceImpl;

@RestController
@RequestMapping("/labs")
public class LabController {

    private final LabServiceImpl labService;

    @Autowired
    public LabController(LabServiceImpl labService) {
        this.labService = labService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> findAll(@RequestParam(name = "search", required = false) String search,
                                               @RequestParam(name = "size", required = false,defaultValue = "10") Integer size,
                                               @RequestParam(name = "page", required = false,defaultValue = "1") Integer page) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addLab(@RequestBody Lab lab) {
        return null;
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody Lab lab) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id) {
        return null;
    }

    @GetMapping("/for-doctor/{doctorId}")
    public ResponseEntity<ResponseDTO> fetchLabsForDoctor(String search,
                                                          @RequestParam(name = "size", required = false,defaultValue = "10") Integer size,
                                                          @RequestParam(name = "page", required = false,defaultValue = "1") Integer page,
                                                          @PathVariable String doctorId) {
        return null;
    }
}
