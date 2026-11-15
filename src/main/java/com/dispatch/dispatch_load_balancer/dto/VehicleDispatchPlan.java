
package com.dispatch.dispatch_load_balancer.dto;

import java.util.List;

public record VehicleDispatchPlan(
        String vehicleId,
        double totalLoad,
        String totalDistance,
        List<OrderRequest> assignedOrders
) {}
