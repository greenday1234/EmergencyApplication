package project.emergencyApplication.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDetailDto {

    private String name;
    private String format;
    private String path;
    private long bytes;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public FileDetailDto multipartOf(String extention, MultipartFile file, String url) {
        return FileDetailDto.builder()
                .name(file.getOriginalFilename())
                .format(extention)
                .path(url)
                .bytes(file.getSize())
                .build();
    }
}
