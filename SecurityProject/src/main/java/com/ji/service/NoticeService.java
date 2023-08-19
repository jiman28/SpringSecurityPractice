package com.ji.service;

import java.util.Optional;

//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ji.controller.DataNotFoundException;
import com.ji.dbs.Noticeboard;
import com.ji.dbs.NoticeboardRepository;
import com.ji.user.SiteUser;

@Service
public class NoticeService {

    private final NoticeboardRepository noticeboardRepository;

    @Autowired
    public NoticeService(NoticeboardRepository noticeboardRepository) {
        this.noticeboardRepository = noticeboardRepository;
    }
    
//    // 게시물 검색1 : 메소드 추가 (JPA 자체 지원 Query 라고 생각하면 된다. = Specification
//    // 주의할 점을 메소드에 사용되는 table 이름들은 MySQL 이 아닌 JPA 의 Entity 클래스의 Field 값을 따라간다!
//    private Specification<Noticeboard> search(String kw) {
//        return new Specification<>() {
//            private static final long serialVersionUID = 1L;
//            @Override
//            public Predicate toPredicate(Root<Noticeboard> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                query.distinct(true);  // 중복을 제거 
//                Join<Noticeboard, SiteUser> u1 = q.join("author", JoinType.LEFT);			// 외래키 확인
//                Join<Noticeboard, Commentboard> a = q.join("commentList", JoinType.LEFT);	// 외래키 확인
//                Join<Commentboard, SiteUser> u2 = a.join("author", JoinType.LEFT);			// 외래키 확인
//                return cb.or(cb.like(q.get("noticeTitle"), "%" + kw + "%"),		// 게시물 제목 
//                        cb.like(q.get("noticeContent"), "%" + kw + "%"),		// 게시물 내용 
//                        cb.like(u1.get("username"), "%" + kw + "%"),			// 게시물 작성자 
//                        cb.like(a.get("commentContent"), "%" + kw + "%"),		// 댓글 내용 
//                        cb.like(u2.get("username"), "%" + kw + "%"));			// 댓글 작성자 
//            }
//        };
//    }

//    // 게시판 보기 (모든 게시물 출력)
//    public List<Noticeboard> getNoticeList() {
//        return noticeboardRepository.findAllByOrderByNoticeDateDesc();
//    }
    
//    // paging 1 : 스프링 자체에서 제공하는 page 사용
//    public Page<Noticeboard> list(int page) {
//    	return noticeboardRepository.findAll(PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "noticeNo")));
//    }

//    // paging 2 : JPA 제공 pageable 사용
//    public Page<Noticeboard> getList(int page) {
//        Pageable pageable = PageRequest.of(page, 5);
//        return this.noticeboardRepository.findAll(pageable);
//    }
    
//    // paging 에 검색 기능 추가1 : JPA
//    public Page<Noticeboard> getList(int page, String kw) {
//        Pageable pageable = PageRequest.of(page, 5);
//        Specification<Noticeboard> spec = search(kw);
//        return this.noticeboardRepository.findAll(spec, pageable);
//    }
    
//    // paging 에 검색 기능 추가2 : raw query
//    public Page<Noticeboard> getList(int page, String kw) {
//    	Pageable pageable = PageRequest.of(page, 5);
//    	return this.noticeboardRepository.findAllByKeyword(kw, pageable);
//    }
    
    // paging 에 검색 기능 추가3 : 상세 검색 추가 (raw query)
    public Page<Noticeboard> getList(int page, String kw, String cs) {
    	Pageable pageable = PageRequest.of(page, 5);
    	
    	Page<Noticeboard> p = this.noticeboardRepository.findAllByKeyword(kw, pageable);
    	if (cs.equals("all")) {
			p = this.noticeboardRepository.findAllByKeyword(kw, pageable);
    	} else if (cs.equals("nAuthor")) {
			p = this.noticeboardRepository.findAllByauthorKeyword(kw, pageable);
		} else if (cs.equals("nTitle")) {
			p = this.noticeboardRepository.findAllBynoticeTitleKeyword(kw, pageable);
		} else if (cs.equals("nContent")) {
			p = this.noticeboardRepository.findAllBynoticeContentKeyword(kw, pageable);
		}
		return p;
    }

    // 게시물 추가
    public void addNotice(String noticeTitle, String noticeContent, SiteUser author) {
        Noticeboard noticeboard = new Noticeboard();
        noticeboard.setNoticeTitle(noticeTitle);
        noticeboard.setNoticeContent(noticeContent);
        noticeboard.setAuthor(author);
        this.noticeboardRepository.save(noticeboard);
    }
    
    // 단일 게시물 내용 보기 (상세 페이지)
    public Noticeboard viewNotice(Integer noticeNo) {
        return noticeboardRepository.findById(noticeNo).orElse(null);
    }
    
    // 게시물 수정
    public void editNotice(Integer noticeNo, String noticeTitle, String noticeContent) {
    	Noticeboard notice = noticeboardRepository.findById(noticeNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notice number: " + noticeNo));
        notice.setNoticeTitle(noticeTitle);
        notice.setNoticeContent(noticeContent);
        noticeboardRepository.save(notice);
    }

    // 게시물 삭제
    public void removeNotice(Integer noticeNo) {
    	Noticeboard notice = noticeboardRepository.findById(noticeNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notice number: " + noticeNo));
    	noticeboardRepository.delete(notice);
    }
    
//    // 게시물 추천
//    public void likeNotice(Noticeboard noticeboard, SiteUser siteUser) {
//    	noticeboard.getLike().add(siteUser);
//        this.noticeboardRepository.save(noticeboard);
//    }
//    
//    // 게시물 비추천
//    public void dislikeNotice(Noticeboard noticeboard, SiteUser siteUser) {
//    	noticeboard.getDislike().add(siteUser);
//        this.noticeboardRepository.save(noticeboard);
//    }
    
    // 게시물 추천 + 재 클릭 시 추천 취소 : contains 를 통해 추천을 했는지 확인!
    public void likeNotice(Noticeboard noticeboard, SiteUser siteUser) {
    	if (noticeboard.getLike().contains(siteUser)) {
    		noticeboard.getLike().remove(siteUser);
    	}else {
    		noticeboard.getLike().add(siteUser);
    	}
        this.noticeboardRepository.save(noticeboard);
    }
    
    // 게시물 비추천 + 재 클릭 시 비추천 취소 : contains 를 통해 비추천을 했는지 확인!
    public void dislikeNotice(Noticeboard noticeboard, SiteUser siteUser) {
    	if (noticeboard.getDislike().contains(siteUser)) {
    		noticeboard.getDislike().remove(siteUser);
    	}else {
    		noticeboard.getDislike().add(siteUser);
    	}
        this.noticeboardRepository.save(noticeboard);
    }
    
    // noticeNo(PK) 로 게시물 조회
    public Noticeboard getNotice(Integer noticeNo) {  
        Optional<Noticeboard> notice = this.noticeboardRepository.findById(noticeNo);
        if (notice.isPresent()) {
            return notice.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    // 게시물 조회수
    // @Transactional은 클래스나 메서드에 붙여줄 경우, 해당 범위 내 메서드가 트랜잭션이 되도록 보장
    // 어떤 연산에 트랜잭션이 보장된다면, DB에서 의도치 않은 값이 저장되거나 조회되는 것을 막을 수 있다.
    @Transactional
	public int updateNoticeView(Integer noticeNo) {
		return this.noticeboardRepository.updateCountView(noticeNo);
	}
}
