package hnqd.aparmentmanager.lockerservice.repository.impl;

import hnqd.project.ApartmentManagement.dto.OrderRequest;
import hnqd.project.ApartmentManagement.entity.Locker;
import hnqd.project.ApartmentManagement.entity.Order;
import hnqd.project.ApartmentManagement.exceptions.CommonException;
import hnqd.project.ApartmentManagement.repository.ILockerRepo;
import hnqd.project.ApartmentManagement.repository.IOrderRepo;
import hnqd.project.ApartmentManagement.service.IOrderService;
import hnqd.project.ApartmentManagement.utils.UploadImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderRepo orderRepo;
    @Autowired
    private ILockerRepo lockerRepo;
    @Autowired
    private UploadImage uploadImage;

    @Override
    public Order createOrder(OrderRequest orderRequest) throws IOException {
        Order order = new Order();

        Locker locker = lockerRepo.findById(orderRequest.getLockerId()).orElseThrow(
                () -> new CommonException.NotFoundException("Locker not found with ID: " + orderRequest.getLockerId())
        );

        if (locker.getStatus().equals("Using")) {
            if (orderRequest.getFile() != null && !orderRequest.getFile().isEmpty()) {
                order.setImage(uploadImage.uploadToCloudinary(orderRequest.getFile()));
            }

            order.setStatus("Pending");
            order.setLocker(locker);
            order.setCreatedAt(new Timestamp(new Date().getTime()));

            return orderRepo.save(order);
        } else {
            throw new CommonException.LockerIsUnOccupiedException("Locker is blank status!");
        }
    }

    @Override
    public Order updateOrder(int orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(
                () -> new CommonException.NotFoundException("Order not found with ID: " + orderId)
        );

        if (order.getStatus().equals("Pending")) {
            order.setStatus("Received");
            order.setUpdatedAt(new Timestamp(new Date().getTime()));

            return orderRepo.save(order);
        }

        return null;
    }

    @Override
    public Page<Order> getOrders(Map<String, String> params) {
        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));
        Pageable pageable = PageRequest.of(page, size);

        if (params.get("lockerId") != null && !params.get("lockerId").isEmpty()) {
            return orderRepo.findAllByLockerId(Integer.parseInt(params.get("lockerId")), pageable);
        } else if (params.get("status") != null && !params.get("status").isEmpty()) {
            return orderRepo.findAllByStatus(params.get("status"), pageable);
        } else {
            return orderRepo.findAll(pageable);
        }
    }

    @Override
    public List<Order> getListOrder() {
        return orderRepo.findAll();
    }

    @Override
    public Order getOrderById(int orderId) {
        return orderRepo.findById(orderId).orElseThrow(
                () -> new CommonException.NotFoundException("Order not found with ID: " + orderId)
        );
    }

    @Override
    public void deleteOrder(int orderId) {
        orderRepo.findById(orderId).orElseThrow(
                () -> new CommonException.NotFoundException("Order not found with ID: " + orderId)
        );

        orderRepo.deleteById(orderId);
    }
}
