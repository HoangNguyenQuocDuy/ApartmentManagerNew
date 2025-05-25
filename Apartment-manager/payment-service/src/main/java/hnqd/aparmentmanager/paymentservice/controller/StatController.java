//package hnqd.aparmentmanager.paymentservice.controller;
//
//import hnqd.aparmentmanager.common.dto.response.ResponseObject;
//import hnqd.aparmentmanager.paymentservice.service.IStatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/stats")
//@RequiredArgsConstructor
//public class StatController {
//
//    private final IStatService statService;
//
//    @GetMapping("/revenue")
//    public ResponseEntity<ResponseObject> getStatsRevenueByMonth(@RequestParam Map<String, String> params) {
//        int month = Integer.parseInt(params.getOrDefault("month", String.valueOf(0)));
//
//        if (month != 0) {
//            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
//                    new ResponseObject("OK", "Get stat revenue by month successfully!",
//                            statService.getRevenueByMonth(Integer.parseInt(params.get("month")), Integer.parseInt(params.get("year"))))
//            );
//
//        }
//        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
//                new ResponseObject("OK", "Get stat revenue by year successfully!",
//                        statService.getTotalRevenueByYear(Integer.parseInt(params.get("year"))))
//        );
//    }
//
//}
