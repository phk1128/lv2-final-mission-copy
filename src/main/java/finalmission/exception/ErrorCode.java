package finalmission.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "ERROR_001", "잘못된 요청입니다."),

    DUPLICATE_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "RES_001", "이미 예약된 시간 입니다."),
    WEEKEND_RESERVATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "RES_002", "주말 예약 불가능"),
    HOLIDAY_RESERVATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "RES_003", "공휴일은 예약이 불가능 합니다."),
    RESERVATION_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "RES_004", "삭제할 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RES_005", "예약을 찾을 수 없습니다."),

    EXCEEDED_ROOM_CAPACITY(HttpStatus.BAD_REQUEST, "ROOM_001", "사용 인원은 최대 인원을 넘을 수 없습니다."),

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER_001", "패스워드가 일치 하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_002", "회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER_003", "이미 존재하는 이메일입니다."),

    INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "TIME_001", "시간은 0시 ~ 24시까지만 가능 합니다."),
    INVALID_TIME_ORDER(HttpStatus.BAD_REQUEST, "TIME_002", "시작 시간은 끝 시간보다 미래일 수 없습니다."),

    EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "API_001", "외부 API 호출 중 오류가 발생했습니다."),

    DUPLICATE_ROOM_NAME(HttpStatus.BAD_REQUEST, "ROOM_001", "회의실 이름은 중복될 수 없습니다."),

    TIME_OUT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_001", "요청 시간이 초과되었습니다."),
    CONNECTION_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_002", "연결에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
