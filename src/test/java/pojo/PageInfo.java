package pojo;

public class PageInfo {
    private String page;
    private String total;
    private String limit;

    public PageInfo(String page, String total, String limit) {
        this.page = page;
        this.total = total;
        this.limit = limit;
    }
    public PageInfo(){}
    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
