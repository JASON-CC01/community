package com.jason.comunication.comunication.controller;

import com.jason.comunication.comunication.dto.AccessTokenDTO;
import com.jason.comunication.comunication.dto.GitHupUser;
import com.jason.comunication.comunication.mapper.UserMapper;
import com.jason.comunication.comunication.model.User;
import com.jason.comunication.comunication.provider.GitHupProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHupProvider gitHupProvider;

    @Value("${githup.Redirect_uri}")
    private String Redirect_uri;

    @Value("${githup.Client_id}")
    private String Client_id;

    @Value("${githup.Client_secret}")
    private String Client_secret;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(value = "code")String code,
                           @RequestParam(value = "state")String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(Redirect_uri);
        accessTokenDTO.setClient_id(Client_id);
        accessTokenDTO.setClient_secret(Client_secret);
        accessTokenDTO.setState(state);
        String accessToken = gitHupProvider.getAccessToken(accessTokenDTO);
        GitHupUser githupUser = gitHupProvider.getUser(accessToken);
        if (githupUser != null)
        {
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githupUser.getName());
            user.setAccountId(String.valueOf(githupUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            request.getSession().setAttribute("uesr",githupUser);
            return "redirect:/";
        }else {
            return "redirect:/";
        }
        //System.out.println(user.getName());
    }
}
