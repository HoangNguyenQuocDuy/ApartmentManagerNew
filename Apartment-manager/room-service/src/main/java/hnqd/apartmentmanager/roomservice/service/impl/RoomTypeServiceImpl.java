package hnqd.apartmentmanager.roomservice.service.impl;

import hnqd.apartmentmanager.roomservice.entity.RoomType;
import hnqd.apartmentmanager.roomservice.repository.IRoomTypeRepo;
import hnqd.apartmentmanager.roomservice.service.IRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomTypeServiceImpl implements IRoomTypeService {
    @Autowired
    private IRoomTypeRepo roomTypeRepo;

    @Override
    public List<RoomType> getRoomTypes() {
        return roomTypeRepo.findAll();
    }
}
