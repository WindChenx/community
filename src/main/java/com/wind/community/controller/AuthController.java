package com.wind.community.controller;

import com.wind.community.dao.AccessTokenDao;
import com.wind.community.dao.GithubUserDao;
import com.wind.community.mapper.UserMapper;
import com.wind.community.model.User;
import com.wind.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.nio.cs.US_ASCII;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state, HttpServletRequest request) {
        AccessTokenDao accessTokenDTO = new AccessTokenDao();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println(accessToken);
        if (accessToken != null) {
            GithubUserDao githubUserDao = githubProvider.getUser(accessToken);
            if (githubUserDao != null) {
                User user = new User();
                user.setToken(UUID.randomUUID().toString());
                user.setName(githubUserDao.getLogin());
                user.setAccountId(String.valueOf(githubUserDao.getId()));
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                userMapper.insert(user);
                request.getSession().setAttribute("user", githubUserDao);
                return "redirect:/";
            }
        }
        return "redirect:/";
    }
}
