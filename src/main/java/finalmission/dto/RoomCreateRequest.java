package finalmission.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomCreateRequest(
        @NotBlank(message = "회의실 이름을 입력해주세요")
        String name,
        @NotNull(message = "회의실 최대수용인원을 입력해주세요")
        @Min(value = 1, message = "수용인원은 1명이상이어야 합니다.")
        @Max(value = 15, message = "수용인원은 15명이하이여야 합니다.")
        int maxNumberOfPeople
) {
}
