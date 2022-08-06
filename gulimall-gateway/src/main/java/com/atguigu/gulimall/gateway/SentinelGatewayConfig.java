package com.atguigu.gulimall.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.fastjson.JSON;
import com.bohui.common.exception.BizCodeEnume;
import com.bohui.common.utils.R;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class SentinelGatewayConfig {
//TODO响应式编程
    public  SentinelGatewayConfig(){
        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                R error = R.error(BizCodeEnume.TOO_MANY_REQUEST.getCode(), BizCodeEnume.TOO_MANY_REQUEST.getMsg());
                String errJson = JSON.toJSONString(error);
                Mono<ServerResponse> body = ServerResponse.ok().body(Mono.just(errJson), String.class);
                return body;
            }
        });
    }
}
