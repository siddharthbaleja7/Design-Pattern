package com.example.orders;

import java.util.ArrayList;
import java.util.List;

/**
 * Telescoping constructors + setters. Allows invalid states.
 */
public final class Order {
    private final String id;
    private final String customerEmail;
    private final  List<OrderLine> lines;
    private final Integer discountPercent; // 0..100 expected, but not enforced
    private final boolean expedited;
    private final String notes;

    private Order(Builder b){
        this.id = b.id;
        this.customerEmail = b.customerEmail;
        this.lines = b.lines;
        this.discountPercent = b.discountPercent;
        this.expedited = b.expedited;
        this.notes = b.notes;
    }

    public String getId() { return id; }
    public String getCustomerEmail() { return customerEmail; }
    public List<OrderLine> getLines() { return lines; } // leaks internal list
    public Integer getDiscountPercent() { return discountPercent; }
    public boolean isExpedited() { return expedited; }
    public String getNotes() { return notes; }

    public int totalBeforeDiscount() {
        int sum = 0;
        for (OrderLine l : lines) sum += l.getQuantity() * l.getUnitPriceCents();
        return sum;
    }

    public int totalAfterDiscount() {
        int base = totalBeforeDiscount();
        if (discountPercent == null) return base;
        return base - (base * discountPercent / 100);
    }
    public static class Builder{
        private final String id;
        private final String customerEmail;
        private final List<OrderLine> lines = new ArrayList<>();
        private Integer discountPercent;
        private boolean expedited;
        private String notes;
        public Builder(String id , String customerEmail , OrderLine line){
            this.id = id;
            this.customerEmail = customerEmail;
            this.lines.add(line);
        }
        public Builder addLine(OrderLine line){
            this.lines.add(line);
            return this;
        }
        public Builder setDiscountPercent(Integer discountPercent){
            this.discountPercent = discountPercent;
            return this;
        }
        public Builder setExpedited(boolean expedited){
            this.expedited = expedited;
            return this;
        }
        public Builder setNotes(String notes){
            this.notes = notes;
            return this;
        }
        public Order build(){
            if (!PricingRules.isValidEmail(customerEmail)) {
                throw new IllegalArgumentException("Invalid email: " + customerEmail);
            }
            if (!PricingRules.isValidDiscount(discountPercent)) {
                throw new IllegalArgumentException("Invalid discount: " + discountPercent);
            }
            return new Order(this);
        }
    }
}
