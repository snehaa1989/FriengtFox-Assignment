
package com.dispatch.dispatch_load_balancer.dto;

import java.util.List;

public record DispatchPlanResponse(
        List<VehicleDispatchPlan> dispatchPlan
) {}
