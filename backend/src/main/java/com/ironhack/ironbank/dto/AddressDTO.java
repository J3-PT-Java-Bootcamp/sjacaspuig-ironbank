package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressDTO {

    private String street;
    private String number;
    private String extraInformation;
    private String postalCode;
    private String city;
    private String country;
    private String province;

    public static AddressDTO fromEntity(Address address) {
        var addressDTO = new AddressDTO();
        addressDTO.setStreet(address.getStreet());
        addressDTO.setNumber(address.getNumber());
        addressDTO.setExtraInformation(address.getExtraInformation());
        addressDTO.setPostalCode(address.getPostalCode());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setProvince(address.getProvince());
        return addressDTO;
    }

    public static List<AddressDTO> fromEntities(List<Address> addresses) {
        return addresses.stream().map(AddressDTO::fromEntity).toList();
    }
}
