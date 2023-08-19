package com.ji.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ji.dbs.Commentboard;
import com.ji.dbs.NoticeWriteForm;
import com.ji.dbs.Noticeboard;
import com.ji.service.CommentService;
import com.ji.service.NoticeService;
import com.ji.service.UserService;
import com.ji.user.PasswordForm;
import com.ji.user.SiteUser;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pokemon")
public class PokemonMainController {

    private final NoticeService noticeService;
    private final CommentService commentService;
    private final UserService userService;	// 생성자 추가
    
    @Autowired
    public PokemonMainController(NoticeService noticeService, CommentService commentService, UserService userService) {
		this.noticeService = noticeService;
		this.commentService = commentService;
		this.userService = userService;
	}
    
    Logger logger = LoggerFactory.getLogger("com.ji.controller.PokemonMainController");

    @RequestMapping(value = {"/home", "/"})
//    @GetMapping("/home")
    public String viewHome(Principal principal, Model model) {
    	if(principal == null) {
    		model.addAttribute("member", "");
    	}else {
    		SiteUser siteUser = this.userService.getUser(principal.getName());
    		model.addAttribute("member", siteUser);
    	}
        return "home";
    }
    
    @RequestMapping(value = {"/kanto", "/"})
//    @GetMapping("/kanto")
    public String viewKanto() {
        return "kanto";
    }
    
    @RequestMapping(value = {"/jhoto", "/"})
//    @GetMapping("/jhoto")
    public String viewJhoto() {
        return "jhoto";
    }
    
    // 게시판 보기 (모든 게시물 출력)
//    @RequestMapping(value = {"/noticeboard", "/"})
//    public String list(Model model) {
//        List<Noticeboard> noticeList = noticeService.getNoticeList();
//        model.addAttribute("notice_list", noticeList);
//        return "noticeboard";
//    }

//    // paging 1 : 스프링 자체에서 제공하는 page 사용
//    @RequestMapping(value = {"/noticeboard", "/"})
//    public String list(Model model, @RequestParam(required = false, defaultValue = "0", value = "page") int page) {
//
//        Page<Noticeboard> listPage = noticeService.list(page); // 불러올 페이지의 데이터 1페이지는 0부터 시작
//
//        int totalPage = listPage.getTotalPages(); // 총 페이지 수
//
//        model.addAttribute("notice_list", listPage.getContent());
//        model.addAttribute("totalPage", totalPage);
//
//        return "noticeboard";
//    }

//    // paging 2 : JPA 제공 pageable 사용
//    @RequestMapping(value = {"/noticeboard", "/"})
//    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
//        Page<Noticeboard> paging = this.noticeService.getList(page);
//        model.addAttribute("paging", paging);
//        return "noticeboard";
//    }
    
//    // paging 에 검색 기능 추가1 : JPA + raw query
//    @RequestMapping(value = {"/noticeboard", "/"})
////    @GetMapping("/noticeboard")
//    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
//    								@RequestParam(value = "kw", defaultValue = "") String kw) {
//        Page<Noticeboard> paging = this.noticeService.getList(page, kw);
//        model.addAttribute("paging", paging);
//        model.addAttribute("kw", kw);
//        return "noticeboard";
//    }
    
 // paging 에 검색 기능 추가2 : 상세 검색
    @RequestMapping(value = {"/noticeboard", "/"})
//    @GetMapping("/noticeboard")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
    								@RequestParam(value = "kw", defaultValue = "") String kw,
    								@RequestParam(value = "cs", defaultValue = "") String cs) {
        Page<Noticeboard> paging = this.noticeService.getList(page, kw, cs);
        
        logger.info("paging.getTotalElements()=======>"+paging.getTotalElements());
        model.addAttribute("paging", paging);
        if (paging.getTotalElements() <= 0) {
			model.addAttribute("Retrieved", "");
		}else {
			model.addAttribute("Retrieved", "1");
		}
        model.addAttribute("kw", kw);
        model.addAttribute("cs", cs);
        return "noticeboard";
    }

//    // 게시물 작성 페이지 이동
//    @PreAuthorize("isAuthenticated()")
//    @RequestMapping(value = "/writeNotice", method = RequestMethod.GET)
//    public String writeNotice() {
//        return "noticewrite";
//    }
//    
//    // 게시물 추가
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/addNotice")
//    public String addNotice(@RequestParam(value = "notice_title") String noticeTitle,
//                            @RequestParam(value = "notice_content") String noticeContent,
//                            Principal principal) {
//    	SiteUser siteUser = this.userService.getUser(principal.getName());
//        noticeService.addNotice(noticeTitle, noticeContent, siteUser);
//        
//        return "redirect:/pokemon/noticeboard";
//    }
    
    // 게시물 작성 페이지 이동 ~ form 형식으로 교체 : 에러메시지를 나오게 하기 위해서
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/addNotice", method = RequestMethod.GET)
    public String writeNotice(NoticeWriteForm noticeWriteForm) {
        return "form_noticewrite";
    }
    
    // 게시물 추가 ~ form 형식으로 교체
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addNotice")
    public String addNotice(Principal principal, @Valid NoticeWriteForm noticeWriteForm,  BindingResult bindingResult) {
    	if (bindingResult.hasErrors()) {
            return "form_noticewrite";
        }
    	
    	SiteUser siteUser = this.userService.getUser(principal.getName());

        noticeService.addNotice(noticeWriteForm.getWriteTitle(), noticeWriteForm.getWriteContent(), siteUser);
        
        return "redirect:/pokemon/noticeboard";
    }
    
//    // 단일 게시물 내용 보기 (상세 페이지)
//    @GetMapping("/viewNotice/{no}") // 상세 페이지
//    public String viewNotice(@PathVariable Integer no, Model model) {
//        Noticeboard notice = noticeService.viewNotice(no);
//        List<Commentboard> commentList = commentService.listComments(no);
//        model.addAttribute("notice", notice);
//        model.addAttribute("comment_list", commentList);
//        
//        logger.info("commentList.getTotalElements()=======>"+ commentList);
//        if (commentList.size() == 0) {
//			model.addAttribute("Retrieved", "");
//		}else {
//			model.addAttribute("Retrieved", "1");
//		}
//        
//        return "noticeview";
//    }

    // 단일 게시물 내용 보기 (상세 페이지 + 진입시 조회수 상승 + 조회수 중복 방지(쿠키사용))
    @GetMapping("/viewNotice/{no}") // 상세 페이지
    public String viewNotice(@PathVariable Integer no, Model model,
    						 HttpServletRequest request, HttpServletResponse response) {
        Noticeboard notice = noticeService.viewNotice(no);
        List<Commentboard> commentList = commentService.listComments(no);
        model.addAttribute("notice", notice);
        model.addAttribute("comment_list", commentList);
        
        logger.info("commentList.getTotalElements()=======>"+ commentList);
        if (commentList.size() == 0) {
			model.addAttribute("Retrieved", "");
		}else {
			model.addAttribute("Retrieved", "1");
		}
        
        // 조회수 중복 방지를 위한 쿠키 설정
        Cookie oldCookie = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("countNoticeView")) {	// 같은 이름의 쿠키가 있는 지 확인
					oldCookie = cookie;
				}
			}
		}
		
		if (oldCookie != null) {
			if (!oldCookie.getValue().contains("["+ no.toString() +"]")) {
				this.noticeService.updateNoticeView(no);
				oldCookie.setValue(oldCookie.getValue() + "_[" + no + "]");
				oldCookie.setPath("/");		// 특별한 경우가 아니라면 건드리지 않는다
//				oldCookie.setMaxAge(60 * 60 * 24);	// 쿠키 시간 24시간
				oldCookie.setMaxAge(60 * 10);		// 쿠키 시간 10분 ~ 쿠키 유지 시간은 1초 기준으로 한다
				response.addCookie(oldCookie);
			}
		} else {
			this.noticeService.updateNoticeView(no);
			Cookie newCookie = new Cookie("countNoticeView", "[" + no + "]");	// 이때 해당 쿠키의 이름을 선언
			newCookie.setPath("/");		// 특별한 경우가 아니라면 건드리지 않는다
			newCookie.setMaxAge(60 * 10);		// 쿠키 시간 10분
			response.addCookie(newCookie);
		}
        
        return "noticeview";
    }
    
    // 게시물 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/editNotice")
    public String editNotice(@RequestParam Integer noticeNo,
                             @RequestParam String notice_title,
                             @RequestParam String notice_content) throws Exception {
        noticeService.editNotice(noticeNo, notice_title, notice_content);

        return "redirect:/pokemon/viewNotice/" + noticeNo; // noticeNo를 경로에 직접 포함시킴 (절대 경로)
    }
    
    // 게시물 수정 취소
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cancelEditNotice")
	public String cancelEditNotice(String noticeNo) throws Exception {

		return "redirect:viewNotice/" + noticeNo; // 방법2 : 상대 경로
	}

    // 게시물 삭제
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/removeNotice")
    public String removeNotice(@RequestParam Integer noticeNo) throws Exception {

        noticeService.removeNotice(noticeNo);
        
        return "redirect:/pokemon/noticeboard";
    }
    
    // 게시물 추천
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/likeNotice")
    public String likeNotice(@RequestParam Integer noticeNo, Principal principal) {
    	Noticeboard notice = this.noticeService.getNotice(noticeNo);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.noticeService.likeNotice(notice, siteUser);
        return "redirect:/pokemon/viewNotice/" + noticeNo; // noticeNo를 경로에 직접 포함시킴 (절대 경로)
    }
    
    // 게시물 비추천
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/dislikeNotice")
    public String dislikeNotice(@RequestParam Integer noticeNo, Principal principal) {
    	Noticeboard notice = this.noticeService.getNotice(noticeNo);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.noticeService.dislikeNotice(notice, siteUser);
        return "redirect:/pokemon/viewNotice/" + noticeNo; // noticeNo를 경로에 직접 포함시킴 (절대 경로)
    }
    
    // 댓글 추가
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addComment")
    public String addComment(@RequestParam(value = "nwcn") Integer noticeNo,
                             @RequestParam(value = "comment_write") String commentContent,
                             Principal principal) throws Exception {
    	SiteUser siteUser = this.userService.getUser(principal.getName());
        commentService.addComment(noticeNo, commentContent, siteUser);

        return "redirect:/pokemon/viewNotice/" + noticeNo; // noticeNo를 경로에 직접 포함시킴 (절대 경로)
    }
    
    // 댓글 삭제
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/removeComment")
    public String removeComment(@RequestParam(value = "ndcn") Integer noticeNo,
            					@RequestParam(value = "dcn") Integer commentNo) throws Exception {
    	commentService.removeComment(commentNo);

    	return "redirect:/pokemon/viewNotice/" + noticeNo; // noticeNo를 경로에 직접 포함시킴 (절대 경로)
    }
    
    // 댓글 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/editComment")
    public String editComment(@RequestParam(value = "cedit") String commentContent,
    						  @RequestParam(value = "noticeNo") Integer noticeNo,
            				  @RequestParam(value = "ecn") Integer commentNo) throws Exception {
    	commentService.editComment(commentContent, commentNo);

//    	return "redirect:/pokemon/viewNotice/" + noticeNo; // noticeNo를 경로에 직접 포함시킴 (절대 경로)
//    	return String.format("redirect:/pokemon/viewNotice/%s", noticeNo);	// String.format 을 이용한 redirect
        return String.format("redirect:/pokemon/viewNotice/%s#comment_%s", noticeNo, commentNo);	// 앵커태그 추가
    }
    
//    // 댓글 수정 취소
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/cancelEditComment")
//	public String cancelEditComment(String noticeNo) throws Exception {
//
////    	redirectAttr.addAttribute("no", noticeNo);	// @PathVariable 를 사용할 경우 먹히지 않는다.
////		return "redirect:/pokemon/viewNotice/" + noticeNo; // 방법1 : 절대 경로
//		return "redirect:viewNotice/" + noticeNo; // 방법2 : 상대 경로
//	}
    
    // 댓글 수정 취소 = 앵커태그 추가를 위해 매개변수 수정 + js 함수 변경
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cancelEditComment")
	public String cancelEditComment(@RequestParam(value = "nccn") Integer noticeNo,
									@RequestParam(value = "ccn") Integer commentNo) throws Exception {

//    	redirectAttr.addAttribute("no", noticeNo);	// @PathVariable 를 사용할 경우 먹히지 않는다.
//		return "redirect:/pokemon/viewNotice/" + noticeNo; // 방법1 : 절대 경로
//		return "redirect:viewNotice/" + noticeNo; // 방법2 : 상대 경로
		return String.format("redirect:/pokemon/viewNotice/%s#comment_%s", noticeNo, commentNo);	// 앵커태그 추가
	}

}
