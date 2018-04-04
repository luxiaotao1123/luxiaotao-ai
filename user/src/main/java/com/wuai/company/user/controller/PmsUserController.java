package com.wuai.company.user.controller;

import com.wuai.company.user.service.UserService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.wuai.company.util.JwtToken.ID;

/**
 * Created by 97947 on 2017/9/22.
 * 运营管理系统
 */
@RestController
@RequestMapping("pms/login")
public class PmsUserController {
    @Autowired
    private UserService userService;

    /**
     * <p>
     * <span>API说明：<a style="color:blue">运营管理系统 登录</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/login/login/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param name 用户名
     * @param password 密码
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
    @RequestMapping("login/load/{name}")
    public Response pmsLogin(@PathVariable("name") String name, String password) {

        return userService.pmsLogin(name, password);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">管理员添加用户</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/login/register/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param username 用户名
     * @param password 密码
     * @param grade 等级（选填 default=2）
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
    @RequestMapping("register/load/{name}")
    public Response pmsRegister(@PathVariable("name") String name, String username, String password, @RequestParam(defaultValue = "2") Integer grade) {

        return userService.pmsRegister(name, username,  password, grade);
    }
    /**
     * <p>
     * <span>API说明：<a style="color:blue">修改密码</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/login/update/pass/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param name
     * @param password
     *
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
    @RequestMapping("update/pass/{name}")
    public Response pmsUpdatePass(@PathVariable("name") String name,String password) {

        return userService.pmsUpdatePass(name,password);
    }

}
