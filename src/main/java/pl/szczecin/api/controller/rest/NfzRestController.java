package pl.szczecin.api.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.szczecin.api.dto.NfzProviderDTO;
import pl.szczecin.api.dto.mapper.NfzProviderMapper;
import pl.szczecin.business.NfzService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(NfzRestController.API_NFZ)
public class NfzRestController {


    public static final String API_NFZ = "/api/nfz";

    private final NfzService nfzService;
    private final NfzProviderMapper nfzProviderMapper;


    @Operation(
            summary = "Get NFZ provider list",
            description = "This endpoint returns a list of available providers " +
                    "for branch 16 - Zachodniopomorski from external API - NFZ.",
            tags = {"External API - NFZ"} // TAG do grupowania endpointów
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "NFZ providers found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = NfzProviderDTO.class
                                    )
                            )
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameter supplied",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "NFZ provider not found",
                    content = @Content)
    })
    @GetMapping()
    public List<NfzProviderDTO> nfzGetHealthBenefits(
            @Parameter(
                    description = "Enter a year. Please use a correct value according to the example",
                    example = "2022"
            )
            @RequestParam(value = "year") final Integer year
    ) {
        return nfzService.getNfzProviders(year).stream()
                .map(nfzProviderMapper::map)
                .toList();

    }

}
