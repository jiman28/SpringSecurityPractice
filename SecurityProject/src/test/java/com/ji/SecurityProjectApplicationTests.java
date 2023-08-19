package com.ji;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ji.dbs.Commentboard;
import com.ji.dbs.CommentboardRepository;
import com.ji.dbs.Noticeboard;
import com.ji.dbs.NoticeboardRepository;

@SpringBootTest
class SecurityProjectApplicationTests {
	
	@Autowired
    private NoticeboardRepository noticeboardRepository;
	@Autowired
    private CommentboardRepository commentboardRepository;

	@Test
	void contextLoads() {
		
		Noticeboard notice1 = new Noticeboard();
		notice1.setNoticeTitle("sbb가 무엇인가요?");
		notice1.setNoticeContent("sbb에 대해서 알고 싶습니다.");
		notice1.setNoticeDate(LocalDateTime.now());
        this.noticeboardRepository.save(notice1);  // 첫번째 게시판 저장
        
        Noticeboard notice2 = new Noticeboard();
		notice2.setNoticeTitle("sbb가 무엇인가요?");
		notice2.setNoticeContent("sbb에 대해서 알고 싶습니다.");
		notice2.setNoticeDate(LocalDateTime.now());
        this.noticeboardRepository.save(notice2);  // 두번째 게시판 저장

        Commentboard  comment1 = new Commentboard ();
        comment1.setCommentContent("id는 자동으로 생성되나요?");
        comment1.setNoticeboard(notice1);
        comment1.setCommentDate(LocalDateTime.now());
        this.commentboardRepository.save(comment1);  // 첫번째 게시판의 첫번째 댓글 저장
        
        Commentboard  comment2 = new Commentboard ();
        comment2.setCommentContent("스프링부트 짜증");
        comment2.setNoticeboard(notice1);
        comment2.setCommentDate(LocalDateTime.now());
        this.commentboardRepository.save(comment2);  // 첫번째 게시판의 두번째 댓글 저장
	}
}
