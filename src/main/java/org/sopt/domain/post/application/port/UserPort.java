package org.sopt.domain.post.application.port;

import org.sopt.domain.user.domain.model.User;

/**
 * Post 도메인이 User 도메인 세부 구현에 직접 의존하지 않도록 분리한 포트.
 * 이거는 사실 지금의 상황에서는 그렇게 어울리지는 않는다고 생각하는데요
 * 이유는 프로젝트 구조에 비해서 과도한 책임 분리라고 생각하기 때문입니다.
 * 과제가 아닌 좀더 확장의 가능성이 열려있는 상황에서 좋다고 생각하는 아키텍처입니다.
 * 그래도 오비니까 헥사고날 향을 첨가해봤습니다.
 * 실무에서도 아예 처음부터 완전한 헥사고날 아키텍처로 설계하는 일이 거의 없다고 해요(제 피셜이 아닌 삼성 서버 개발자분이랑 얘기하다 들었음 진짜에요)
 * 오히려 필요에 따라 이렇게 부분적으로 패턴을 차용하는게 더 자연스럽고 흔한 일이라고 하더라고요.
 */
public interface UserPort {

    /**
     * 게시글 작성자 또는 반응 주체 사용자 정보를 조회한다.
     *
     * @param userId 사용자 ID
     * @return 사용자 엔티티
     */
    User getUser(Long userId);
}
