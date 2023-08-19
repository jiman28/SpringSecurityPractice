package com.ji.dbs;

import jakarta.validation.constraints.NotEmpty;

public class NoticeWriteForm {
	
	// 회원가입을 위한 form 게시글을 쓰기 위한 Form
	// Form 양식이 있어야 에러메시지를 띄울 수 있는것 같다
	
	@NotEmpty(message = "제목은 필수항목입니다.")
	private String writeTitle;

	@NotEmpty(message = "내용은 필수항목입니다.")
	private String writeContent;

	public String getWriteTitle() {
		return writeTitle;
	}

	public void setWriteTitle(String writeTitle) {
		this.writeTitle = writeTitle;
	}

	public String getWriteContent() {
		return writeContent;
	}

	public void setWriteContent(String writeContent) {
		this.writeContent = writeContent;
	}

}