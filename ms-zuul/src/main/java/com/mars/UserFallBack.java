package com.mars;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Slf4j
@Component
public class UserFallBack implements FallbackProvider {
    @Override
    public String getRoute() {
        return "ms-user";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        log.error("测试 Ribbon Retry 降级, " + cause);
        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        } else {
            return this.fallbackResponse();
        }
    }

    public ClientHttpResponse fallbackResponse() {
        return this.response(HttpStatus.BAD_GATEWAY);
    }

    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream((UserFallBack.this.getRoute() +  " 服务不可用").getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                // headers设定
                HttpHeaders headers = new HttpHeaders();
                MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
                headers.setContentType(mt);
                return headers;
            }
        };
    }
}
