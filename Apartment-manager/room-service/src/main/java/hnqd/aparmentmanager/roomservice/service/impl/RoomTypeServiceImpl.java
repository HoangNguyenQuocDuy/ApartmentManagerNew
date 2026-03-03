package hnqd.aparmentmanager.roomservice.service.impl;

import hnqd.aparmentmanager.roomservice.entity.RoomType;
import hnqd.aparmentmanager.roomservice.repository.IRoomTypeRepo;
import hnqd.aparmentmanager.roomservice.service.IRoomTypeService;
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
