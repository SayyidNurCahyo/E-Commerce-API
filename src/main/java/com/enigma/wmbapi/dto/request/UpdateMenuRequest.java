package com.enigma.wmbapi.dto.request;

import com.enigma.wmbapi.entity.Image;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMenuRequest {
    @NotBlank(message = "Id is Required")
    private String id;
    @NotBlank(message = "Name is Required")
    private String name;
    @NotNull(message = "Price is Required")
    @Min(value = 0, message = "Price Must Be Greater Than Or Equal 0")
    private Long price;
    private List<MultipartFile> images;
}
