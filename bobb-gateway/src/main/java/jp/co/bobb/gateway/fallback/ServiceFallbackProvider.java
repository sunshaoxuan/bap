package jp.co.bobb.gateway.fallback;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.bobb.gateway.model.ErrorCode;
import jp.co.bobb.gateway.model.Msg;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ServiceFallbackProvider implements FallbackProvider {
    @Override
    public ClientHttpResponse fallbackResponse(Throwable cause) {
        cause.printStackTrace();
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() {
                return getStatusCode().value();
            }

            @Override
            public String getStatusText() {
                return getStatusCode().getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                //响应体
                Msg msg = new Msg();
                msg.setCode(ErrorCode.MICRO_SERVICE_UNAVAILABLE);
                msg.setMsg("service unavailable,please try connecting later");
                ObjectMapper objectMapper = new ObjectMapper();
                String content = objectMapper.writeValueAsString(msg);
                return new ByteArrayInputStream(content.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return httpHeaders;
            }
        };
    }

    @Override
    public String getRoute() {
        //表明是为哪个微服务提供回退，"*"全部
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return null;
    }
}
