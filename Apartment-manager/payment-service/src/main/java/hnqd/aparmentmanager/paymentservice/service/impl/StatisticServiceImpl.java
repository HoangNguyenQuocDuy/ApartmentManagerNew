package hnqd.aparmentmanager.paymentservice.service.impl;

import hnqd.aparmentmanager.paymentservice.repository.IStatisticRepo;
import hnqd.aparmentmanager.paymentservice.service.IStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements IStatisticService {

    private final IStatisticRepo statRepo;

    @Override
    public List<Object[]> getRevenueByMonth(int month, int year) {
        return statRepo.getRevenueByMonth(month, year);
    }

    @Override
    public List<Object[]> getTotalRevenueByYear(int year) {
        return statRepo.getTotalRevenueByYear(year);
    }

}
