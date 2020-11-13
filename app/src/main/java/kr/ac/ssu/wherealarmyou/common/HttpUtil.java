package kr.ac.ssu.wherealarmyou.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

public class HttpUtil {

    @SneakyThrows
    public static Mono<String> requestHttp(String requestUrl, Map<String, String> headers) {
        return Mono.fromCallable(() -> {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // HTTP Header 설정
            for (Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            char[] buffer = new char[1024];
            StringBuffer sb = new StringBuffer();

            // 응답 모으기
            int len = 0;
            while ((len = reader.read(buffer)) > 0) {
                sb.append(buffer, 0, len);
            }

            return sb.toString();
        });
    }
}
