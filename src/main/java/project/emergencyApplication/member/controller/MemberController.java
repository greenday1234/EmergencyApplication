package project.emergencyApplication.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @GetMapping("/api/test")
    public String publicApi() {

        return "member";
    }
}
