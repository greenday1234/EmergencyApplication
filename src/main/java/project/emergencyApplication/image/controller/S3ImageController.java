package project.emergencyApplication.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.emergencyApplication.image.dto.FileDetailDto;
import project.emergencyApplication.image.service.S3ImageService;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3ImageController {

    private final S3ImageService s3ImageService;

    @PostMapping("/upload")
    public ResponseEntity<FileDetailDto> s3Upload(@RequestPart(value = "file") MultipartFile file){
        return ResponseEntity.ok(s3ImageService.upload(file));
    }

    @GetMapping("/delete")
    public ResponseEntity<String> s3delete(@RequestParam String addr){
        return ResponseEntity.ok(s3ImageService.deleteImageFromS3(addr));
    }
}
