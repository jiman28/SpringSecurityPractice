
// thymeleaf 문법을 사용하지 않는다!!

function toHome(frm) {
    frm.action = "/pokemon/home"
    frm.submit();
}

function toViewNotice(frm) {
    frm.action = "/pokemon/cancelEditNotice";
    frm.submit();
}

function toNoticeboard(frm) {
    frm.action = "/pokemon/noticeboard";
    frm.submit();
}
function fn_enable() {
    document.querySelector('#n_title').disabled = false;
    document.querySelector('#n_content').disabled = false;
    document.querySelector('#tr_btn').style.display = 'none';
    document.querySelector('#tr_btn_modify').style.display = 'table-row';
}

function fn_modify_notice() {
    let noticeViewForm = document.noticeViewForm;
    let title = noticeViewForm.notice_title.value;
    let content = noticeViewForm.notice_content.value;

    if (!title || !content) {
        alert("게시물 제목과 내용은 필수항목입니다.");
    } else {
        noticeViewForm.action = "/pokemon/editNotice";
        noticeViewForm.submit();
    }
}

function fn_remove_notice(frm, event) {
    if (confirm("게시글을 정말 삭제하시겠습니까?")) {
        // 사용자가 확인을 눌렀을 때
        frm.action = "/pokemon/removeNotice";
        frm.submit();
    } else {
        // 사용자가 취소를 눌렀을 때 폼 제출을 취소함
        event.preventDefault();
    }
}

function add_comment() {
    let noticeViewForm = document.noticeViewForm;
    let content = noticeViewForm.comment_write.value;

    if (!content) {
        alert("댓글 내용은 필수항목입니다.");
    } else {
        noticeViewForm.action = "/pokemon/addComment";
        noticeViewForm.submit();
    }
}

function cm_enable(elm) {
    elm.parentElement.previousElementSibling.firstElementChild.disabled = false;
    elm.parentElement.style.display = 'none';
    elm.parentElement.nextElementSibling.style.display = 'table-row';
}

function cm_cancel_comment(url, nno, cno) {
    let form = document.createElement("form");
    form.setAttribute("method", "post");
    form.setAttribute("action", url);

    let input2 = document.createElement("input");
    input2.setAttribute("type", "hidden")
    input2.setAttribute("name", "nccn")
    input2.setAttribute("value", nno)
    form.appendChild(input2);

    let input3 = document.createElement("input");
    input3.setAttribute("type", "hidden")
    input3.setAttribute("name", "ccn")
    input3.setAttribute("value", cno)
    form.appendChild(input3);

    // html 의 form 안의 input 태그의 csrf 토큰의 value값을 가져온다 
    let csrfTokenInput = document.querySelector('input[name="_csrf"]');
    let csrfToken = csrfTokenInput.value;

    // CSRF 토큰을 폼 데이터에 추가한다
    let input4 = document.createElement("input");
    input4.setAttribute("type", "hidden")
    input4.setAttribute("name", "_csrf")
    input4.setAttribute("value", csrfToken)
    form.appendChild(input4);

    document.body.appendChild(form);

    form.submit();
}

function cm_modify_comment(url, cont, nno, cno) {
    if (!cont) {
        alert("댓글 내용은 필수항목입니다.");
    } else {
        let form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", url);

        let input1 = document.createElement("input");
        input1.setAttribute("type", "hidden")
        input1.setAttribute("name", "cedit")
        input1.setAttribute("value", cont)
        form.appendChild(input1);

        let input2 = document.createElement("input");
        input2.setAttribute("type", "hidden")
        input2.setAttribute("name", "noticeNo")
        input2.setAttribute("value", nno)
        form.appendChild(input2);

        let input3 = document.createElement("input");
        input3.setAttribute("type", "hidden")
        input3.setAttribute("name", "ecn")
        input3.setAttribute("value", cno)
        form.appendChild(input3);


        let csrfTokenInput = document.querySelector('input[name="_csrf"]');
        let csrfToken = csrfTokenInput.value;

        let input4 = document.createElement("input");
        input4.setAttribute("type", "hidden")
        input4.setAttribute("name", "_csrf")
        input4.setAttribute("value", csrfToken)
        form.appendChild(input4);

        document.body.appendChild(form);

        form.submit();
    }
}

function cm_remove_comment(url, nno, cno, event) {
    if (confirm("댓글을 정말 삭제하시겠습니까?")) {
        let form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", url);

        let input2 = document.createElement("input");
        input2.setAttribute("type", "hidden")
        input2.setAttribute("name", "ndcn")
        input2.setAttribute("value", nno)
        form.appendChild(input2);

        let input3 = document.createElement("input");
        input3.setAttribute("type", "hidden")
        input3.setAttribute("name", "dcn")
        input3.setAttribute("value", cno)
        form.appendChild(input3);

        let csrfTokenInput = document.querySelector('input[name="_csrf"]');
        let csrfToken = csrfTokenInput.value;

        let input4 = document.createElement("input");
        input4.setAttribute("type", "hidden")
        input4.setAttribute("name", "_csrf")
        input4.setAttribute("value", csrfToken)
        form.appendChild(input4);

        document.body.appendChild(form);

        form.submit();
    } else {
        // 사용자가 취소를 눌렀을 때 폼 제출을 취소함
        event.preventDefault();
    }

}

function likeThisNotice(frm) {
    frm.action = "/pokemon/likeNotice";
    frm.submit();
}

function dislikeThisNotice(frm) {
    frm.action = "/pokemon/dislikeNotice";
    frm.submit();
}
