package project.emergencyApplication.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.emergencyApplication.dto.Person;

@RestController
public class PersonController {

    @ApiOperation(value="Find person by ID", notes="Provide an ID to look up a specific person from the people data.", response= Person.class)
    @GetMapping("/people/{id}")
    public Person findPersonById(@ApiParam(value="ID value for the person you need to retrieve", required=true) @PathVariable Long id) {
        // Method implementation
        return new Person();
    }
}
