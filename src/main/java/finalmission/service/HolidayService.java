package finalmission.service;

import finalmission.domain.Holiday;
import finalmission.repository.HolidayRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    @Transactional(readOnly = true)
    public boolean isHoliday(final LocalDate date) {
        return holidayRepository.existsByDate(date);
    }

    @Transactional
    public void saveHoliday(final List<Holiday> holidays) {
        holidayRepository.saveAll(holidays);
    }

}
