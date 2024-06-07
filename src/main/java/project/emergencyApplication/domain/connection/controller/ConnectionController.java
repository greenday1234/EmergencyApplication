package project.emergencyApplication.domain.connection.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.connection.dto.ConnectionResponseDto;
import project.emergencyApplication.domain.connection.service.ConnectionService;

import java.util.List;

@Tag(name = "Connection", description = "계정 연동")
@RestController
@RequiredArgsConstructor
@RequestMapping("/connection")
public class ConnectionController {

    private final ConnectionService connectionService;

    @Operation(summary = "계정 연동 요청 조회")
    @GetMapping("/info")
    public ResponseEntity<List<ConnectionResponseDto>> connectionInfo() {
        return ResponseEntity.ok(connectionService.connectionInfo(SecurityUtil.getCurrentMemberId()));
    }
}
