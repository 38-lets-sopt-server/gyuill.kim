package org.sopt.domain.post.presentation.controller;

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
public class PostQueryController {

    private final PostQueryService postQueryService;
    private final PostResponseMapper postResponseMapper;

    public PostQueryController(PostQueryService postQueryService, PostResponseMapper postResponseMapper) {
        this.postQueryService = postQueryService;
        this.postResponseMapper = postResponseMapper;
    }

    @GetMapping
    public ResponseEntity<CommonApiResponse<PostCursorPageResponse>> getAllPosts(
            @RequestParam(required = false) BoardType boardType,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        GetPostsRequest request = new GetPostsRequest(boardType, cursor, size);
        request.validate();

        PostCursorResult result = postQueryService.getPosts(request.boardType(), request.cursor(), request.size());
        PostCursorPageResponse response = postResponseMapper.toCursorPageResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_LIST_READ, response);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonApiResponse<PostCursorPageResponse>> searchPosts(
            @RequestParam(required = false) String titleKeyword,
            @RequestParam(required = false) String authorNickname,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        SearchPostsRequest request = new SearchPostsRequest(titleKeyword, authorNickname, cursor, size);
        request.validate();

        PostCursorResult result = postQueryService.searchPosts(
                request.titleKeyword(),
                request.authorNickname(),
                request.cursor(),
                request.size()
        );
        PostCursorPageResponse response = postResponseMapper.toCursorPageResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_LIST_READ, response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommonApiResponse<PostResponse>> getPost(@PathVariable Long postId) {
        PostResult result = postQueryService.getPost(postId);
        PostResponse response = postResponseMapper.toResponse(result);

        return CommonApiResponse.successResponse(PostSuccessCode.POST_READ, response);
    }
}
