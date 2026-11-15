
package com.dispatch.dispatch_load_balancer.controller;

import com.dispatch.dispatch_load_balancer.dto.*;
import com.dispatch.dispatch_load_balancer.service.DispatchService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {

    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    // API 1: Accept delivery orders
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse> saveOrders(
            @Valid @RequestBody OrdersRequest request) {

        return ResponseEntity.ok(
                dispatchService.saveOrders(request)
        );
    }

    // API 2: Accept vehicle details
    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse> saveVehicles(
            @Valid @RequestBody VehiclesRequest request) {

        return ResponseEntity.ok(
                dispatchService.saveVehicles(request)
        );
    }

    // API 3: Generate optimized dispatch plan
    @GetMapping("/plan")
    public ResponseEntity<DispatchPlanResponse> generateDispatchPlan() {

        return ResponseEntity.ok(
                dispatchService.generateDispatchPlan()
        );
    }
}
