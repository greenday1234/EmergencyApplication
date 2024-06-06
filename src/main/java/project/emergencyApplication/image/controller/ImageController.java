package project.emergencyApplication.image.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.emergencyApplication.image.dto.FileDetailDto;
import project.emergencyApplication.image.service.ImageService;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
@Tag(name = "S3-Image", description = "프로필 이미지 업로드 및 삭제")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDetailDto> s3Upload(@RequestPart(value = "file") MultipartFile file){
        return ResponseEntity.ok(imageService.upload(file));
    }

    @GetMapping("/delete")
    public ResponseEntity<String> s3Delete(@RequestParam String address){
        return ResponseEntity.ok(imageService.deleteImageFromS3(address));
    }
}
