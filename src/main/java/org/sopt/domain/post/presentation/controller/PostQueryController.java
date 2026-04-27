package org.sopt.domain.post.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.domain.post.application.dto.PostCursorResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.service.PostQueryService;
import org.sopt.domain.post.domain.exception.PostErrorCode;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.GetPostsRequest;
import org.sopt.domain.post.presentation.dto.request.SearchPostsRequest;
import org.sopt.domain.post.presentation.dto.response.PostCursorPageResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.domain.post.presentation.mapper.PostResponseMapper;
import org.sopt.global.annotation.ApiExceptions;
import org.sopt.global.code.GlobalErrorCode;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게시글 읽기 API를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/posts")
@Tag(name = "Post", description = "게시글 조회 API")
public class PostQueryController {

    private final PostQueryService postQueryService;
    private final PostResponseMapper postResponseMapper;

    public PostQueryController(PostQueryService postQueryService, PostResponseMapper postResponseMapper) {
        this.postQueryService = postQueryService;
        this.postResponseMapper = postResponseMapper;
    }

    /**
     * 게시글 목록을 커서 기반으로 조회한다.
     *
     * @param boardType 게시판 타입
     * @param cursor 다음 페이지 조회용 커서
     * @param size 페이지 크기
     * @return 게시글 페이지 응답
     */
    @GetMapping
    @Operation(summary = "게시글 목록 조회", description = "게시판 타입과 커서 기반 페이징으로 게시글 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<PostCursorPageResponse>> getAllPosts(
            @Parameter(description = "게시판 타입", example = "FREE")
            @RequestParam(required = false) BoardType boardType,
            @Parameter(description = "다음 페이지 조회용 커서", example = "10")
            @RequestParam(required = false) Long cursor,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        GetPostsRequest request = new GetPostsRequest(boardType, cursor, size);
        request.validate();

        PostCursorResult result = postQueryService.getPosts(request.boardType(), request.cursor(), request.size());
        PostCursorPageResponse response = postResponseMapper.toCursorPageResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_LIST_READ, response);
    }

    /**
     * 키워드로 게시글을 검색한다.
     *
     * @param keyword 검색 키워드
     * @param cursor 다음 페이지 조회용 커서
     * @param size 페이지 크기
     * @return 검색 결과 페이지 응답
     */
    @GetMapping("/search")
    @Operation(summary = "게시글 검색", description = "키워드와 커서 기반 페이징으로 게시글을 검색합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 검색 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<PostCursorPageResponse>> searchPosts(
            @Parameter(description = "검색 키워드", example = "스프링")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "다음 페이지 조회용 커서", example = "10")
            @RequestParam(required = false) Long cursor,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        SearchPostsRequest request = new SearchPostsRequest(keyword, cursor, size);
        request.validate();

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
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<PostResponse>> getPost(
            @Parameter(description = "게시글 ID", example = "1")
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
    @Operation(summary = "숨김 게시글 조회", description = "일반 공개 상세 조회와 달리 숨김 상태 게시글 확인에 사용합니다.")
    @ApiResponse(responseCode = "200", description = "숨김 게시글 조회 성공")
    @ApiExceptions({PostErrorCode.class, GlobalErrorCode.class})
    public ResponseEntity<CommonApiResponse<PostResponse>> getHiddenPost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId
    ) {
        PostResult result = postQueryService.getHiddenPost(postId);
        PostResponse response = postResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_HIDDEN_READ, response);
    }
}
