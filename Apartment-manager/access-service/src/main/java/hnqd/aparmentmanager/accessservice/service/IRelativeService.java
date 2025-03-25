package hnqd.aparmentmanager.accessservice.service;

import hnqd.aparmentmanager.accessservice.dto.RelativeRequest;
import hnqd.aparmentmanager.accessservice.entity.Relative;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface IRelativeService {

    Page<Relative> getListRelative(Map<String, String> query);

    Relative createRelative(RelativeRequest relativeRequest) throws IOException;

    Relative updateRelative(int relativeId, MultipartFile file, Map<String, String> params) throws IOException;

    void deleteRelative(int relativeId);
}
