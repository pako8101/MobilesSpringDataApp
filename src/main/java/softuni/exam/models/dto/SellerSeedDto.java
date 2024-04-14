package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class SellerSeedDto implements Serializable {
    @Expose
    @Size(min = 2,max = 30)
    @NotNull
    private String firstName;
    @Expose
    @Size(min = 2,max = 30)
    @NotNull
    private String lastName;
    @Expose
    @Size(min = 3,max = 6)
    @NotNull
    private String personalNumber;

    public SellerSeedDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }
}
