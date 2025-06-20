package finalmission.service;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import finalmission.dto.HolidayResponse;
import finalmission.external.HolidaysRequester;
import finalmission.repository.HolidayRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
@ActiveProfiles("test")
@EnableScheduling
class HolidaySchedulerTest {

    @MockitoBean
    private HolidaysRequester holidaysRequester;

    @MockitoBean
    private HolidayRepository holidayRepository;

    @MockitoSpyBean
    private HolidayService holidayService;

    @MockitoSpyBean
    private HolidayScheduler holidayScheduler;

    @Test
    @DisplayName("스케줄러가 정상적으로 휴일 데이터를 조회하고 저장한다")
    void saveHoliday() {
        // given
        List<HolidayResponse> mockHolidays = List.of(
                new HolidayResponse("20240101", "신정", "Y"),
                new HolidayResponse("20240209", "설날", "Y")
        );
        given(holidaysRequester.getHolidays(any(LocalDate.class)))
                .willReturn(mockHolidays);

        // when
        holidayScheduler.saveHoliday();

        // then
        await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    verify(holidaysRequester).getHolidays(any(LocalDate.class));
                    verify(holidayRepository).saveAll(any());
                    verify(holidayService).saveHoliday(anyList());
                });
    }

    @Test
    @DisplayName("외부 API에서 빈 응답이 와도 정상 처리된다")
    void saveHolidayWithEmptyResponse() {
        // given
        given(holidaysRequester.getHolidays(any(LocalDate.class)))
                .willReturn(List.of());

        // when
        holidayScheduler.saveHoliday();

        // then
        await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    verify(holidaysRequester).getHolidays(any(LocalDate.class));
                    verify(holidayRepository).saveAll(any());
                    verify(holidayService).saveHoliday(List.of());
                });
    }

    @Test
    @DisplayName("스케쥴러가 잘 실행 되는지 확인 한다.")
    void checkSchedule() {
        // given
        given(holidaysRequester.getHolidays(any(LocalDate.class)))
                .willReturn(List.of());

        // should
        await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    verify(holidaysRequester).getHolidays(any(LocalDate.class));
                    verify(holidayRepository).saveAll(any());
                    verify(holidayService).saveHoliday(List.of());
                });
    }
}
