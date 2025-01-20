package hnqd.aparmentmanager.lockerservice.repository.impl;

import hnqd.project.ApartmentManagement.entity.Locker;
import hnqd.project.ApartmentManagement.entity.User;
import hnqd.project.ApartmentManagement.exceptions.CommonException;
import hnqd.project.ApartmentManagement.repository.ILockerRepo;
import hnqd.project.ApartmentManagement.repository.IUserRepo;
import hnqd.project.ApartmentManagement.service.ILockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LockerService implements ILockerService {

    @Autowired
    private ILockerRepo lockerRepo;
    @Autowired
    private IUserRepo userRepo;

    @Override
    public Locker createLocker() {
        Locker locker = new Locker();
        locker.setStatus("Blank");

        return lockerRepo.save(locker);
    }

    @Override
    public Locker updateLocker(int lockerId, Map<String, String> body) {
        Locker locker = lockerRepo.findById(lockerId).orElseThrow(() -> (
                new CommonException.NotFoundException("Locker with id " + lockerId + " not found")
        ));
        User userRequest = userRepo.findById(Integer.parseInt(body.get("userId"))).orElseThrow(
                () -> new CommonException.NotFoundException("User with id " + body.get("userId") + " not found")
        );

        if (locker.getStatus().equals("Blank")) {
            locker.setStatus("Using");
            Locker lockerUpdate = lockerRepo.save(locker);

            userRequest.setLocker(lockerUpdate);
            userRepo.save(userRequest);

            return lockerUpdate;

        } else {
            locker.setStatus("Blank");
            userRequest.setLocker(null);
            userRepo.save(userRequest);

            return lockerRepo.save(locker);
        }
    }

    @Override
    public void deleteLocker(int id) {
        Locker locker = lockerRepo.findById(id).orElseThrow(() -> (
                new CommonException.NotFoundException("Locker with id " + id + " not found")
        ));

        if (locker.getStatus().equals("Blank")) {
            lockerRepo.delete(locker);
        } else {
            throw new CommonException.NotFoundException("Locker with id " + id + " is not blank");
        }
    }

    @Override
    public List<Locker> getLockers(Map<String, String> params) {
        if (params.get("status") != null && !params.get("status").isEmpty()) {
            return lockerRepo.findAllByStatus(params.get("status"));
        } else {
            return lockerRepo.findAll();
        }
    }

    @Override
    public Page<Locker> getLockersPaging(Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));
        Pageable pageable = PageRequest.of(page, size);
        return lockerRepo.findAll(pageable);
    }

    @Override
    public Locker getLockerById(int id) {
        return lockerRepo.findById(id).orElseThrow(
                () -> new CommonException.NotFoundException("Locker not found!")
        );
    }
}
