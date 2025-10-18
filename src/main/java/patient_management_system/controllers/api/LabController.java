package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Lab;
import patient_management_system.serviceImpl.LabServiceImpl;

@Tag(name = "Lab Management")
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
        return labService.findAll(search,size,page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id) {
        return labService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addLab(@RequestBody Lab lab) {
        return labService.addLab(lab);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateById(@RequestBody Lab lab) {
        return labService.updateById(lab);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable String id) {
        return labService.deleteById(id);
    }
}
