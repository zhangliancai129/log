package com.example.logback.filter;


import com.example.logback.util.TraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*", filterName = "logMDCFilter")
public class LogMDCFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogMDCFilter.class);
    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean putTraceId = this.putTraceId();
        boolean putSpanId = this.putSpanId();

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            if (putTraceId) {
                this.removeTraceId();
            }
            if (putSpanId) {
                this.removeSpanId();
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        LOGGER.info("logMDCFilter init...");
    }

    @Override
    public void destroy() {
        LOGGER.info("logMDCFilter destroy...");
    }


    private boolean putTraceId() {
        if (null == MDC.get(TRACE_ID)) {
            try {
                MDC.put(TRACE_ID, TraceUtil.generateId());
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    private void removeTraceId() {
        try {
            MDC.remove(TRACE_ID);
        } catch (Exception e) {
        }
    }

    private boolean putSpanId() {
        if (null == MDC.get(SPAN_ID)) {
            try {
                MDC.put(SPAN_ID, TraceUtil.generateId());
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    private void removeSpanId() {
        try {
            MDC.remove(SPAN_ID);
        } catch (Exception e) {
        }
    }
}
