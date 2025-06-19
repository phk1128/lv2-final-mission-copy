package finalmission.fake;

import finalmission.dto.HolidayApiResponse;
import finalmission.exception.ErrorCode;
import finalmission.exception.InternalServerException;
import finalmission.external.HolidayClient;
import java.net.SocketTimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
@Profile({"test"})
public class FakeHolidayRestClient implements HolidayClient {

    private final RestClient restClient;
    private final String serviceKey;

    public FakeHolidayRestClient(
            @Value("${public.api.base_url}") String baseUrl,
            @Value("${public.api.key}") String serviceKey,
            RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.serviceKey = serviceKey;
    }

    @Override
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
