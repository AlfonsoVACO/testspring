package com.bancoazteca.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpAsyncRequestControl;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MainController {

	@Bean
	@Order(-1)
	public GlobalFilter a() {
	    return (exchange, chain) -> {
	    	ServerHttpRequest request = exchange.getRequest().mutate().path("/rest/user/edit").method(HttpMethod.PUT).build();
	        return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(() -> {}));
	    };
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

		return builder.routes().route("monolito", p -> p.path("/**").filters(f -> f.filter((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest().mutate().path("/rest/user").build();
			return chain.filter(exchange.mutate().request(request).build());
		}).modifyRequestBody(GatewayFilterChain .class, ServerWebExchange.class, (exchange, chain) -> {
			
			ServerHttpRequest request = exchange.getRequest().mutate().path("/rest/user").build();
			return chain.filter(exchange.mutate().request(request).build());
		})).uri("http://localhost:9090")).build();
	}
	
	/*private String getRequestBody(ServerHttpRequest request) {
	    Flux<DataBuffer> body = request.getBody();
	    StringBuilder sb = new StringBuilder();        

	    body.subscribe(buffer -> {
	        byte[] bytes = new byte[buffer.readableByteCount()];
	        buffer.read(bytes);
	        DataBufferUtils.release(buffer);
	        String bodyString = new String(bytes, StandardCharsets.UTF_8);
	        sb.append(bodyString);
	    });
	    String str = sb.toString();
	    if (str != null) {
	    	
	    	try {
	    		JSONObject obj = new JSONObject(str);
	    		obj.put("BSN_FLOWNUM", getBsnFlowNum("M1"));
	    		str = obj.toString();
	    	}catch(Exception e) {
	    		log.error(e.getMessage(), e);
	    	}       	
	    }        
	    return str;
	} */

	@RequestMapping("/defaultfortune")
	public String defaultFortune() {
		return "When you feel sad: start dot spring dot io.";
	}

	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}

	/*
	 * @Bean public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
	 * return builder.routes().route("fortune_api", p ->
	 * p.path("/v2/fortune").and().host("api.monolith.com") .filters(f ->
	 * f.setPath("/fortune").requestRateLimiter()
	 * .rateLimiter(RedisRateLimiter.class, c ->
	 * c.setBurstCapacity(1).setReplenishRate(1)) .configure(c -> c.setKeyResolver(
	 * exchange ->
	 * Mono.just(exchange.getRequest().getHeaders().getFirst("X-Fortune-Key")))))
	 * .uri("lb://fortune")) .route("fortune_rewrite", p ->
	 * p.path("/service/randomfortune") .filters(f ->
	 * f.setPath("/fortune").hystrix(c ->
	 * c.setFallbackUri("forward:/defaultfortune"))) .uri("lb://fortune"))
	 * .route("hello_rewrite", p -> p.path("/service/hello/**").filters(f ->
	 * f.filter((exchange, chain) -> { String name =
	 * exchange.getRequest().getQueryParams().getFirst("name"); String path =
	 * "/hello/" + name; ServerHttpRequest request =
	 * exchange.getRequest().mutate().path(path).build(); return
	 * chain.filter(exchange.mutate().request(request).build());
	 * })).uri("lb://hello")) .route("index", p -> p.path("/").filters(f ->
	 * f.setPath("/index.html")).uri("lb://ui")) .route("ui", p ->
	 * p.path("/").or().path("/css/**").or().path("/js/**").uri("lb://ui"))
	 * .route("monolith", p -> p.path("/**").uri("http://localhost:8081")).build();
	 * }
	 */
}
