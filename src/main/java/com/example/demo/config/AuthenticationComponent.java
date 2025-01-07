package com.example.demo.config;

import com.example.demo.data_jooq.request.ValidToken;
import com.example.demo.data_jooq.response.ValidTokenResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
public class AuthenticationComponent {
    @Value("${jwt.singerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    private long REFRESH_DURATION;

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if(!(verified && expiryTime.after(new Date()))){
            throw new RuntimeException("Unauthenticated");
        }
        return signedJWT;
    }
    public ValidTokenResponse introspect(ValidToken request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (RuntimeException e) {
            isValid = false;
        }

        return ValidTokenResponse.builder().valid(isValid).build();
    }

//    public AuthenticationResponse authenticate(AuthenticationRequest request){
//
//    }
}
