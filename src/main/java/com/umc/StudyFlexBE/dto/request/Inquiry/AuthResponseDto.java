package com.umc.StudyFlexBE.dto.request.Inquiry;


import com.umc.StudyFlexBE.dto.request.NaverDto;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Data
@NoArgsConstructor

public class AuthResponseDto {
    private NaverDto naverUser;
    private String token;

}
