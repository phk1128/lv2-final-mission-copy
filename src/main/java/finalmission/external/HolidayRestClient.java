package finalmission.external;

import finalmission.dto.HolidayApiResponse;
import finalmission.exception.ErrorCode;
import finalmission.exception.InternalServerException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
public class HolidayRestClient {

    private final RestClient restClient;
    private final String serviceKey;

    public HolidayRestClient(
            @Value("${public.api.base_url}") String baseUrl,
            @Value("${public.api.key}") String serviceKey,
            RestClient.Builder builder) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        this.restClient = builder
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .requestFactory(requestFactory)
                .build();
        this.serviceKey = serviceKey;
    }

    public HolidayApiResponse getHolidaysByYearAndMonth(final int year, final int month) {
        try {
            return restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/getRestDeInfo")
                            .queryParam("ServiceKey", serviceKey)
                            .queryParam("pageNo", 1)
                            .queryParam("numOfRows", 100)
                            .queryParam("solYear", year)
                            .queryParam("solMonth", String.format("%02d", month))
                            .build())
                    .retrieve()
                    .body(HolidayApiResponse.class);
        }
        catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                throw new InternalServerException(ErrorCode.TIME_OUT_EXCEPTION);
            }
            throw new InternalServerException(ErrorCode.CONNECTION_EXCEPTION);
        }

    }
}
