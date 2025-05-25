package hnqd.aparmentmanager.accessservice.service;

import hnqd.aparmentmanager.accessservice.dto.RelativeRequest;
import hnqd.aparmentmanager.accessservice.entity.Relative;
import hnqd.aparmentmanager.common.dto.response.ContractResponse;
import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface IRelativeService {

    RestResponse<ListResponse<Relative>> getListRelative(Map<String, String> query);

    Relative createRelative(RelativeRequest relativeRequest) throws IOException;

    Relative updateRelative(int relativeId, MultipartFile file, Map<String, String> params) throws IOException;

    void deleteRelative(int relativeId);

    RestResponse<ListResponse<Relative>> getListRelative(int page, int size,
                                                                 String sort, String filter,
                                                                 String search, boolean all);

}
