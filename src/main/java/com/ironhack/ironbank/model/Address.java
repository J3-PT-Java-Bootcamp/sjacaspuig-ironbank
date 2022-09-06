package com.ironhack.ironbank.model;

import com.ironhack.ironbank.dto.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotNull
    private String street;

    private String number;

    @Column(name = "extra_information")
    private String extraInformation;

    @NotNull
    @Column(name = "postal_code")
    private String postalCode;

    @NotNull
    private String city;

    @NotNull
    private String country;

    private String province;

    public String toFormattedAddress() {
        return street + ", " + number + ", " + extraInformation + ", " + postalCode + " " + city + ", " + province + " " + country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(postalCode, address.postalCode) &&
                Objects.equals(city, address.city);
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", number=" + number +
                ", extraInformation='" + extraInformation + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                '}';
    }

    public static Address fromDTO(AddressDTO addressDTO) {
        var address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setNumber(addressDTO.getNumber());
        address.setExtraInformation(addressDTO.getExtraInformation());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());

        if (addressDTO.getProvince() != null) {
            address.setProvince(addressDTO.getProvince());
        }

        return address;
    }
}
