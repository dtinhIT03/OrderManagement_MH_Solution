package com.example.demo.config;

import com.example.demo.data_jooq.request.ValidToken;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.singerKey}")
    private String singerKey;

    private NimbusJwtDecoder nimbusJwtDecoder;

    @Autowired
    private AuthenticationComponent authenticationComponent;

    @Override
    public Jwt decode(String token) throws JwtException{
        try{
            var response = authenticationComponent.introspect(ValidToken.builder().token(token).build());
            if(!response.isValid()) throw  new JwtException("Token invalid");
        }
        catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

            if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(singerKey.getBytes(),"HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512).build();
        }
            return nimbusJwtDecoder.decode(token);
    }


}
