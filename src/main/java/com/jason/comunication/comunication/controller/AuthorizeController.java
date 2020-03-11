package com.jason.comunication.comunication.controller;

import com.jason.comunication.comunication.dto.AccessTokenDTO;
import com.jason.comunication.comunication.dto.GitHupUser;
import com.jason.comunication.comunication.provider.GitHupProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/callback")
    public String callback(@RequestParam(value = "code")String code,
                           @RequestParam(value = "state")String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(Redirect_uri);
        accessTokenDTO.setClient_id(Client_id);
        accessTokenDTO.setClient_secret(Client_secret);
        accessTokenDTO.setState(state);
        String accessToken = gitHupProvider.getAccessToken(accessTokenDTO);
        GitHupUser user = gitHupProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
