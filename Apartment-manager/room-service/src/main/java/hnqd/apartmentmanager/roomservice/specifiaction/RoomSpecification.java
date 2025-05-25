package hnqd.apartmentmanager.roomservice.specifiaction;

import hnqd.apartmentmanager.roomservice.entity.Room;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RoomSpecification {

    public static Specification<Room> filterByRoomIds(List<Integer> roomIds) {
        return (root, query, cb) ->
                root.get("id").in(roomIds);
    }

    public static Specification<Room> hasStatus(String status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

}
