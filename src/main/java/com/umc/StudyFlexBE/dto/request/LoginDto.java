package com.umc.StudyFlexBE.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@NoArgsConstructor
public class LoginDto {


    @JsonProperty
    @NotNull
    private String email;


    @JsonProperty
    @NotNull
    private String password;


}
