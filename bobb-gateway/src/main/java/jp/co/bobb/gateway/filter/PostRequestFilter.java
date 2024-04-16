package jp.co.bobb.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import jp.co.bobb.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Parker Sun
 */
//@Component
@Slf4j
public class PostRequestFilter extends ZuulFilter {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        //int值来定义过滤器的执行顺序，数值越小优先级越高
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            if (ctx.get("requestURI").toString().contains("file")) {
                return null;
            }
            String response = ctx.getResponseBody();
            if (null == response) {
                InputStream inputStream = ctx.getResponseDataStream();
                if (null != inputStream) {
                    response = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
                }
                inputStream.close();
            }
            log.info("response:{}", response);
            try {
                mapper.readTree(response);
                JSONObject responseJson = JSONObject.parseObject(response);
                if (null != responseJson && responseJson.containsKey("exception")
                        && responseJson.getString("exception").contains("MethodArgumentNotValidException")) {
                    response = JSONObject.toJSONString(Result.failure(HttpStatus.SC_INTERNAL_SERVER_ERROR,
                            responseJson.getJSONArray("errors").getJSONObject(0).getString("defaultMessage")));
                    log.info("response:{}", response);
                }
            } catch (Exception e) {
            }
            ctx.setResponseBody(response);
        } catch (Exception e) {
            log.error("exception", e);
        }
        return null;
    }

}
