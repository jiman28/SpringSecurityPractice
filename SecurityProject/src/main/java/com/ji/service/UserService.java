package com.ji.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ji.controller.DataNotFoundException;
import com.ji.user.SiteUser;
import com.ji.user.UserRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// 회원가입
	public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
	
	// 사용자 정보 조회
	public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
	
	// 비밀번호 변경
	public void updatePassword(SiteUser siteUser, String newPassword) {
		siteUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(siteUser); // detached인 상태인 객체(SiteUser)를 명시적으로 merge
    }
	
	// 회원 탈퇴하기
	public void withdrawSite(SiteUser siteUser) {
		siteUser.setStatus("n");	// status 에 n 을 줌으로서 탈퇴한 상태로 처리 (DB에는 존재한다!)
		userRepository.save(siteUser);
	}
	
}
