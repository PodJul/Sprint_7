package pojo;

import io.qameta.allure.Step;

public class CourierId {
    private int id;
    public CourierId(int id){
        this.id=id;
    }
    public CourierId(){}
    @Step("Get courier Id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
