package hnqd.aparmentmanager.paymentservice.repository;

import java.util.List;

public interface IStatisticRepo {

    List<Object[]> getTotalRevenueByYear(int year);

    List<Object[]> getRevenueByMonth(int month, int year);

}
