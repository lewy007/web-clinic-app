package pl.szczecin.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NfzProvider {

    Integer year;
    String branch;
    String code;
    String name;
    String nip;
    String regon;
    String registryNumber;
    String postCode;
    String street;
    String place;
    String phone;
    String commune;

}
