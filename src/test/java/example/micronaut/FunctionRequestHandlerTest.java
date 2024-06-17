package example.micronaut;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import example.micronaut.domain.FuncRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class FunctionRequestHandlerTest {

    @Inject
    private FunctionRequestHandler handler;
    @Inject
    private ObjectMapper objectMapper;

    APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
    FuncRequest rec;

    @BeforeEach
    void beforeEach() throws JsonProcessingException {
        rec = new FuncRequest();

        rec.setAction("get");
        request.setBody(objectMapper.writeValueAsString(rec));
    }

    @Test
    public void test() {
        handler.execute(request);
    }

    @Test
    void test2() {
        //        SecretKey key1 = Keys.hmacShaKeyFor(keyStr.getBytes());
        SecretKey key1 = Jwts.SIG.HS256.key().build();
        SecretKey key2 = Jwts.SIG.HS256.key().build();

        String jws = Jwts.builder()
                .signWith(key1)
                .issuer("me")
                .subject("Bob")
                .audience().add("you").and()
                .expiration(addDate()) //a java.util.Date
                .notBefore(minusDate()) //a java.util.Date
                .issuedAt(new Date()) // for example, now
                .id(UUID.randomUUID().toString()).compact();
        System.out.println(jws);

        // SignatureException：署名検証に失敗した場合
        // ExpiredJwtException：exp期限切れ
        // PrematureJwtException：nbf未来日
        Jws<Claims> claims = Jwts.parser().verifyWith(key1).build().parseSignedClaims(jws);

        System.out.println(claims);
    }

    private Date addDate() {
        Date now = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        cal.add(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
    }

    private Date minusDate() {
        Date now = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        cal.add(Calendar.DAY_OF_MONTH, -1);

        return cal.getTime();
    }
}
