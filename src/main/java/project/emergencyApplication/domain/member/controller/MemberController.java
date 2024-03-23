package project.emergencyApplication.domain.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @GetMapping("/api/test")
    public String publicApi() {

        return "member";
    }

    @GetMapping("/api/test1")
    public String publicApi1() {
        return "이건 되냐?";
    }
}
