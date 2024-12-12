package hnqd.apartmentmanager.roomservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "upload-service", url = "http://localhost:9004")
public interface IUploadService {
    @PostMapping("/cloudinary/upload")
    String uploadImageToCloudinary(@PathVariable("file") MultipartFile files);
}
