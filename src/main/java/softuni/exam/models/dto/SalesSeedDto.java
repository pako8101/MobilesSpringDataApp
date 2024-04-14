package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SalesSeedDto implements Serializable {
    @Expose
    private boolean discounted;
    @Expose
    @Value("7")
    @NotNull
    private String number;
    @Expose
    @NotNull
    private String saleDate;
    @Expose
    private Long seller;

    public SalesSeedDto() {
    }

    public boolean isDiscounted() {
        return discounted;
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public Long getSeller() {
        return seller;
    }

    public void setSeller(Long seller) {
        this.seller = seller;
    }
}
