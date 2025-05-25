package hnqd.aparmentmanager.paymentservice.service.impl;

import hnqd.aparmentmanager.paymentservice.entity.Invoice;
import hnqd.aparmentmanager.paymentservice.service.IStatService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatServiceImpl implements IStatService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Object[]> getRevenueByMonth(int month, int year) {
        CriteriaBuilder b = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root invoice = q.from(Invoice.class);

        Expression<Integer> invoiceMonth = b.function("MONTH", Integer.class, invoice.get("dueDate"));
        Expression<Integer> invoiceYear = b.function("YEAR", Integer.class, invoice.get("dueDate"));

        q.multiselect(
                invoice.get("invoiceType"),
                b.sum(invoice.get("amount"))
        );

        q.where(
                b.equal(invoice.get("invoiceStatus"), "Completed"),
                b.equal(invoiceMonth, month),
                b.equal(invoiceYear, year)
        );

        q.groupBy(invoice.get("invoiceType"));
//        q.orderBy(b.asc(invoiceType.get("type")));

        List<Object[]> results = entityManager.createQuery(q).getResultList();

        return results;
    }

    @Override
    public List<Object[]> getTotalRevenueByYear(int year) {
        CriteriaBuilder b = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);

        Root<Invoice> invoice = q.from(Invoice.class);

        Expression<Integer> invoiceMonth = b.function("MONTH", Integer.class, invoice.get("dueDate"));
        Expression<Integer> invoiceYear = b.function("YEAR", Integer.class, invoice.get("dueDate"));

        q.multiselect(
                invoiceMonth,
                b.sum(invoice.get("amount"))
        );
        q.where(
                b.equal(invoice.get("invoiceStatus"), "Completed"),
                b.equal(invoiceYear, year)
        );
        q.groupBy(invoiceMonth);
        q.orderBy(b.asc(invoiceMonth));
        List<Object[]> results = entityManager.createQuery(q).getResultList();

        Map<Integer, Object[]> revenueMap = new HashMap<>();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            revenueMap.put(month, result);
        }

        List<Object[]> finalResults = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            if (revenueMap.containsKey(month)) {
                finalResults.add(revenueMap.get(month));
            } else {
                finalResults.add(new Object[]{month, 0});
            }
        }

        return finalResults;
    }
}
