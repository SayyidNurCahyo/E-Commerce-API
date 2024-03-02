package com.enigma.wmbapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTableRequest {
    @NotBlank(message = "Name is Required")
    private String name;
}
