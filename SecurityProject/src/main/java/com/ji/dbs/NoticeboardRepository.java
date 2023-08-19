package com.ji.dbs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeboardRepository extends JpaRepository<Noticeboard, Integer> {
	
	// 서비스에서 Sort.Direction.DESC 으로 순서를 정했기 때문에 이제 사용할 필요가 없다.
	// 게시판을 list 형태로 게시 날짜 순서로 보기 
	
//	// 단순 게시판 리스트
//	List<Noticeboard> findAllByOrderByNoticeDateDesc();
	
//	// paging 2 : JPA 제공 pageable 사용
//	Page<Noticeboard> findAll(Pageable pageable);
	
//	// 게시물 검색1 (JPA)
//	Page<Noticeboard> findAll(Specification<Noticeboard> spec, Pageable pageable);
	
	// 게시물 검색2 + 3 = 전체 검색 (raw query)
	@Query("select "
            + "distinct q "
            + "from Noticeboard q " 
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Commentboard a on a.noticeboard=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.noticeTitle like %:kw% "
            + "   or q.noticeContent like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.commentContent like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Noticeboard> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
	
	// 게시물 검색3 (상세 검색 = 사용자)
	@Query("select   "
			+ "distinct q "
			+ "from Noticeboard q "
			+ "left outer join SiteUser u1 on q.author=u1 "
			+ "where "
			+ "u1.username like concat('%',?1,'%')"
			)
	Page<Noticeboard> findAllByauthorKeyword(String kw, Pageable pageable);
	
	// 게시물 검색3 (상세 검색 = 제목)
	@Query("select   "
			+ "  n "
			+ "from Noticeboard n " 
			+ "where "
			+ "noticeTitle like concat('%',?1,'%')"	
			)
	Page<Noticeboard> findAllBynoticeTitleKeyword(String kw, Pageable pageable);
	
	// 게시물 검색3 (상세 검색 = 내용)
	@Query("select   "
			+ "  n "
			+ "from Noticeboard n " 
			+ "where "
			+ "noticeContent like concat('%',?1,'%')"	
			)
	Page<Noticeboard> findAllBynoticeContentKeyword(String kw, Pageable pageable);
	
	// 게시물 조회수
	// 삽입(Insert), 수정(Update), 삭제(Delete) 등 데이터의 변경이 있는 쿼리 사용시 @Modifying 를 무조건 사용한다
	// 쿼리 해석 : 'no = noticeNo' 일 경우 countView 에 +1 하라
	@Modifying
	@Query("update Noticeboard q set q.countView = q.countView + 1 where q.noticeNo = :no")
	int updateCountView(@Param("no") Integer noticeNo);
	
}
