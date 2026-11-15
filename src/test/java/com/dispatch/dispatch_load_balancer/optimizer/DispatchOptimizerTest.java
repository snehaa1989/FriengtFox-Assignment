
package com.dispatch.dispatch_load_balancer.optimizer;

import com.dispatch.dispatch_load_balancer.entity.DeliveryOrder;
import com.dispatch.dispatch_load_balancer.entity.Vehicle;
import com.dispatch.dispatch_load_balancer.enums.Priority;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DispatchOptimizerTest {

    private DispatchOptimizer optimizer;

    @BeforeEach
    void setUp() {
        optimizer = new DispatchOptimizer();
    }

    @Test
    void shouldAssignHighPriorityOrdersFirst() {

        Vehicle vehicle = new Vehicle(
                "V001", 100, 28.6139, 77.2090, "Delhi"
        );

        DeliveryOrder low = new DeliveryOrder(
                "O003", 28.60, 77.20,
                "Address 3", 20, Priority.LOW
        );

        DeliveryOrder high = new DeliveryOrder(
                "O001", 28.61, 77.21,
                "Address 1", 20, Priority.HIGH
        );

        DeliveryOrder medium = new DeliveryOrder(
                "O002", 28.62, 77.22,
                "Address 2", 20, Priority.MEDIUM
        );

        var result = optimizer.optimize(
                List.of(low, high, medium),
                List.of(vehicle)
        );

        var assigned = result.get(0).assignedOrders();

        assertEquals("O001", assigned.get(0).orderId());
        assertEquals("O002", assigned.get(1).orderId());
        assertEquals("O003", assigned.get(2).orderId());
    }

    @Test
    void shouldNeverExceedVehicleCapacity() {

        Vehicle vehicle = new Vehicle(
                "V001", 50, 28.6139, 77.2090, "Delhi"
        );

        DeliveryOrder order1 = new DeliveryOrder(
                "O001", 28.61, 77.21,
                "Address 1", 30, Priority.HIGH
        );

        DeliveryOrder order2 = new DeliveryOrder(
                "O002", 28.62, 77.22,
                "Address 2", 30, Priority.LOW
        );

        assertThrows(
                IllegalStateException.class,
                () -> optimizer.optimize(
                        List.of(order1, order2),
                        List.of(vehicle)
                )
        );
    }

    @Test
    void shouldChooseNearestEligibleVehicle() {

        Vehicle vehicle1 = new Vehicle(
                "V001", 10, 28.6139, 77.2090, "Delhi"
        );

        Vehicle vehicle2 = new Vehicle(
                "V002", 100, 28.6139, 77.2090, "Delhi"
        );

        DeliveryOrder order = new DeliveryOrder(
                "O001", 28.6140, 77.2090,
                "Nearby address", 20, Priority.HIGH
        );

        var result = optimizer.optimize(
                List.of(order),
                List.of(vehicle1, vehicle2)
        );

        assertTrue(result.get(0).assignedOrders().isEmpty());
        assertEquals(
                "O001",
                result.get(1).assignedOrders().get(0).orderId()
        );
    }

    @Test
    void shouldReturnZeroLoadForVehiclesWithNoOrders() {

        Vehicle vehicle = new Vehicle(
                "V001", 100, 28.6139, 77.2090, "Delhi"
        );

        var result = optimizer.optimize(
                List.of(),
                List.of(vehicle)
        );

        assertEquals(1, result.size());
        assertEquals(0.0, result.get(0).totalLoad());
        assertEquals("0.00 km", result.get(0).totalDistance());
        assertTrue(result.get(0).assignedOrders().isEmpty());
    }

    @Test
    void shouldRejectOrdersWhenNoVehiclesAreAvailable() {

        DeliveryOrder order = new DeliveryOrder(
                "O001", 28.61, 77.21,
                "Address", 10, Priority.HIGH
        );

        assertThrows(
                IllegalStateException.class,
                () -> optimizer.optimize(
                        List.of(order),
                        List.of()
                )
        );
    }
}
