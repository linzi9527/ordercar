package com.ordercar.utils;

import javax.servlet.http.HttpServletResponse;

public class AllowOrigin {

    public static void AllowOrigin(HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods","POST,GET");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","*");
        //httpServletResponse.setHeader("Allow","POST");
        //httpServletResponse.setHeader("Allow","GET");
        //httpServletResponse.setContentType("application/json");
        //httpServletResponse.setCharacterEncoding("UTF-8");
        //httpServletResponse.setContentType("text/html;charset=UTF-8");
    }
}
