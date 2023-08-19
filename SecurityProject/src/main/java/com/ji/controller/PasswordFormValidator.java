//package com.ji.password;
//
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
////  Password와 PasswordConfirm값이 일치하는지 확인해주는 PasswordFormValidator를 생성
//public class PasswordFormValidator implements Validator {
//	
//	public boolean supports(Class<?> clazz) {
//        return PasswordForm.class.isAssignableFrom(clazz);
//    }
//
//    public void validate(Object target, Errors errors) {
//        PasswordForm passwordForm = (PasswordForm) target;
//        if(!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordConfirm())){
//            errors.rejectValue("newPassword", "wrong value", "입력한 새 패스워드가 일치하지 않습니다.");
//        }
//    }
//}
