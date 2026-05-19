package org.sopt.domain.post.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.application.dto.PostCursorResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.service.PostQueryService;
import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.GetPostsRequest;
import org.sopt.domain.post.presentation.dto.request.SearchPostsRequest;
import org.sopt.domain.post.presentation.dto.response.PostCursorPageResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.domain.post.presentation.mapper.PostResponseMapper;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게시글 읽기 API를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostQueryController implements PostQueryControllerDocs {

    private final PostQueryService postQueryService;
    private final PostResponseMapper postResponseMapper;

    /**
     * 게시글 목록을 커서 기반으로 조회한다.
     *
     * @param request 게시글 목록 조회 요청
     * @return 게시글 페이지 응답
     */
    @GetMapping
    @Override
    public ResponseEntity<CommonApiResponse<PostCursorPageResponse>> getAllPosts(
            @Valid @ModelAttribute GetPostsRequest request
    ) {
        PostCursorResult result = postQueryService.getPosts(request.boardType(), request.cursor(), request.size());
        PostCursorPageResponse response = postResponseMapper.toCursorPageResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_LIST_READ, response);
    }

    /**
     * 키워드로 게시글을 검색한다.
     *
     * @param request 게시글 검색 요청
     * @return 검색 결과 페이지 응답
     */
    @GetMapping("/search")
    @Override
    public ResponseEntity<CommonApiResponse<PostCursorPageResponse>> searchPosts(
            @Valid @ModelAttribute SearchPostsRequest request
    ) {
        PostCursorResult result = postQueryService.searchPosts(
                request.keyword(),
                request.cursor(),
                request.size()
        );
        PostCursorPageResponse response = postResponseMapper.toCursorPageResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_LIST_READ, response);
    }

    /**
     * 일반 공개 가능한 게시글 상세를 조회한다.
     *
     * @param postId 게시글 ID
     * @return 게시글 상세 응답
     */
    @GetMapping("/{postId}")
    @Override
    public ResponseEntity<CommonApiResponse<PostResponse>> getPost(
            @PathVariable Long postId
    ) {
        PostResult result = postQueryService.getPost(postId);
        PostResponse response = postResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_READ, response);
    }

    /**
     * 숨김 게시글 확인용 상세를 조회한다.
     *
     * @param postId 게시글 ID
     * @return 게시글 상세 응답
     */
    @GetMapping("/{postId}/hidden")
    @Override
    public ResponseEntity<CommonApiResponse<PostResponse>> getHiddenPost(
            @PathVariable Long postId
    ) {
        PostResult result = postQueryService.getHiddenPost(postId);
        PostResponse response = postResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_HIDDEN_READ, response);
    }
}
