package hnqd.aparmentmanager.visitorservice.service;

import hnqd.aparmentmanager.visitorservice.entity.Visitor;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IVisitorService {

    Page<Visitor> getVisitors(Map<String, String> params);

    void deleteVisitor(Integer visitorId);

}
