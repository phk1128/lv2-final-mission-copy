package finalmission.domain;

import finalmission.exception.BadRequestException;
import finalmission.exception.ErrorCode;
import finalmission.global.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    public Member(final String email, final String password, final MemberRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void validatePassword(final String password) {
        if (!Objects.equals(password, this.password)) {
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof final Member member)) {
            return false;
        }
        return Objects.equals(getId(), member.getId()) && Objects.equals(getEmail(), member.getEmail())
                && Objects.equals(getPassword(), member.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPassword());
    }
}
