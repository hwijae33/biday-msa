package shop.biday.ftp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import shop.biday.ftp.model.document.ImageDocument;
import shop.biday.ftp.model.domain.ImageModel;

import java.util.List;
import java.util.Optional;

public interface ImageService {
    String uploadFileByAdmin(String role, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId);

    String uploadFilesByUser(String role, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId);

    String update(String role, List<MultipartFile> multipartFiles, String id);

    String deleteById(String role, String id);

    ResponseEntity<byte[]> getImage(String id);

//    Optional<ImageDocument> findById(String id);
//
//    ImageModel findByTypeAndUploadPath(String type, String uploadPath);
//
//    ImageModel findByOriginalNameAndType(String name, String type);
//
//    ImageModel findByOriginalNameAndTypeAndReferencedId(String type, String name, Long referencedId);
//
//    ImageModel findByTypeAndReferencedIdAndUploadPath(String type, String referencedId, String uploadPath);
//
//    List<ImageModel> findByTypeAndReferencedId(String type, Long referencedId);
}
