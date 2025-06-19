package finalmission.external;

import finalmission.dto.HolidayApiResponse;
import finalmission.dto.HolidayResponse;
import finalmission.exception.BadGatewayException;
import finalmission.exception.ErrorCode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class HolidayRequesterImpl implements HolidaysRequester {

    private final HolidayClient holidayRestClient;

    @Override
    public List<HolidayResponse> getHolidays(final LocalDate date) {
        try {
            final HolidayApiResponse response = holidayRestClient.getHolidaysByYearAndMonth(date.getYear(), date.getMonthValue());
            return getHolidayResponseByApiResponse(response);
        } catch (Exception e) {
            log.error("API 호출 중 예상치 못한 예외 발생", e);
            throw new BadGatewayException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    private List<HolidayResponse> getHolidayResponseByApiResponse(final HolidayApiResponse response) {
        if (response.body() == null) {
            return Collections.emptyList();
        }
        final HolidayApiResultCode resultCode = getHolidayApiResultCodeByApiResponse(response);
        if (!resultCode.isSuccess()) {
            log.error("API 오류 : {} {}", resultCode, resultCode.getDescription());
        }
        return extractHolidaysData(response);

    }

    private List<HolidayResponse> extractHolidaysData(final HolidayApiResponse response) {
        if (response.body() == null ||
                response.body().items() == null ||
                response.body().items().item() == null) {
            return Collections.emptyList();
        }

        return response.body().items().item();
    }

    private HolidayApiResultCode getHolidayApiResultCodeByApiResponse(final HolidayApiResponse response) {
        if (response.header() == null || response.header().resultCode() == null) {
            return HolidayApiResultCode.UNKNOWN_ERROR;
        }
        return HolidayApiResultCode.fromCode(response.header().resultCode());
    }

}
