package hnqd.aparmentmanager.visitorservice.service;

import hnqd.aparmentmanager.common.dto.response.ListResponse;
import hnqd.aparmentmanager.common.dto.response.RestResponse;
import hnqd.aparmentmanager.visitorservice.dto.response.VisitRequestVisitorRes;

import java.util.Map;

public interface IVisitRequestVisitorService {

    RestResponse<ListResponse<VisitRequestVisitorRes>> getListVisitRequestVisitor(Map<String, String> filterMap);

}
