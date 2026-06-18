package com.metrowest.entity;

import java.util.HashMap;
import java.util.Map;

public class OrderItems
{
    private Map<String, Integer> quantities = new HashMap<>();

    public Map<String, Integer> getQuantities() { return quantities; }

    public void setQuantities(Map<String, Integer> quantities) { this.quantities = quantities; }
}
