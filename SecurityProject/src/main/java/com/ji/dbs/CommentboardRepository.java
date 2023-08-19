package com.ji.dbs;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentboardRepository extends JpaRepository<Commentboard, Integer> {

	// 댓글을 list 형태로 보기 
    List<Commentboard> findByNoticeboard_NoticeNo(Integer noticeNo);
}
