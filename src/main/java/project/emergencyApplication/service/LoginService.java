package project.emergencyApplication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.emergencyApplication.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    
}
