package com.wuai.company.user.EaseMob.api.impl;


import com.wuai.company.user.EaseMob.TokenUtil;
import com.wuai.company.user.EaseMob.api.AuthTokenAPI;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class EasemobAuthToken implements AuthTokenAPI {

	@Override
	public Object getAuthToken(){
		return TokenUtil.getAccessToken();
	}
}
