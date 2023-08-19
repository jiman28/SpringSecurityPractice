package com.ji.user;

import org.hibernate.validator.constraints.Length;

//  PasswordForm을 생성하여 새로운 비밀번호를 받아올 준비를 한다.
public class PasswordForm {
	
	private String oldPassword;
	
	@Length(min = 3, max =20)
    private String newPassword;

//    @Length(min = 3, max =50)
    private String newPasswordConfirm;
    
    // Getters and setters...

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}

	public void setNewPasswordConfirm(String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}

}
