package com.wuai.company.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/deploy")
public class DeployManagerController {

    @Value("${qiniu.accessKey}")
    private  String accessKey ;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucketName}")
    private String bucketName;
    /**
     * 获取七牛的token
     *
     * @return
     */
    @GetMapping("/qiniu/token")
    public Response QiNiu() {
        JSONObject jsonObject = new JSONObject();

        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(bucketName);
        jsonObject.put("toKen",token);
        return Response.success(jsonObject);
    }


}
