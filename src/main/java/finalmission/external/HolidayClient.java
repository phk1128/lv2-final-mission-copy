package finalmission.external;

import finalmission.dto.HolidayApiResponse;

public interface HolidayClient {
    HolidayApiResponse getHolidaysByYearAndMonth(int year, int month);
}