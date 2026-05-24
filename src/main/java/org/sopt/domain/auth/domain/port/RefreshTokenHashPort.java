package org.sopt.domain.auth.domain.port;

/**
 * refresh token 원문을 저장용 해시로 변환하는 포트.
 */
public interface RefreshTokenHashPort {

    /**
     * refresh token 원문으로부터 저장용 해시 값을 만든다.
     *
     * @param refreshToken refresh token 원문
     * @return 저장용 해시 값
     */
    String hash(String refreshToken);

    /**
     * refresh token 원문과 저장된 해시 값을 비교한다.
     *
     * @param refreshToken refresh token 원문
     * @param expectedHash 저장된 해시 값
     * @return 일치 여부
     */
    boolean matches(String refreshToken, String expectedHash);
}
