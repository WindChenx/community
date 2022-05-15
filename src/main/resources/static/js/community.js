function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    console.log(content);

    $.ajax({
        type: "post",
        url: "/comment",
        contentType: 'application/json; charset=UTF-8',
        dataType: 'json',
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            if (response.code == 200) {
                $("#comment_section").hide();
            } else {
                console.log(response.messageerror);
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=b08882b70f0a6b6bf262&redirect_uri=http://localhost:8080/callback&scope=githubUserDTO&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
    });
}