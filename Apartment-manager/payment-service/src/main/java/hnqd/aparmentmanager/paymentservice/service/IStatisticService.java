package hnqd.aparmentmanager.paymentservice.service;

import java.util.List;

public interface IStatisticService {

    List<Object[]> getRevenueByMonth(int month, int year);

    List<Object[]> getTotalRevenueByYear(int year);

}
