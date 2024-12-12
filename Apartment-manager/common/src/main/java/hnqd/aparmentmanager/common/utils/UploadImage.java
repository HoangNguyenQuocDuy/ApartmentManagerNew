package hnqd.aparmentmanager.common.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UploadImage {
//    @Lazy
    @Autowired
    private Cloudinary cloudinary;

    public String uploadToCloudinary(MultipartFile file) throws IOException {
        Map res = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "Apartment Management"));

        return res.get("secure_url").toString();
    }

    public List<String> UploadMultipleImageToCloudinary(MultipartFile[] files) throws IOException {
        List<String> results = new ArrayList<>();
        for (MultipartFile file : files) {
            results.add(uploadToCloudinary(file));
        }

        return results;
    }
}
