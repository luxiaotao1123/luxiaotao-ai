package com.wuai.company.user.EaseMob.api.impl;


import com.wuai.company.user.EaseMob.EasemobAPI;
import com.wuai.company.user.EaseMob.OrgInfo;
import com.wuai.company.user.EaseMob.ResponseHandler;
import com.wuai.company.user.EaseMob.TokenUtil;
import com.wuai.company.user.EaseMob.api.ChatMessageAPI;
import io.swagger.client.ApiException;
import io.swagger.client.api.ChatHistoryApi;
import org.springframework.stereotype.Component;


@Component
public class EasemobChatMessage  implements ChatMessageAPI {

    private ResponseHandler responseHandler = new ResponseHandler();
    private ChatHistoryApi api = new ChatHistoryApi();

    @Override
    public Object exportChatMessages(final Long limit,final String cursor,final String query) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatmessagesGet(OrgInfo.ORG_NAME,OrgInfo.APP_NAME, TokenUtil.getAccessToken(),query,limit+"",cursor);
            }
        });
    }

    @Override
    public Object exportChatMessages(final String timeStr) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatmessagesTimeGet(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(),timeStr);
            }
        });
    }
}
