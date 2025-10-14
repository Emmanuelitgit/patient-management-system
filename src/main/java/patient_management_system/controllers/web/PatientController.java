package patient_management_system.controllers.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Patient;
import patient_management_system.service.PatientService;

@Slf4j
@Controller
@RequestMapping("/view")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patients")
    public String viewPatients(Model model){
        ResponseEntity<ResponseDTO> response = patientService.findAll();
        if (response.getStatusCode().is2xxSuccessful()){
            log.info("Status:->>{}", response.getStatusCode());
        }
        model.addAttribute("patients",response.getBody().getData());

        return "patients";
    }

    @GetMapping("/addPatientForm")
    public String showAddPatientForm(Model model){
        model.addAttribute("patient", new Patient());
        return "addPatient";
    }

    @PostMapping("/addPatient")
    public String addPatient(@ModelAttribute @Valid Patient patient){
        ResponseEntity<ResponseDTO> response = patientService.addPatient(patient);
        if (!response.getStatusCode().is2xxSuccessful()){}

        return "redirect:/view/patients";
    }

    @GetMapping("/viewPatient/{id}")
    public String viewPatient(@PathVariable String id, Model model){
        ResponseEntity<ResponseDTO> response = patientService.findById(id);
        if (!response.getStatusCode().is2xxSuccessful()){}

        model.addAttribute("patient", response.getBody().getData());

        return "patient";
    }

    @GetMapping("/updatePatientForm/{id}")
    public String showUpdatePatientForm(@PathVariable String id, Model model){
        ResponseEntity<ResponseDTO> existingPatient = patientService.findById(id);
        model.addAttribute("patient", existingPatient);

        return "updatePatient";
    }

    @PutMapping("/updatePatient")
    public String updatePatient(@ModelAttribute Patient patient){
        ResponseEntity<ResponseDTO> response = patientService.updateById(patient);
        if (!response.getStatusCode().is2xxSuccessful()){}

        return "redirect:/view/patients";
    }

    @DeleteMapping("/deletePatient/{id}")
    public String deletePatient(@PathVariable String id){
        ResponseEntity<ResponseDTO> response = patientService.deleteById(id);
        if (!response.getStatusCode().is2xxSuccessful()){}

        return "redirect:/view/patients";
    }

}
