package com.wuai.company.order.controller;

import com.wuai.company.order.service.SceneService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.wuai.company.util.JwtToken.ID;

/**
 * Created by hyf on 2017/5/26.
 * 场景的controller
 */
@RestController
@RequestMapping("scene")
public class SceneController {

    @Autowired
    private SceneService sceneService;

    /**
     * <p>
     * <span>API说明：<a style="color:blue">场景的选择</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/scene/choose/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request 请求头
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("choose/auth")
    public Response choose(HttpServletRequest request) throws Exception {

        return sceneService.choose((Integer) request.getAttribute(ID));
    }
    @PostMapping("store/scene")
    public Response storeScene() {
        return sceneService.storeScene();
    }

}
