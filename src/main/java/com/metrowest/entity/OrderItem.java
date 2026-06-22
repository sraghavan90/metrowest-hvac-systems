package com.metrowest.entity;

public class OrderItem
{
    private long id;
    private int qty;

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString()
    {
        return "OrderItem[id=" + id + ", qty=" + qty + "]";
    }
}
