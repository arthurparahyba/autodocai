package com.autodoc.ai.project.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class HTMXInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView == null) {
            return;
        }
        final String originalViewName = modelAndView.getViewName();
        if(originalViewName.startsWith("redirect")) {
            return;
        }

        if ("true".equals(request.getHeader("HX-Request"))) {
            modelAndView.setViewName(originalViewName + " :: content");
        }
    }
}
