package finalmission.external;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import finalmission.dto.HolidayResponse;
import finalmission.fake.FakeHolidayRestClient;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(FakeHolidayRestClient.class)
@Import(HolidayRequesterImpl.class)
@ActiveProfiles("test")
class HolidayRequesterImplTest {

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Autowired
    private HolidayRequesterImpl holidayRequester;

    @Autowired
    private MockRestServiceServer server;

    @Value("${public.api.base_url}")
    private String baseUrl;

    @Test
    @DisplayName("휴일 목록 반환 성공")
    void getHolidaysTest() {
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
        final List<HolidayResponse> holidays = holidayRequester.getHolidays(LocalDate.of(2024, 12, 25));
        final HolidayResponse first = holidays.getFirst();

        //then
        assertAll(
                () -> assertThat(holidays).hasSize(1),
                () -> assertThat(first.dateName()).isEqualTo("크리스마스"),
                () -> assertThat(first.locdate()).isEqualTo("20241225"),
                () -> assertThat(first.isHoliday()).isEqualTo("Y")
        );
        server.verify();
    }

    @Test
    @DisplayName("응답이 비어있을 때 빈 리스트 반환")
    void getHolidaysEmptyTest() {
        //given
        final String expected = """
                <response>
                    <header>
                        <resultCode>00</resultCode>
                        <resultMsg>NORMAL SERVICE.</resultMsg>
                    </header>
                    <body>
                       <items/>
                        <numOfRows>10</numOfRows>
                        <pageNo>1</pageNo>
                        <totalCount>1</totalCount>
                    </body>
                </response>
                """;

        server.expect(requestTo(containsString(baseUrl + "/getRestDeInfo")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(expected, MediaType.APPLICATION_XML));

        //when
        final List<HolidayResponse> holidays = holidayRequester.getHolidays(LocalDate.of(2024, 6, 15));

        //then
        assertThat(holidays).isEmpty();
    }
}
