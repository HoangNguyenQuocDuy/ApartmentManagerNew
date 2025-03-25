package hnqd.aparmentmanager.accessservice.service;

import hnqd.aparmentmanager.accessservice.dto.EntryRightRequest;
import hnqd.aparmentmanager.accessservice.entity.EntryRight;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IEntryRightService {

    Page<EntryRight> getEntryRightsPaging(Map<String, String> params);

    EntryRight createEntryRight(EntryRightRequest entryRightRequest);

    EntryRight updateEntryRight(int erId, Map<String, String> params);

    void deleteEntryRight(int erId);

}
