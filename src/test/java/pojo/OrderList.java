package pojo;

import java.util.List;

public class OrderList {
    private List<Order> order;
    private PageInfo pageInfo;
    private List<AvailableStations> availableStations;

    public OrderList(List<Order> order, PageInfo pageInfo, List<AvailableStations> availableStations) {
        this.order = order;
        this.pageInfo = pageInfo;
        this.availableStations = availableStations;
    }
    public OrderList(){}

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AvailableStations> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(List<AvailableStations> availableStations) {
        this.availableStations = availableStations;
    }
}
