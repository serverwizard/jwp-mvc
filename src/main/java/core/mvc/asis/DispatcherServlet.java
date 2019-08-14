package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.BasicHandlerMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private BasicHandlerMapping handlerMapping;

    @Override
    public void init() {
        handlerMapping = new BasicHandlerMapping();

        handlerMapping.addMapping(new RequestMapping());
        handlerMapping.addMapping(new AnnotationHandlerMapping("next"));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            ModelAndView modelAndView = handlerMapping.find(req, resp);
            modelAndView.getView().render(null, req, resp);

            if(modelAndView == null){
                throw new ServletException("존재하지 않는 페이지 입니다.");
            }
        }catch (Throwable e){
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

}
