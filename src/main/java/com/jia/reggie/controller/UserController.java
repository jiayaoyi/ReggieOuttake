package com.jia.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jia.reggie.common.R;
import com.jia.reggie.entity.User;
import com.jia.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String userPhone = user.getPhone();
        if (StringUtils.isNotEmpty(userPhone)) {
            //生成随机验证码
            //String verifyCode = ValidateCodeUtils.generateValidateCode(4).toString();
            //开发用，验证码固定为1234
            String verifyCode = "1234";
            //验证
            //SMSUtils.sendMessage("瑞吉外卖","SMS_282785036",userPhone,verifyCode);
            session.setAttribute(userPhone, verifyCode);
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map userLogin, HttpSession session) {
        String userPhone = userLogin.get("phone").toString();
        String code = userLogin.get("code").toString();
        Object verifyCode = session.getAttribute(userPhone);
        if (verifyCode != null && verifyCode.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, userPhone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                User newUser = new User();
                newUser.setPhone(userPhone);
                newUser.setStatus(1);
                userService.save(newUser);
                session.setAttribute("user", newUser.getId());
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        } else {
            return R.error("手机号码或验证码错误，登录失败");
        }
    }

    /**
     * 登出方法
     *
     * @param request 请求
     * @return 登出成功消息
     */
    @PostMapping("loginout")
    public R<String> logout(HttpServletRequest request) {
        //清理session中保存的员工信息
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
