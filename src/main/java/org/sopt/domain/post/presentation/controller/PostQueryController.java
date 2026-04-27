package org.sopt.domain.post.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.domain.post.application.dto.PostCursorResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.service.PostQueryService;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.GetPostsRequest;
import org.sopt.domain.post.presentation.dto.request.SearchPostsRequest;
import org.sopt.domain.post.presentation.dto.response.PostCursorPageResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.domain.post.presentation.mapper.PostResponseMapper;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@Tag(name = "Post Query", description = "게시글 조회 API")
public class PostQueryController {

    private final PostQueryService postQueryService;
    private final PostResponseMapper postResponseMapper;

    public PostQueryController(PostQueryService postQueryService, PostResponseMapper postResponseMapper) {
        this.postQueryService = postQueryService;
        this.postResponseMapper = postResponseMapper;
    }

    @GetMapping
    @Operation(summary = "게시글 목록 조회", description = "게시판 타입과 커서 기반 페이징으로 게시글 목록을 조회합니다.")
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

    @GetMapping("/search")
    @Operation(summary = "게시글 검색", description = "키워드와 커서 기반 페이징으로 게시글을 검색합니다.")
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

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 정보를 조회합니다.")
    public ResponseEntity<CommonApiResponse<PostResponse>> getPost(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId
    ) {
        PostResult result = postQueryService.getPost(postId);
        PostResponse response = postResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_READ, response);
    }

    @GetMapping("/{postId}/preview")
    @Operation(summary = "게시글 미리보기 조회", description = "게시글 미리보기 정보를 조회합니다.")
    public ResponseEntity<CommonApiResponse<PostResponse>> getPostPreview(
            @Parameter(description = "게시글 ID", example = "1")
            @PathVariable Long postId
    ) {
        PostResult result = postQueryService.getPostPreview(postId);
        PostResponse response = postResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_READ, response);
    }
}
