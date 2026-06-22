package com.metrowest.entity;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrder
{
    private List<OrderItem> items = new ArrayList<>();
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
