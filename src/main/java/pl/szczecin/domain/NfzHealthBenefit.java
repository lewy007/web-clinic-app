package pl.szczecin.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NfzHealthBenefit {

    String code;
    String name;
    Integer year;

}
