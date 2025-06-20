package finalmission.service;

import finalmission.domain.Holiday;
import finalmission.dto.HolidayResponse;
import finalmission.external.HolidaysRequester;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HolidayScheduler {

    private final HolidaysRequester holidaysRequester;
    private final HolidayService holidayService;

    @Async
    @Scheduled(cron = "${schedules.cron.holiday.save}")
    public void saveHoliday() {
        final List<Holiday> holidays = holidaysRequester.getHolidays(LocalDate.now()).stream()
                .map(this::convertHoliday)
                .toList();
        holidayService.saveHoliday(holidays);
    }


    private Holiday convertHoliday(final HolidayResponse holiday) {
        final LocalDate date = LocalDate.parse(holiday.locdate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        boolean isHoliday = Objects.equals(holiday.isHoliday(), "Y");
        return new Holiday(date, holiday.dateName(), isHoliday);
    }

}
