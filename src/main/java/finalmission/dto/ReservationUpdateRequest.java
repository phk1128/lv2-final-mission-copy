package finalmission.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateRequest(
        @NotBlank(message = "예약 날짜를 입력해주세요")
        @Future(message = "예약 날짜는 현재 날짜 이후여야 합니다")
        LocalDate reservationDate,
        @NotNull(message = "시작 시간을 입력해주세요")
        @Min(value = 0, message = "시작 시간은 0시 이후여야 합니다")
        @Max(value = 24, message = "시작 시간은 24시 이전이어야 합니다")
        int startTime,
        @NotNull(message = "끝 시간을 입력해주세요")
        @Min(value = 0, message = "끝 시간은 0시 이후여야 합니다")
        @Max(value = 24, message = "끝 시간은 24시 이전이어야 합니다")
        int endTime,
        @NotNull(message = "인원수를 입력해주세요")
        @Min(value = 1, message = "인원수는 1명 이상이어야 합니다")
        int numberOfPeople
) {
}
