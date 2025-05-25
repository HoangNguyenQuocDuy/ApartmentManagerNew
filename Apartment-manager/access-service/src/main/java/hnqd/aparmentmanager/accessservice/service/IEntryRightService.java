package hnqd.aparmentmanager.accessservice.service;

import hnqd.aparmentmanager.accessservice.dto.EntryRightRequest;
import hnqd.aparmentmanager.accessservice.entity.EntryRight;
import hnqd.aparmentmanager.accessservice.entity.Relative;
import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IEntryRightService {

    RestResponse<ListResponse<EntryRight>> getEntryRightsPaging(Map<String, String> params);

    EntryRight createEntryRight(EntryRightRequest entryRightRequest);

    EntryRight updateEntryRight(int erId, Map<String, String> params);

    void deleteEntryRight(int erId);

}
