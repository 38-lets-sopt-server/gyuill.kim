package org.sopt.domain.post.presentation.controller;

import java.util.List;

import org.sopt.domain.post.application.service.PostCommandService;
import org.sopt.domain.post.application.service.PostQueryService;
import org.sopt.domain.post.application.dto.CreatePostCommand;
import org.sopt.domain.post.application.dto.PostPageResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.dto.UpdatePostCommand;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.CreatePostRequest;
import org.sopt.domain.post.presentation.dto.request.GetPostsRequest;
import org.sopt.domain.post.presentation.dto.request.UpdatePostRequest;
import org.sopt.domain.post.presentation.dto.response.PostPageResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    public PostController(PostCommandService postCommandService, PostQueryService postQueryService) {
        this.postCommandService = postCommandService;
        this.postQueryService = postQueryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody CreatePostRequest request) {
        request.validate();
        PostResult result = postCommandService.createPost(new CreatePostCommand(
                request.boardType(),
                request.title(),
                request.content(),
                request.authorUserId()
        ));
        return ApiResponse.success(PostSuccessCode.POST_CREATED, new PostResponse(
                result.id(),
                result.boardType(),
                result.title(),
                result.content(),
                result.author(),
                result.createdAt()
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PostPageResponse>> getAllPosts(
            @RequestParam(required = false) BoardType boardType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        GetPostsRequest request = new GetPostsRequest(boardType, page, size);
        request.validate();

        PostPageResult result = postQueryService.getPosts(request.boardType(), request.page(), request.size());
        List<PostResponse> responses = result.content().stream()
                .map(postResult -> new PostResponse(
                        postResult.id(),
                        postResult.boardType(),
                        postResult.title(),
                        postResult.content(),
                        postResult.author(),
                        postResult.createdAt()
                ))
                .toList();
        return ApiResponse.success(PostSuccessCode.POST_LIST_READ, new PostPageResponse(
                responses,
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages(),
                result.hasNext()
        ));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long postId) {
        PostResult result = postQueryService.getPost(postId);
        return ApiResponse.success(PostSuccessCode.POST_READ, new PostResponse(
                result.id(),
                result.boardType(),
                result.title(),
                result.content(),
                result.author(),
                result.createdAt()
        ));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
        request.validate();
        postCommandService.updatePost(postId, new UpdatePostCommand(request.title(), request.content()));
        return ApiResponse.success(PostSuccessCode.POST_UPDATED, null);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId) {
        postCommandService.deletePost(postId);
        return ApiResponse.success(PostSuccessCode.POST_DELETED, null);
    }
}
