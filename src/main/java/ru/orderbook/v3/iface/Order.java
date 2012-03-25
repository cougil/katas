package ru.orderbook.v3.iface;

/**
 * A data transfer object used by {@link ru.orderbook.v3.iface.AppEnvironment}.
 */
public class Order {
    private final long orderId;
    private final String symbol;
    private final boolean isBuy;
    private final int price;
    private final int quantity;

    public Order(long orderId, String symbol, boolean isBuy, int price, int quantity) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.isBuy = isBuy;
        this.price = price;
        this.quantity = quantity;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
