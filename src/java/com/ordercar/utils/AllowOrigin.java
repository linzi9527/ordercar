package com.ordercar.utils;

import javax.servlet.http.HttpServletResponse;

public class AllowOrigin {

    public static void AllowOrigin(HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Access-Control-Allow-Origin","*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods","POST");
        httpServletResponse.setHeader("Access-Control-Allow-Methods","GET");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","Access-Control");
        httpServletResponse.setHeader("Allow","POST");
        httpServletResponse.setHeader("Allow","GET");
    }
}
