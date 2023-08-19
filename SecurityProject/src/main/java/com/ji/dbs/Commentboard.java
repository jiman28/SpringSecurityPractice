package com.ji.dbs;

import java.time.LocalDateTime;
import java.util.Set;

import com.ji.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class Commentboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentNo;

    @ManyToOne
    @JoinColumn(name = "notice_no")
    private Noticeboard noticeboard;

    @Column(columnDefinition = "TEXT")
    private String commentContent;

    private LocalDateTime commentDate;
    
    // 엔티티가 데이터베이스에 저장되기 전에 실행되는 메서드
    // 엔티티가 영속화(Persistence)되기 전에 실행되는 메서드를 지정하는 annotation
    @PrePersist
    public void prePersist() {
    	commentDate = LocalDateTime.now();
    }
    
    // SiteUser 의 PK 를 FK 로 가져옴
    @ManyToOne
    private SiteUser author;
    
    @ManyToMany
    Set<SiteUser> like;
    
    @ManyToMany
    Set<SiteUser> dislike;
    
    // @Entity 는 SQL 에서 DB 위치만 특정해주면 알아서 table 을 자동 생성해준다.
    // table 이름은 @Entity 클래스의 이름에서 앞의 대문자만 소문자로 바꿔서 자동 생성된다.
    // name 을 특별히 선언하지 않으면 field 이름의 대문자가 '_소문자' 형태로 자동 생성
    // @태그를 적절히 사용함으로서 PK, FK, auto_increment 여부를 여기서 선언한다.
    
    // Getters and setters...
    // Getter, setter 는 Lombok 을 사용하면 자동 생성해주지만 사용하지 않으면 직접 만들어준다. 

	public Integer getCommentNo() {
		return commentNo;
	}

	public void setCommentNo(Integer commentNo) {
		this.commentNo = commentNo;
	}

	public Noticeboard getNoticeboard() {
		return noticeboard;
	}

	public void setNoticeboard(Noticeboard noticeboard) {
		this.noticeboard = noticeboard;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public LocalDateTime getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(LocalDateTime commentDate) {
		this.commentDate = commentDate;
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
	
    // You can also implement equals() and hashCode() here if needed.
}
