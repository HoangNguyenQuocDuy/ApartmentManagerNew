package hnqd.aparmentmanager.visitorservice.service.impl;

import hnqd.aparmentmanager.common.exceptions.CommonException;
import hnqd.aparmentmanager.visitorservice.entity.Visitor;
import hnqd.aparmentmanager.visitorservice.repository.IVisitorRepository;
import hnqd.aparmentmanager.visitorservice.service.IVisitorService;
import hnqd.aparmentmanager.visitorservice.specification.VisitorSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VisitorServiceImpl implements IVisitorService {

    private final IVisitorRepository visitorRepository;

    @Autowired
    public VisitorServiceImpl(IVisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public Page<Visitor> getVisitors(Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));
        String idCardNumber = params.getOrDefault("idCardNumber", "");

        Specification<Visitor> spec = Specification.where(null);

        if (!idCardNumber.equals("")) {
            spec = spec.and(VisitorSpecification.containsIdCardNumber(idCardNumber));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return visitorRepository.findAll(spec, pageable);
    }

    @Override
    public void deleteVisitor(Integer visitorId) {
        Visitor visitor = visitorRepository.findById(visitorId).orElseThrow(
                () -> new CommonException.NotFoundException("Visitor not found!")
        );

        visitorRepository.delete(visitor);
    }
}
