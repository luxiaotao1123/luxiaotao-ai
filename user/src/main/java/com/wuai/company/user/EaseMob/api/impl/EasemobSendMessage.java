package com.wuai.company.user.EaseMob.api.impl;


import com.wuai.company.user.EaseMob.EasemobAPI;
import com.wuai.company.user.EaseMob.OrgInfo;
import com.wuai.company.user.EaseMob.ResponseHandler;
import com.wuai.company.user.EaseMob.TokenUtil;
import com.wuai.company.user.EaseMob.api.SendMessageAPI;
import io.swagger.client.ApiException;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;
import org.springframework.stereotype.Component;

@Component
public class EasemobSendMessage implements SendMessageAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameMessagesPost(OrgInfo.ORG_NAME,OrgInfo.APP_NAME, TokenUtil.getAccessToken(), (Msg) payload);
            }
        });
    }
}
