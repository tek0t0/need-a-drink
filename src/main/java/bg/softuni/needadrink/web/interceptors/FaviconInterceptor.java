package bg.softuni.needadrink.web.interceptors;

import bg.softuni.needadrink.util.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class FaviconInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) {

        String favicon = Constants.FAV_ICON_URL;

        if (modelAndView != null) {
            modelAndView.addObject("favicon", favicon);
        }
    }
}
