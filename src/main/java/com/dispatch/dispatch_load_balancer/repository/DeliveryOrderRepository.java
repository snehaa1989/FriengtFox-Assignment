
package com.dispatch.dispatch_load_balancer.repository;

import com.dispatch.dispatch_load_balancer.entity.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryOrderRepository
        extends JpaRepository<DeliveryOrder, String> {
}
