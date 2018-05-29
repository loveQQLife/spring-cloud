package com.kreken.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class CoreHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CoreHttpRequestInterceptor.class);

    /**
     * Intercept client http response.
     *
     * @param request   the request
     * @param body      the body
     * @param execution the execution
     * @return the client http response
     * @throws IOException the io exception
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);

        String header = StringUtils.collectionToDelimitedString(
                CoreHeaderInterceptor.LABEL.get(),
                CoreHeaderInterceptor.HEADER_LABEL_SPLIT);
        logger.info("header={} ", header);
        requestWrapper.getHeaders().add(CoreHeaderInterceptor.HEADER_LABEL, header);

        return execution.execute(requestWrapper, body);
    }
}
