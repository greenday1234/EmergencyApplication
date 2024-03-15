package project.emergencyApplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.emergencyApplication.dto.Person;

@RestController
public class PersonController {

    @GetMapping("/people/{id}")
    public Person findPersonById(@PathVariable Long id) {
        // Method implementation
        return new Person();
    }
}
