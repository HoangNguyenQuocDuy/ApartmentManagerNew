package hnqd.aparmentmanager.lockerservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class OrderRequest {
    private String status;
    private MultipartFile file;
    private Integer lockerId;
}
