
package com.dispatch.dispatch_load_balancer.service;

import com.dispatch.dispatch_load_balancer.dto.*;
import com.dispatch.dispatch_load_balancer.entity.DeliveryOrder;
import com.dispatch.dispatch_load_balancer.entity.Vehicle;
import com.dispatch.dispatch_load_balancer.optimizer.DispatchOptimizer;
import com.dispatch.dispatch_load_balancer.repository.DeliveryOrderRepository;
import com.dispatch.dispatch_load_balancer.repository.VehicleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DispatchService {

    private final DeliveryOrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final DispatchOptimizer dispatchOptimizer;

    public DispatchService(
            DeliveryOrderRepository orderRepository,
            VehicleRepository vehicleRepository,
            DispatchOptimizer dispatchOptimizer) {

        this.orderRepository = orderRepository;
        this.vehicleRepository = vehicleRepository;
        this.dispatchOptimizer = dispatchOptimizer;
    }

    @Transactional
    public ApiResponse saveOrders(OrdersRequest request) {

        // Validate duplicate order IDs within the incoming request
        Set<String> orderIds = new HashSet<>();

        for (OrderRequest order : request.orders()) {
            if (!orderIds.add(order.orderId())) {
                throw new IllegalArgumentException(
                        "Duplicate order ID: " + order.orderId()
                );
            }
        }

        // Convert DTOs to entities
        List<DeliveryOrder> orders = request.orders()
                .stream()
                .map(order -> new DeliveryOrder(
                        order.orderId(),
                        order.latitude(),
                        order.longitude(),
                        order.address(),
                        order.packageWeight(),
                        order.priority()
                ))
                .toList();

        // Save or update orders
        orderRepository.saveAll(orders);

        return new ApiResponse(
                "Delivery orders accepted.",
                "success"
        );
    }

    @Transactional
    public ApiResponse saveVehicles(VehiclesRequest request) {

        // Validate duplicate vehicle IDs within the incoming request
        Set<String> vehicleIds = new HashSet<>();

        for (VehicleRequest vehicle : request.vehicles()) {
            if (!vehicleIds.add(vehicle.vehicleId())) {
                throw new IllegalArgumentException(
                        "Duplicate vehicle ID: " + vehicle.vehicleId()
                );
            }
        }

        // Convert DTOs to entities
        List<Vehicle> vehicles = request.vehicles()
                .stream()
                .map(vehicle -> new Vehicle(
                        vehicle.vehicleId(),
                        vehicle.capacity(),
                        vehicle.currentLatitude(),
                        vehicle.currentLongitude(),
                        vehicle.currentAddress()
                ))
                .toList();

        // Save or update vehicles
        vehicleRepository.saveAll(vehicles);

        return new ApiResponse(
                "Vehicle details accepted.",
                "success"
        );
    }

    @Transactional(readOnly = true)
    public DispatchPlanResponse generateDispatchPlan() {

        // Fetch orders and vehicles from the database
        List<DeliveryOrder> orders = orderRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findAll();

        // Generate optimized dispatch plan
        List<VehicleDispatchPlan> plan =
                dispatchOptimizer.optimize(orders, vehicles);

        return new DispatchPlanResponse(plan);
    }
}
