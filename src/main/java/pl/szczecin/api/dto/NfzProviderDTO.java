package pl.szczecin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NfzProviderDTO {

    private Integer year;
    private String branch;
    private String code;
    private String name;
    private String nip;
    private String regon;
    private String registryNumber;
    private String postCode;
    private String street;
    private String place;
    private String phone;
    private String commune;
}
