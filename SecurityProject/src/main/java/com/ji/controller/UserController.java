package com.ji.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
//import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//import com.ji.password.PasswordFormValidator;
import com.ji.service.UserService;
import com.ji.user.PasswordForm;
import com.ji.user.SiteUser;
import com.ji.user.UserCreateForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}
	
	Logger logger = LoggerFactory.getLogger("com.ji.controller.UserController");

	// 회원가입 페이지
	@GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "form_signup";
    }

	// 회원가입 폼
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
    	
    	logger.info("==============================================>signup : userCreateForm: " + userCreateForm);
    	logger.info("==============================================>signup : bindingResult: " + bindingResult);
    	
        if (bindingResult.hasErrors()) {
            return "form_signup";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "form_signup";
        }
        
        try {
        userService.create(userCreateForm.getUsername(), 
                userCreateForm.getEmail(), userCreateForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "form_signup";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "form_signup";
        }

//        return "redirect:/";
        return "home";	// 이렇게 직접 html 을 쏴줌으로서 해결가능하다
        
    }
    
    // 로그인
    @GetMapping("/login")
    public String login() {
        return "form_login";
    }
    
//    // passwordForm 데이터를 받을때 바인더를 설정
//    // passwordFormValidator는 Bean등록을 안했기 때문에 new로 객체 생성
//    @InitBinder("passwordForm")
//    public void initBinder(WebDataBinder webDataBinder){
//        webDataBinder.addValidators(new PasswordFormValidator());
//    }
    
    // Password 변경 페이지 : @InitBinder + Validator 를 사용하는 경우
    @GetMapping("/password")
    public String updatePasswordForm(Principal principal, Model model) {
        model.addAttribute(principal);
        model.addAttribute(new PasswordForm());
        return "form_password";
    }
    
//    // Password 변경하기
//    @PostMapping("/password")
//    public String updatePassword(Principal principal, @Valid PasswordForm passwordForm, Errors errors,
//                                 Model model){
//        // Password 변경 페이지로 redirect 후 에러메시지 송출
//        if(errors.hasErrors()){
//            model.addAttribute(principal);
//            return "password_form";
//        }
//        // 비밀번호 변경 성공
//        SiteUser siteUser = this.userService.getUser(principal.getName());
//        userService.updatePassword(siteUser, passwordForm.getNewPassword());
//        // 변경 후 로그아웃 (security 문제로 로그인이 된 상태에서 비밀번호 변경 후 문제가 생길 수 있기 때문)
//        return "redirect:/user/logout";
//    }
    
    // Password 변경하기 : passwordEncoder 를 직접 사용하는 경우
    @PostMapping("/password")
    public String updatePassword(Principal principal, @Valid PasswordForm passwordForm, Errors errors,
                                 Model model){
        // Password 변경 페이지로 redirect 후 에러메시지 송출
        if(errors.hasErrors()){
            model.addAttribute(principal);
            return "form_password";
        }
        
        SiteUser siteUser = this.userService.getUser(principal.getName());

        // 패스워드 일치 여부 검사
        if (!passwordEncoder.matches(passwordForm.getOldPassword(), siteUser.getPassword())) {
            errors.rejectValue("oldPassword", "passwordInCorrect", "기존 비밀번호가 맞지 않습니다.");
            return "form_password";
        }

        // 새로운 비밀번호와 확인 비밀번호 일치 여부 검사
        if (!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordConfirm())) {
            errors.rejectValue("newPasswordConfirm", "wrongPassword", "입력한 새 패스워드가 일치하지 않습니다.");
            return "form_password";
        }

        // 새로운 비밀번호와 이전 비밀번호가 같은지 확인
        if (passwordEncoder.matches(passwordForm.getNewPassword(), siteUser.getPassword())) {
            errors.rejectValue("newPassword", "samePassword", "입력한 새 패스워드가 이전 패스워드와 같으면 안됩니다.");
            return "form_password";
        }
        
        // 정상 작동 시 패스워드 변경
        userService.updatePassword(siteUser, passwordForm.getNewPassword());
        // 변경 후 로그아웃 (security 문제로 로그인이 된 상태에서 비밀번호 변경 후 문제가 생길 수 있기 때문)
        return "redirect:/user/logout";
    }
    
    // 회원 탈퇴 페이지
    @GetMapping("/withdraw")
    public String withdrawSiteForm(Principal principal, Model model) {
    	model.addAttribute(principal);	// principal 객체는 현재 인증된 사용자의 정보를 담고 있는 객체
        model.addAttribute(new PasswordForm());	// 비밀번호를 입력받기 위해 빈 PasswordForm 객체를 뷰로 전달
        return "form_withdraw";
    }
    
    // 회원 탈퇴하기
    @PostMapping("/withdraw")
    public String withdrawSite(Principal principal, @Valid PasswordForm passwordForm, Errors errors,
    						   Model model) {
    	if(errors.hasErrors()){		// errors 객체는 사용자의 입력값에 대한 유효성 검사를 수행한 후에 발생한 오류를 담고 있는 객체
            model.addAttribute(principal);	// 로그인한 사용자의 정보를 계속 사용하기 위해 오류가 발생한 경우에도 principal 객체를 뷰로 전달
            return "form_withdraw";
        }
    	
    	SiteUser siteUser = this.userService.getUser(principal.getName());

        // 패스워드 일치 여부 검사 (유효성 검사)
        if (!passwordEncoder.matches(passwordForm.getOldPassword(), siteUser.getPassword())) {
            errors.rejectValue("oldPassword", "passwordInCorrect", "비밀번호가 맞지 않습니다.");
            return "form_withdraw";
        }
        
        // 정상 작동 시 패스워드 변경
        userService.withdrawSite(siteUser);
        // 변경 후 로그아웃 (security 문제로 로그인이 된 상태에서 비밀번호 변경 후 문제가 생길 수 있기 때문)
        return "redirect:/user/logout";
    }
    
}
