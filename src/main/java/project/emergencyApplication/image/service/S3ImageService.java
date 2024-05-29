package project.emergencyApplication.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.emergencyApplication.auth.jwt.utils.SecurityUtil;
import project.emergencyApplication.domain.member.entity.Member;
import project.emergencyApplication.domain.member.repository.MemberRepository;
import project.emergencyApplication.image.dto.FileDetailDto;
import project.emergencyApplication.image.exception.S3Exception;
import project.emergencyApplication.texts.ExceptionTexts;
import project.emergencyApplication.texts.ImageTexts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3ImageService {

    private final AmazonS3 amazonS3;
    private final MemberRepository memberRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public FileDetailDto upload(MultipartFile file) {
        validateImageFileExtention(file);   // 검증

        try {
            return uploadImageToS3(file);
        } catch (IOException e) {
            throw new S3Exception(ExceptionTexts.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    private void validateImageFileExtention(MultipartFile file) {

        if(file.isEmpty() || Objects.isNull(file.getOriginalFilename())){   // 파일이 존재하는지 검증
            throw new S3Exception(ExceptionTexts.EMPTY_FILE_EXCEPTION);
        }

        String fileName = file.getOriginalFilename();

        int lastDotIndex = fileName.lastIndexOf(".");   // 파일 확장자 존재 유무 검증
        if (lastDotIndex == -1) {
            throw new S3Exception(ExceptionTexts.NO_FILE_EXTENTION);
        }

        String extention = fileName.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {     // 파일 확장자 검증
            throw new S3Exception(ExceptionTexts.INVALID_FILE_EXTENTION);
        }
    }

    private FileDetailDto uploadImageToS3(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename(); //파일 명
        String extention = fileName.substring(fileName.lastIndexOf(".")); //확장자 명

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + fileName; //변경된 파일 명

        InputStream is = file.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("file/" + extention);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try{
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest); // put image to S3
        }catch (Exception e){
            throw new S3Exception(ExceptionTexts.PUT_OBJECT_EXCEPTION);
        }finally {
            byteArrayInputStream.close();
            is.close();
        }

        updateImage(s3FileName);

        return new FileDetailDto().multipartOf(extention, file,
                amazonS3.getUrl(bucketName, s3FileName).toString());
    }

    private void updateImage(String s3FileName) {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException(ExceptionTexts.NOT_EXIST.getText()));

        findMember.updateMemberImage(amazonS3.getUrl(bucketName, s3FileName).toString());
    }

    public String deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
            deleteImage();

            return ImageTexts.IMAGE_DELETE_SUCCESS.getText();
        }catch (Exception e){
            throw new S3Exception(ExceptionTexts.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }

    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new S3Exception(ExceptionTexts.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }

    private void deleteImage() {
        Member findMember = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException(ExceptionTexts.NOT_EXIST.getText()));

        findMember.updateMemberImage(null);
    }
}