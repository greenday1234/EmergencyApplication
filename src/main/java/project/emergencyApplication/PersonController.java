package project.emergencyApplication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class PersonController {
    private final PersonRepository personRepository;
    @PostMapping("/users/save")
    public void personSave(@RequestBody Person person) {
        personRepository.save(person);

    }
}
