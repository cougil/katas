package ru.orderbook.v3.app;


import ru.orderbook.v3.iface.*;

import java.util.LinkedHashSet;
import java.util.Set;


public abstract class AbstractAppEnvironment implements AppEnvironment {
    private final Set<OrderConsumer> consumers = new LinkedHashSet<OrderConsumer>();
    private final LogLevel logLevel;
    /**
     * Implementation of {@link katas.java.cmc.orderbook.v1.iface.Log} which uses the standard out.
     */
    protected final Log log = new Log() {
        @Override
        public void log(LogLevel logLevel, String msg) {
            if (isEnabled(logLevel)) {
                System.out.println(logLevel + ": " + msg);
            }
        }

        private boolean isEnabled(LogLevel logLevel) {
            return logLevel.compareTo(AbstractAppEnvironment.this.logLevel) >= 0;
        }
    };

    public AbstractAppEnvironment(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public void registerHandler(OrderConsumer handler) {
        consumers.add(handler);
    }

    @Override
    public final void run() {
        notifyStart();
        try {
            feedOrders();
        } catch (Exception e) {
            log.log(LogLevel.ERROR, e.getMessage());
        } finally {
            notifyFinish();
        }
    }

    /**
     * Sends a stream of orders to {@link katas.java.cmc.orderbook.v1.iface.OrderConsumer}s.
     *
     * @throws Exception if there is an error.
     * @see #notifyOrder(Action, Order)
     */
    protected abstract void feedOrders() throws Exception;

    /**
     * Invokes {@link katas.java.cmc.orderbook.v1.iface.OrderConsumer#handleEvent(katas.java.cmc.orderbook.v1.iface.Action, katas.java.cmc.orderbook.v1.iface.Order)} for every registered consumer with
     * specified <code>action</code> and <code>order</code>.
     */
    protected void notifyOrder(Action action, Order order) {
        for (OrderConsumer consumer : consumers) {
            consumer.handleEvent(action, order);
        }
    }

    private void notifyStart() {
        for (OrderConsumer consumer : consumers) {
            consumer.startProcessing(log);
        }
    }

    private void notifyFinish() {
        for (OrderConsumer consumer : consumers) {
            consumer.finishProcessing();
        }
    }
}