package hnqd.aparmentmanager.lockerservice.repository;

import hnqd.aparmentmanager.lockerservice.entity.Order;
import hnqd.project.ApartmentManagement.dto.OrderRequest;
import hnqd.project.ApartmentManagement.entity.Order;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IOrderService {

    Order createOrder(OrderRequest order) throws IOException;

    Order updateOrder(int orderId);

    Page<Order> getOrders(Map<String, String> params);

    List<Order> getListOrder();

    Order getOrderById(int lockerId);

    void deleteOrder(int orderId);
}
