package com.cd.uap.jwt;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cd.uap.pojo.CacheMgr;

/**
 * JWT认证令牌过滤器
 *
 */
public class JWTAuthenticationFilter extends OncePerRequestFilter {
	private static Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    @Value("${jwt.header}")
    private String token_header;

    @Resource
    private JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String auth_token = request.getHeader(this.token_header);
        final String auth_token_start = "Bearer ";
        if ((!StringUtils.isEmpty(auth_token)) && auth_token.startsWith(auth_token_start)) {
            auth_token = auth_token.substring(auth_token_start.length());
        } else {
            // 不按规范,不允许通过验证
            auth_token = null;
        }

        String username = jwtUtils.getUsernameFromToken(auth_token);
        log.info(String.format("Checking authentication for user %s.", username));

        //获取已登录的token信息
        CacheMgr cm=CacheMgr.getInstance();
        Object token = cm.getValue(username);
        //已登出，或者重新登录过token已改变，auth_token无效
        if(token==null||!token.equals(auth_token)) {
        	 auth_token = null;
        	 username = null;
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
            // UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            UserDetails userDetails = jwtUtils.getUserFromToken(auth_token);

            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
            // the database compellingly. Again it's up to you ;)
            if (jwtUtils.validateToken(auth_token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info(String.format("Authenticated user %s, setting security context", username));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

}
