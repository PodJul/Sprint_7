package pojo;

import io.qameta.allure.Step;

public class OrderId {
    private Order order;
    @Step("Get order Id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
