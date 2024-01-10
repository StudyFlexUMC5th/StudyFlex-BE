package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.request.NaverDto;
import com.umc.StudyFlexBE.dto.response.APICallException;
import com.umc.StudyFlexBE.dto.response.InvalidAuthorizationCodeException;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.Role;
import com.umc.StudyFlexBE.repository.MemberRepository;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;





@NoArgsConstructor(force = true)
@Service
public class NaverService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;

    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${naver.redirect.url}")
    private String NAVER_REDIRECT_URL;

    private final static String NAVER_AUTH_URI = "https://nid.naver.com";
    private final static String NAVER_API_URI = "https://openapi.naver.com";




    public String getNaverLogin() {
        return NAVER_AUTH_URI + "/oauth2.0/authorize"
                + "?client_id=" + NAVER_CLIENT_ID
                + "&redirect_uri=" + NAVER_REDIRECT_URL
                + "&response_type=code";
    }

    public NaverDto getNaverInfo(String code) throws InvalidAuthorizationCodeException, APICallException {
        if (code == null || code.trim().isEmpty()) {
            throw new InvalidAuthorizationCodeException("Authorization code is missing.");
        }
        String accessToken = retrieveAccessToken(code);
        return getUserInfoWithToken(accessToken);
    }

    private String retrieveAccessToken(String code) throws APICallException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", NAVER_CLIENT_ID);
            params.add("client_secret", NAVER_CLIENT_SECRET);
            params.add("code", code);
            params.add("redirect_uri", NAVER_REDIRECT_URL);

            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    NAVER_AUTH_URI + "/oauth2.0/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            return (String) jsonObj.get("access_token");
        } catch (ParseException | RestClientException e) {
            throw new APICallException("Failed to retrieve access token.", e);
        }
    }

    private NaverDto getUserInfoWithToken(String accessToken) throws APICallException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    NAVER_API_URI + "/v1/nid/me",
                    HttpMethod.POST,
                    httpEntity,
                    String.class);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            JSONObject account = (JSONObject) jsonObj.get("response");

            String id = String.valueOf(account.get("id"));
            String email = String.valueOf(account.get("email"));
            String name = String.valueOf(account.get("name"));

            return NaverDto.builder().id(id).email(email).name(name).build();
        } catch (ParseException e) {
            throw new APICallException("Failed to parse user info response.", e);
        }
    }

    public Member registerOrAuthenticate(NaverDto naverUser) {
        Member existingMember = memberRepository.findByEmail(naverUser.getEmail());

        if (existingMember != null) {
            existingMember.setNewUser(false); // 기존 사용자 표시하기
            return existingMember;
        }

        // 새로운 사용자 생성
        Member newMember = new Member();
        newMember.setEmail(naverUser.getEmail());
        newMember.setName(naverUser.getName());
        newMember.setPassword(passwordEncoder.encode("defaultPassword"));
        newMember.setRole(Role.ROLE_USER);
        newMember.setNewUser(true); // 새로운 사용자 표시하기

        return newMember;
    }
}