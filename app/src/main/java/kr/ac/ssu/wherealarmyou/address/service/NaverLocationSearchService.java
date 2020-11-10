package kr.ac.ssu.wherealarmyou.address.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Tm128;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.address.Location;
import kr.ac.ssu.wherealarmyou.common.HttpUtil;
import lombok.SneakyThrows;

public class NaverLocationSearchService implements LocationSearchService {
    // 네이버 지역 검색 REST API URL
    private final String NAVER_SEARCH_URL;
    // 네이버 개발자 센터 CLIENT ID
    private final String NAVER_DEVELOPERS_CLIENT_ID;
    // 네이버 개발자 센터 CLIENT SECRET
    private final String NAVER_DEVELOPERS_CLIENT_SECRET;


    // 네이버 클라우드 플랫폼 GeoCoding REST API URL
    private final String NAVER_GEOCODING_URL;
    // 네이버 클라우드 플랫폼 CLIENT ID
    private final String NAVER_CLOUD_PLATFORM_CLIENT_ID;
    // 네이버 클라우드 플랫폼 CLIENT SECRET
    private final String NAVER_CLOUD_PLATFORM_CLIENT_SECRET;

    // secret.xml의 설정 값을 가져오기 위해 필요
    public NaverLocationSearchService(Context context) {
        NAVER_SEARCH_URL = context.getString(R.string.naver_developers_local_search_url);
        NAVER_DEVELOPERS_CLIENT_ID = context.getString(R.string.naver_developers_client_id);
        NAVER_DEVELOPERS_CLIENT_SECRET = context.getString(R.string.naver_developers_client_secret);

        NAVER_GEOCODING_URL = context.getString(R.string.naver_cloud_platform_geocoding_url);
        NAVER_CLOUD_PLATFORM_CLIENT_ID = context.getString(R.string.naver_cloud_platform_client_id);
        NAVER_CLOUD_PLATFORM_CLIENT_SECRET = context.getString(R.string.naver_cloud_platform_client_secret);
    }

    @SneakyThrows
    @Override
    public List<Location> search(String query) {
        // 네이버 지역 검색 기본 URL + Query String
        String searchUrl = NAVER_SEARCH_URL
                + "?" + "query=" + URLEncoder.encode(query, "UTF-8")
                + "&" + "display=" + "5"         // 검색 결과 출력 건수 지정; 최대 5개
                + "&" + "start=" + "1"           // 검색 시작 위치; 1만 가능
                + "&" + "sort=" + "random";       // 정렬 옵션; random은 유사도순


        // 인증에 필요한 HTTP 헤더
        Map<String, String> headers = Map.of(
                "X-Naver-Client-Id", NAVER_DEVELOPERS_CLIENT_ID,
                "X-Naver-Client-Secret", NAVER_DEVELOPERS_CLIENT_SECRET
        );

        String resultJson = HttpUtil.requestHttp(searchUrl, headers);

        Log.d("AddressSearchService", "resultJson : " + resultJson);

        return parseJson(resultJson);
    }

    // 네이버 지역 검색 결과 JSON을 파싱
    // 네이버 지역 검색 결과에서 제공하는 Tm128 좌표계의 좌표를 위경도 좌표계로 변환해서 사용; 우선 Naver GeoCoding을 거치지 않고 구현
    private List<Location> parseJson(String naverLocationSearchJson) {
        JsonObject root = new JsonParser().parse(naverLocationSearchJson).getAsJsonObject();

        JsonArray items = root.getAsJsonArray("items");

        List<Location> result = new ArrayList<>();
        for (JsonElement item : items) {
            JsonObject i = item.getAsJsonObject();
            String title = i.get("title").getAsString();
            title = removeTag(title);       // title에 HTML 태그가 들어가있으므로 제거
            String jibunAddress = i.get("address").getAsString();
            String roadAddress = i.get("roadAddress").getAsString();

            int mapx = i.get("mapx").getAsInt();
            int mapy = i.get("mapy").getAsInt();
            // Tm128 좌표계의 좌표를 위경도 좌표로 변환
            LatLng latLng = new Tm128(mapx, mapy).toLatLng();

            Location location = new Location(title, roadAddress, jibunAddress, latLng.longitude, latLng.latitude);

            result.add(location);
        }

        return result;
    }

    // 태그를 제거한 문자열 반환
    private String removeTag(String html) {
        return html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
    }
}
