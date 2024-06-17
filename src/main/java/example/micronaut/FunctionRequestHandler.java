package example.micronaut;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;

public class FunctionRequestHandler
        extends MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent requestEvent) {

        try {
            return run(requestEvent);
        } catch (NoSuchAlgorithmException | IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

        return null;
    }

    private APIGatewayProxyResponseEvent run(APIGatewayProxyRequestEvent requestEvent)
            throws NoSuchAlgorithmException, IOException {

        // ★テストクラスに使用パターンを記載
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

        SecretKey key = Jwts.SIG.HS256.key().build();

        String jws = Jwts.builder().subject("Joe").signWith(key).compact();

        System.out.println(jws);

        return responseEvent;
    }

}
