package com.jason.comunication.comunication.provider;

import com.alibaba.fastjson.JSON;
import com.jason.comunication.comunication.dto.AccessTokenDTO;
import com.jason.comunication.comunication.dto.GitHupUser;
import okhttp3.*;
import org.springframework.stereotype.Component;


import java.io.IOException;


@Component
public class GitHupProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType medieType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(medieType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println(string);
            String token = string.split("&")[0].split("=")[1];
            System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public GitHupUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder()
                    .url("https://api.github.com/user?access_token="+accessToken)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                GitHupUser gitHupUser = JSON.parseObject(string,GitHupUser.class);
                return gitHupUser;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
    }
}
