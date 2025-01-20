package hnqd.aparmentmanager.lockerservice.controller;

import hnqd.project.ApartmentManagement.dto.OrderRequest;
import hnqd.project.ApartmentManagement.dto.ResponseObject;
import hnqd.project.ApartmentManagement.entity.Order;
import hnqd.project.ApartmentManagement.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PutMapping("/{orderId}")
    public ResponseEntity<ResponseObject> confirmedOrder(@PathVariable("orderId") int orderId) {
        Order orderSave = orderService.updateOrder(orderId);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                new ResponseObject("OK", "Update order successfully!", orderSave)
        );
    }

    @PostMapping("/")
    public ResponseEntity<ResponseObject> createOrder(
            @RequestPart("file") MultipartFile file,
            @RequestParam Map<String, String> params
    ) throws IOException {
        OrderRequest orderRequest = OrderRequest.builder()
                .lockerId(Integer.parseInt(params.get("lockerId")))
                .file(file)
                .build();

        Order orderSave = orderService.createOrder(orderRequest);

        messagingTemplate.convertAndSend(
                String.format("/notification/lockers/%s", orderSave.getLocker().getId()),
                "You have new order");

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                new ResponseObject("OK", "Create order successfully!", orderSave)
        );
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResponseObject> deleteOrder(@PathVariable("orderId") int orderId) {
        orderService.deleteOrder(orderId);

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                new ResponseObject("OK", "Delete order successfully!", "")
        );
    }

    @GetMapping("/")
    public ResponseEntity<ResponseObject> getOrders(@RequestParam() Map<String, String> params) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                new ResponseObject("OK", "Get orders successfully!", orderService.getOrders(params))
        );
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseObject> getListOrder() {
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                new ResponseObject("OK", "Get orders successfully!", orderService.getListOrder())
        );
    }
}
