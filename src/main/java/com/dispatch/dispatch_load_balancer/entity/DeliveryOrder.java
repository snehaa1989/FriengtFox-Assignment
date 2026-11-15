package com.dispatch.dispatch_load_balancer.entity;

import com.dispatch.dispatch_load_balancer.enums.Priority;
import jakarta.persistence.*;

@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder {

    @Id
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String address;

    @Column(name = "package_weight", nullable = false)
    private double packageWeight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    public DeliveryOrder() {
    }

    public DeliveryOrder(String orderId, double latitude, double longitude,
                         String address, double packageWeight, Priority priority) {
        this.orderId = orderId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.packageWeight = packageWeight;
        this.priority = priority;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(double packageWeight) {
        this.packageWeight = packageWeight;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
