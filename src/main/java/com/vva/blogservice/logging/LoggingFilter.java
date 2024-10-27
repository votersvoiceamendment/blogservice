//package com.vva.blogservice.logging;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class LoggingFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // You can add initialization code if needed
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        // Log the incoming request
//        System.out.println("Incoming Request: " + req.getMethod() + " " + req.getRequestURI());
//        System.out.println("Request Headers: " + req.getHeaderNames());
//
//        // Proceed with the filter chain
//        chain.doFilter(request, response);
//
//        // Log the outgoing response headers (CORS headers)
//        System.out.println("Outgoing Response Headers: ");
//        res.getHeaderNames().forEach(headerName ->
//                System.out.println(headerName + ": " + res.getHeader(headerName))
//        );
//    }
//
//    @Override
//    public void destroy() {
//        // Cleanup resources if necessary
//    }
//}
