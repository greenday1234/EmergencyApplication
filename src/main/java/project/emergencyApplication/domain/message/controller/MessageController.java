package project.emergencyApplication.domain.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.emergencyApplication.domain.message.dto.MessageRequestDto;
import project.emergencyApplication.domain.message.dto.MessageResponseDto;
import project.emergencyApplication.domain.message.service.MessageService;

@Tag(name = "Message", description = "메시지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "메시지 조회")
    @GetMapping("/info")
    public ResponseEntity<MessageResponseDto> messageInfo(@RequestBody MessageRequestDto requestDto) {
        return ResponseEntity.ok(messageService.messageInfo(requestDto));
    }
}
