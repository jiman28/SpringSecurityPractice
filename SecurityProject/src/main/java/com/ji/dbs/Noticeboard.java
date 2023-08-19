package com.ji.dbs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.ji.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;

@Entity
public class Noticeboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_no")
    private Integer noticeNo;
    
    @Column(length = 100)
    private String noticeTitle;

    @Column(columnDefinition = "TEXT")
    private String noticeContent;

    private LocalDateTime noticeDate;
    
    // 엔티티가 데이터베이스에 저장되기 전에 실행되는 메서드
    // 엔티티가 영속화(Persistence)되기 전에 실행되는 메서드를 지정하는 annotation
    @PrePersist
    public void prePersist() {
        noticeDate = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "noticeboard", cascade = CascadeType.REMOVE)
    private List<Commentboard> commentList;
    
    // SiteUser 의 PK 를 FK 로 가져옴
    @ManyToOne
    private SiteUser author;
    
    @ManyToMany
    Set<SiteUser> like;
    
    @ManyToMany
    Set<SiteUser> dislike;
    
    // 조회수의 기본 값을 0으로 지정, null 불가 처리
    @Column(columnDefinition = "integer default 0", nullable = false)
	private int countView;

    // @Entity 는 SQL 에서 DB 위치만 특정해주면 알아서 table 을 자동 생성해준다.
    // table 이름은 @Entity 클래스의 이름에서 앞의 대문자만 소문자로 바꿔서 자동 생성된다.
    // name 을 특별히 선언하지 않으면 field 이름의 대문자가 '_소문자' 형태로 자동 생성
    // @태그를 적절히 사용함으로서 PK, FK, auto_increment 여부를 여기서 선언한다.
    
    // Getters and setters...
    // Getter, setter 는 Lombok 을 사용하면 자동 생성해주지만 사용하지 않으면 직접 만들어준다. 

	public Integer getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(Integer noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public LocalDateTime getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(LocalDateTime noticeDate) {
		this.noticeDate = noticeDate;
	}

	public List<Commentboard> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Commentboard> commentList) {
		this.commentList = commentList;
	}

	public SiteUser getAuthor() {
		return author;
	}

	public void setAuthor(SiteUser author) {
		this.author = author;
	}

	public Set<SiteUser> getLike() {
		return like;
	}

	public void setLike(Set<SiteUser> like) {
		this.like = like;
	}

	public Set<SiteUser> getDislike() {
		return dislike;
	}

	public void setDislike(Set<SiteUser> dislike) {
		this.dislike = dislike;
	}

	public int getCountView() {
		return countView;
	}

	public void setCountView(int countView) {
		this.countView = countView;
	}

    // You can also implement equals() and hashCode() here if needed.
}
