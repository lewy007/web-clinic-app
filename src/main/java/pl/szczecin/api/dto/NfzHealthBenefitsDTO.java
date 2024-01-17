package pl.szczecin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.szczecin.domain.NfzHealthBenefit;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NfzHealthBenefitsDTO {

   private List<NfzHealthBenefitDTO> nfzHealthBenefitsDTO;
}
