package finalmission.external;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import finalmission.dto.HolidayApiResponse;
import finalmission.exception.ErrorCode;
import finalmission.exception.InternalServerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

class HolidayRestClientTimeoutTest {

    private WireMockServer wireMockServer;

    private HolidayRestClient holidayRestClient;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
        wireMockServer.start();

        holidayRestClient = new HolidayRestClient(
                "http://localhost:8089",
                "test-key",
                RestClient.builder()
        );
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("읽기 타임아웃(5초)이 발생하면 ResourceAccessException이 발생한다")
    void readTimeoutTest() {
        // given
        wireMockServer.stubFor(get(urlPathEqualTo("/getRestDeInfo"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<response></response>")
                        .withFixedDelay(6000)));

        //should
        assertThatThrownBy(() -> holidayRestClient.getHolidaysByYearAndMonth(2024, 12))
                .isInstanceOf(InternalServerException.class);
    }

    @Test
    @DisplayName("연결 타임아웃 3초 이상 발생하면 ResourceAccessException이 발생한다")
    void connectionTimeoutTest() {
        //given
        HolidayRestClient timeoutClient = new HolidayRestClient(
                "http://10.255.255.1:12345", // 접근 불가능한 IP
                "test-key",
                RestClient.builder()
        );

        //should
        assertThatThrownBy(() -> timeoutClient.getHolidaysByYearAndMonth(2024, 12))
                .isInstanceOf(InternalServerException.class);
    }
}
