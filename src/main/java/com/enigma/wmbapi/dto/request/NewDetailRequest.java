package com.enigma.wmbapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewDetailRequest {
    @NotBlank(message = "Menu Id is Required")
    private String menuId;
    @NotNull(message = "Quantity is Required")
    @Min(value = 1, message = "Quantity Must Be Greater Than 0")
    private Integer menuQuantity;
}
