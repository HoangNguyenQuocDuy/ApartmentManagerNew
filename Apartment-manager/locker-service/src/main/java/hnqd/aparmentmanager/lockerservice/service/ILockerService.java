package hnqd.aparmentmanager.lockerservice.repository;

import hnqd.aparmentmanager.lockerservice.entity.Locker;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ILockerService {
    Locker createLocker();
    Locker updateLocker(int lockerId, Map<String,String> body);
    void deleteLocker(int id);
    Page<Locker> getLockersPaging(Map<String, String> params);
    Locker getLockerById(int id);
    List<Locker> getLockers(Map<String, String> params);
}
