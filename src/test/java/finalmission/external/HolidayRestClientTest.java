package finalmission.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

import finalmission.dto.HolidayApiResponse;
import finalmission.dto.HolidayApiResponse.Body;
import finalmission.dto.HolidayResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(HolidayRestClient.class)
class HolidayRestClientTest {

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private HolidayRestClient holidayRestClient;

    @Autowired
    private MockRestServiceServer server;

    @Value("${public.api.base_url}")
    private String baseUrl;

    @Nested
    class SuccessCase {

        @Test
        @DisplayName("api 응답을 정상적이므로 resultCode 00을 받는다.")
        void successCase() {
            //given
            final String expected = """
                <response>
                    <header>
                        <resultCode>00</resultCode>
                        <resultMsg>NORMAL SERVICE.</resultMsg>
                    </header>
                    <body>
                        <items>
                            <item>
                                <dateKind>01</dateKind>
                                <dateName>크리스마스</dateName>
                                <isHoliday>Y</isHoliday>
                                <locdate>20241225</locdate>
                                <seq>1</seq>
                            </item>
                        </items>
                        <numOfRows>10</numOfRows>
                        <pageNo>1</pageNo>
                        <totalCount>1</totalCount>
                    </body>
                </response>
                """;

            server.expect(requestTo(containsString(baseUrl + "/getRestDeInfo")))
                    .andExpect(method(HttpMethod.GET))
                    .andExpect(queryParam("pageNo", "1"))
                    .andExpect(queryParam("numOfRows", "100"))
                    .andExpect(queryParam("solYear", "2024"))
                    .andExpect(queryParam("solMonth", "12"))
                    .andRespond(withSuccess(expected, MediaType.APPLICATION_XML));

            //when
            final HolidayApiResponse response = holidayRestClient.getHolidaysByYearAndMonth(2024, 12);
            final String resultCode = response.header().resultCode();
            final List<HolidayResponse> items = response.body().items().item();
            final HolidayResponse first = items.getFirst();

            //then
            assertAll(
                    () -> assertThat(resultCode).isEqualTo("00"),
                    () -> assertThat(first.isHoliday()).isEqualTo("Y"),
                    () -> assertThat(first.locdate()).isEqualTo("20241225"),
                    () -> assertThat(first.dateName()).isEqualTo("크리스마스")
            );
            server.verify();
        }
    }

    @Nested
    class FailureCase {

        @Test
        @DisplayName("api 응답이 애플리케이션 에러이므로 resultCode 01을 받는다")
        void failureCase() {
            //given
            final String expected = """
                <response>
                       <header>
                           <resultCode>01</resultCode>
                           <resultMsg>APPLICATION ERROR</resultMsg>
                       </header>
                   </response>
                """;

            server.expect(requestTo(containsString(baseUrl + "/getRestDeInfo")))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(expected, MediaType.APPLICATION_XML));

            //when
            final HolidayApiResponse response = holidayRestClient.getHolidaysByYearAndMonth(2024, 12);
            final String resultCode = response.header().resultCode();
            final Body body = response.body();

            //then
            assertAll(
                    () -> assertThat(resultCode).isEqualTo("01"),
                    () -> assertThat(body).isNull()
            );
        }
    }


}
