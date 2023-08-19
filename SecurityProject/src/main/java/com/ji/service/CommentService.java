package com.ji.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ji.dbs.Commentboard;
import com.ji.dbs.CommentboardRepository;
import com.ji.dbs.Noticeboard;
import com.ji.dbs.NoticeboardRepository;
import com.ji.user.SiteUser;

@Service
public class CommentService {

    private final CommentboardRepository commentboardRepository;
    private final NoticeboardRepository noticeboardRepository;

    @Autowired
    public CommentService(CommentboardRepository commentboardRepository, NoticeboardRepository noticeboardRepository) {
        this.commentboardRepository = commentboardRepository;
        this.noticeboardRepository = noticeboardRepository;
    }

    // 댓글 보이기
    public List<Commentboard> listComments(Integer noticeNo) {
        return commentboardRepository.findByNoticeboard_NoticeNo(noticeNo);
    }

    // 댓글 추가
    public void addComment(Integer noticeNo, String commentContent, SiteUser author) {
        Noticeboard noticeboard = noticeboardRepository.findById(noticeNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid noticeboardId: " + noticeNo));
        Commentboard comment = new Commentboard();
        comment.setNoticeboard(noticeboard);
        comment.setCommentContent(commentContent);
        comment.setAuthor(author);

        commentboardRepository.save(comment);
    }

    // 댓글 삭제
    public void removeComment(Integer commentNo) {
    	Commentboard comment = commentboardRepository.findById(commentNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment number: " + commentNo));
    	commentboardRepository.delete(comment);
    }

    // 댓글 수정
    public void editComment(String commentContent, Integer commentNo) {
    	Commentboard comment = commentboardRepository.findById(commentNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment number: " + commentNo));
    	comment.setCommentNo(commentNo);
        comment.setCommentContent(commentContent);
        
        this.commentboardRepository.save(comment);
    }
}
