package project.emergencyApplication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import project.emergencyApplication.service.LoginService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    public void login() {

    }

}
