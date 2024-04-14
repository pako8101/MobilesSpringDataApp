package softuni.exam.models.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
public class Sale extends BaseEntity{
@Column(name = "discounted")
    private boolean discounted;
@Column(name = "number",nullable = false,unique = true)
private String number;
@Column(name = "sale_date",nullable = false)
private LocalDateTime saleDate;
@ManyToOne
//@JoinColumn(name = "seller_of_salle",referencedColumnName = "id")
private Seller seller;

    public Sale() {
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

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
