package uk.ac.mmu.foodorderingapp.Model;

public class Rating {

    private String userPhone;
    private String FoodId;
    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String userPhone, String foodId, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.FoodId = foodId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        this.FoodId = foodId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
