package com.ll.tagtune.boundedContext.comment.service;

import com.ll.tagtune.base.rsData.RsData;
import com.ll.tagtune.boundedContext.album.entity.Album;
import com.ll.tagtune.boundedContext.album.service.AlbumService;
import com.ll.tagtune.boundedContext.artist.entity.Artist;
import com.ll.tagtune.boundedContext.artist.service.ArtistService;
import com.ll.tagtune.boundedContext.comment.dto.CommentResponseDTO;
import com.ll.tagtune.boundedContext.comment.entity.Comment;
import com.ll.tagtune.boundedContext.comment.repository.CommentRepository;
import com.ll.tagtune.boundedContext.member.entity.Member;
import com.ll.tagtune.boundedContext.member.service.MemberService;
import com.ll.tagtune.boundedContext.reply.dto.ReplyDTO;
import com.ll.tagtune.boundedContext.reply.entity.Reply;
import com.ll.tagtune.boundedContext.reply.repository.ReplyRepository;
import com.ll.tagtune.boundedContext.reply.service.ReplyService;
import com.ll.tagtune.boundedContext.track.dto.TrackDetailDTO;
import com.ll.tagtune.boundedContext.track.entity.Track;
import com.ll.tagtune.boundedContext.track.service.TrackService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private AlbumService albumService;
    private Member member1;
    private Member member2;
    private Track track;
    private Comment[] comments;
    private int num = 0;

    //todo 조회, 삭제, 수정, 좋아요, 대댓글,

    @BeforeEach
    void beforeEach() {
        String content = "Hello hello%d".formatted(num);
        String content2 = "vwerwerv_%d %d";

        member1 = memberService.join("parkkhee_%d".formatted(num), "1234").getData();
        member2 = memberService.join("pahee_%d".formatted(num), "1234").getData();

        Artist artist = artistService.createArtist("박관희");
        Album album = albumService.createAlbum("albumname", artist);

        track = trackService.createTrack(content, artist, album);

        comments = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> commentService
                        .saveComment(content2.formatted(num, i), track, member1).getData())
                .toArray(Comment[]::new);

        IntStream.rangeClosed(1, 10)
                .mapToObj(i -> replyService
                        .saveReply(member1, comments[0].getId(), content2.formatted(num, i)));
        this.num++;
    }

    @Test
    @DisplayName("RecommendCnt, DeleteStatus 저장시 기본값 세팅 잘 되는지 test")
    void t001() throws Exception {
        assertThat(comments).isNotEmpty();
        Arrays.stream(comments).forEach(comment -> assertThat(comment.getDeleteStatus()).isFalse());
    }

    @Test
    @DisplayName("댓글 삭제")
    void t002() throws Exception {
        Comment comment = comments[0];
        commentService.deleteComment(comment.getId(), member1.getId());

        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }

    @Test
    @DisplayName("댓글 수정")
    void t004() throws Exception {
        Comment comment = comments[0];

        String content = "abdbdsdfawead";
        RsData<Comment> commentRsData = commentService.modifyComment(comment.getId(), content, member1);

        assertThat(commentRsData.getData().getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("부모 댓글에 reply추가")
    void t005() throws Exception {
        Comment comment = comments[0];
        int beforeReplySize = comment.getReplies().size();
        String content = "babybaby";

        RsData<Reply> replyRsData = replyService.saveReply(member1, comment.getId(), content);

        assertThat(comment.getReplies().size()).isEqualTo(beforeReplySize + 1);
    }

    @Test
    @DisplayName("댓글 삭제시 getDeleteStatus()가 true")
    void t006() throws Exception {
        Comment parentComment = comments[0];
        String content = "babybaby";

        replyService.saveReply(member1, parentComment.getId(), content);

        commentService.deleteComment(parentComment.getId(), parentComment.getMember().getId());

        assertThat(parentComment.getDeleteStatus()).isTrue();
        assertThat(commentRepository.findById(parentComment.getId())).isPresent();
    }

    @Test
    @DisplayName("자식, 부모 댓글 둘 다 삭제시")
    void t007() throws Exception {
        Comment parentComment = comments[0];
        String content = "babybaby";

        RsData<Reply> replyRsData = replyService.saveReply(member1, parentComment.getId(), content);

        commentService.deleteComment(parentComment.getId(), parentComment.getMember().getId());

        assertThat(parentComment.getDeleteStatus()).isTrue();
        assertThat(commentRepository.findById(parentComment.getId())).isPresent();

        replyService.deleteReply(replyRsData.getData().getId(), member1.getId());

        assertThat(commentRepository.findById(parentComment.getId())).isEmpty();
    }

    @Autowired
    ReplyRepository replyRepository;

    //todo 수정 테스트 다시
    @Test
    @DisplayName("대댓글 수정하기")
    void t008() throws Exception {
        String newContent = "newnewnew";
        String oriContent = comments[0].getContent();

        Member member = memberService.join("parkkhee_%d".formatted(num), "1234").getData();
        RsData<Reply> replyRsData = replyService.saveReply(member, comments[0].getId(), oriContent);

        RsData<Reply> newReplyRsData =
                replyService.modifyReply(replyRsData.getData().getId(),
                        comments[0].getId(), newContent, member.getId());

        assertThat(newReplyRsData.getData().getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("쿼리 dsl 잘 활용 되는지")
    void t009() throws Exception {
        List<CommentResponseDTO> commentsWithReplies = commentService
                .getCommentsWithReplies(
                        new TrackDetailDTO(
                                track.getId()
                                , track.getTitle()
                                , track.getArtist().getId()
                                , track.getArtist().getArtistName()
                                , track.getAlbum().getId()
                                , track.getAlbum().getName()
                        )
                );

        assertThat(commentsWithReplies).isNotEmpty();
    }

    @Test
    @DisplayName("commentId로 replies 만들기")
    void t010() throws Exception {
        List<CommentResponseDTO> commentsWithReplies = commentService
                .getCommentsWithReplies(
                        new TrackDetailDTO(
                                track.getId()
                                , track.getTitle()
                                , track.getArtist().getId()
                                , track.getArtist().getArtistName()
                                , track.getAlbum().getId()
                                , track.getAlbum().getName()
                        )
                );

        replyService
                .saveReply(member1, comments[0].getId(), "asdada");

        assertThat(replyService.getReplies(comments[0].getId()).getData().size()).isEqualTo(1);

        List<ReplyDTO> replies = commentsWithReplies
                .stream()
                .flatMap(i -> replyService.getReplies(i.getCommentId()).getData().stream())
                .collect(Collectors.toList());

        assertThat(replies).isNotEmpty();
    }
}