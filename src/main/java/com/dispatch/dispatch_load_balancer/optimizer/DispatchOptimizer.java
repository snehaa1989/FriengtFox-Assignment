
package com.dispatch.dispatch_load_balancer.optimizer;

import com.dispatch.dispatch_load_balancer.dto.OrderRequest;
import com.dispatch.dispatch_load_balancer.dto.VehicleDispatchPlan;
import com.dispatch.dispatch_load_balancer.entity.DeliveryOrder;
import com.dispatch.dispatch_load_balancer.entity.Vehicle;
import com.dispatch.dispatch_load_balancer.util.HaversineDistanceCalculator;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DispatchOptimizer {

    private static class VehicleState {
        private final Vehicle vehicle;
        private double remainingCapacity;
        private double totalLoad;
        private double totalDistance;
        private double currentLatitude;
        private double currentLongitude;

        private final List<OrderRequest> assignedOrders = new ArrayList<>();

        VehicleState(Vehicle vehicle) {
            this.vehicle = vehicle;
            this.remainingCapacity = vehicle.getCapacity();
            this.currentLatitude = vehicle.getCurrentLatitude();
            this.currentLongitude = vehicle.getCurrentLongitude();
        }
    }

    public List<VehicleDispatchPlan> optimize(
            List<DeliveryOrder> orders,
            List<Vehicle> vehicles) {

        if (vehicles.isEmpty() && !orders.isEmpty()) {
            throw new IllegalStateException(
                    "No vehicles are available for dispatch."
            );
        }

        List<DeliveryOrder> sortedOrders = new ArrayList<>(orders);

        sortedOrders.sort(
                Comparator.<DeliveryOrder>comparingInt(
                        order -> priorityRank(order.getPriority())
                ).thenComparing(DeliveryOrder::getOrderId)
        );

        List<VehicleState> states = vehicles.stream()
                .map(VehicleState::new)
                .sorted(Comparator.comparing(
                        state -> state.vehicle.getVehicleId()
                ))
                .toList();

        for (DeliveryOrder order : sortedOrders) {

            VehicleState nearestVehicle = null;
            double minimumDistance = Double.MAX_VALUE;

            for (VehicleState state : states) {

                if (state.remainingCapacity < order.getPackageWeight()) {
                    continue;
                }

                double distance =
                        HaversineDistanceCalculator.calculateDistance(
                                state.currentLatitude,
                                state.currentLongitude,
                                order.getLatitude(),
                                order.getLongitude()
                        );

                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    nearestVehicle = state;
                }
            }

            if (nearestVehicle == null) {
                throw new IllegalStateException(
                        "Unable to assign order: " + order.getOrderId()
                        + ". No vehicle has sufficient remaining capacity."
                );
            }

            nearestVehicle.assignedOrders.add(
                    new OrderRequest(
                            order.getOrderId(),
                            order.getLatitude(),
                            order.getLongitude(),
                            order.getAddress(),
                            order.getPackageWeight(),
                            order.getPriority()
                    )
            );

            nearestVehicle.remainingCapacity -= order.getPackageWeight();
            nearestVehicle.totalLoad += order.getPackageWeight();
            nearestVehicle.totalDistance += minimumDistance;

            nearestVehicle.currentLatitude = order.getLatitude();
            nearestVehicle.currentLongitude = order.getLongitude();
        }

        return states.stream()
                .map(state -> new VehicleDispatchPlan(
                        state.vehicle.getVehicleId(),
                        state.totalLoad,
                        String.format(
                                Locale.US,
                                "%.2f km",
                                state.totalDistance
                        ),
                        List.copyOf(state.assignedOrders)
                ))
                .toList();
    }

    private int priorityRank(
            com.dispatch.dispatch_load_balancer.enums.Priority priority) {

        return switch (priority) {
            case HIGH -> 1;
            case MEDIUM -> 2;
            case LOW -> 3;
        };
    }
}
