package katas.groovy.orderbook.v3.consumer

import java.lang.Integer as Price
import java.lang.String as Symbol

import katas.groovy.orderbook.v3.iface.*
import static java.util.Collections.reverseOrder

/**
 * User: dima
 * Date: 25/03/2012
 */
class OrderConsumerImpl implements OrderConsumer {
  private final Map<Symbol, OrderBook> orderBooks = new HashMap().withDefault { new OrderBook() }
  private log

  @Override void startProcessing(Log log) {
    this.log = log
    log.log(LogLevel.INFO, "Started processing orders")
  }

  @Override void handleEvent(Action action, Order order) {
    def orderBook = findBookFor(order)

    if (action == Action.ADD) orderBook.add(order)
    else if (action == Action.EDIT) orderBook.edit(order)
    else if (action == Action.REMOVE) orderBook.remove(order)
  }

  private OrderBook findBookFor(Order order) {
    if (order.symbol != null) orderBooks[order.symbol]
    else orderBooks.values().find { it.has(order) }
  }

  @Override void finishProcessing() {
    log.log(LogLevel.INFO, toPrintableString(orderBooks.sort{ it.key }))
  }

  String toPrintableString(Map orderBookBySymbol) {
    orderBookBySymbol.inject("") { result, entry ->
      result + "\n\n${entry.key}\n" +
              "bidSide${bookLevelToString(entry.value.bidLevels)}\n" +
              "askSide${bookLevelToString(entry.value.askLevels)}"
    } + "\n"
  }

  String bookLevelToString(Map bookLevel) {
    bookLevel.values().inject("") { result, PriceLevel level ->
      result + "\n\tprice = ${level.price}, size = ${level.size}, count = ${level.count}"
    }
  }
}

class OrderBook {
  private final Map<Long, Order> ordersById = new TreeMap()
  final Map<Price, PriceLevel> bidLevels = new TreeMap(reverseOrder()).withDefault { new PriceLevel((int) it) }
  final Map<Price, PriceLevel> askLevels = new TreeMap().withDefault { new PriceLevel((int) it) }

  void add(Order order) {
    priceLevelFor(order).add(order)
    ordersById[order.orderId] = order
  }

  void edit(Order order) {
    Order oldOrder = ordersById[order.orderId]
    Order newOrder = new Order(oldOrder.orderId, oldOrder.symbol, oldOrder.buy, order.price, order.quantity)
    remove(oldOrder)
    add(newOrder)
  }

  void remove(Order order) {
    order = ordersById.remove(order.orderId)
    PriceLevel priceLevel = priceLevelFor(order)
    priceLevel.remove(order)

    if (priceLevel.empty) removePriceLevel(order)
  }

  boolean has(Order order) {
    ordersById.containsKey(order.orderId)
  }

  private PriceLevel priceLevelFor(Order order) {
    (order.buy ? bidLevels : askLevels)[order.price]
  }

  private removePriceLevel(Order order) {
    (order.buy ? bidLevels : askLevels).remove(order.price)
  }
}

class PriceLevel {
  int price
  int count
  int size

  PriceLevel(int price) {
    this.price = price
  }

  def add(Order order) {
    count += 1
    size += order.quantity
  }

  def remove(Order order) {
    count -= 1
    size -= order.quantity
  }

  boolean isEmpty() {
    count == 0
  }

  @Override String toString() {
    "PriceLevel{price=${price}, count=${count}, size=${size}}"
  }
}