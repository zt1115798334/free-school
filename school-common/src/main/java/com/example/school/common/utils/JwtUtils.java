package com.example.school.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.school.common.constant.properties.JwtProperties;
import com.example.school.common.mysql.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 17:25
 * description: jwt 工具类
 */
@AllArgsConstructor
@Component
public class JwtUtils {
    private static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";
    private static final String CLAIM_KEY_USER_ID = "user_id";
    private static final String CLAIM_KEY_AUTHORITIES = "scope";
    private static final String CLAIM_KEY_ACCOUNT_ENABLED = "enabled";
    private static final String CLAIM_KEY_ACCOUNT_NON_LOCKED = "non_locked";
    private static final String CLAIM_KEY_ACCOUNT_NON_EXPIRED = "non_expired";

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private final JwtProperties jwtProperties;

    /**
     * 根据token获取用户id
     *
     * @param token token
     * @return Long
     */
    public Long getUserIdFromToken(String token) {
        Long userId;
        try {
            final Claims claims = getClaimsFromToken(token);
            userId = Long.valueOf(claims.get(CLAIM_KEY_USER_ID).toString());
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    /**
     * 根据token获取用户名称
     *
     * @param token token
     * @return string
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }


    private Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 过期时间 单位 小时
     *
     * @param expiration 小时
     * @return date
     */
    private Date generateExpirationDate(long expiration) {
        return DateUtils.localDateTimeToDate(DateUtils.currentDateTimeAddHour(expiration));
    }

    /**
     * 过期时间 单位 天数
     *
     * @param expiration 天数
     * @return date
     */
    private Date generateRememberMeExpirationDate(long expiration) {
        return DateUtils.localDateTimeToDate(DateUtils.currentDateTimeAddDay(expiration));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String generateAccessToken(String subject, Map<String, Object> claims, Boolean rememberMe) {
        Date expirationDate;
        if (rememberMe) {
            expirationDate = generateRememberMeExpirationDate(jwtProperties.getRememberMeExpiration());
        } else {
            expirationDate = generateExpirationDate(jwtProperties.getRememberMeExpiration());
        }
        return generateToken(subject, claims, expirationDate);
    }

    public String generateRefreshToken(User user) {
        return generateRefreshToken(user, false);
    }

    public String generateRefreshToken(User user, Boolean rememberMe) {
        Map<String, Object> claims = generateClaims(user);
        // 只授于更新 token 的权限
        String[] roles = new String[]{ROLE_REFRESH_TOKEN};
        claims.put(CLAIM_KEY_AUTHORITIES, JSONObject.parse(JSON.toJSONString(roles)));
        return generateRefreshToken(user.getAccount(), claims, rememberMe);
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = generateClaims(user);
        return generateAccessToken(user.getAccount(), claims, false);
    }

    public String generateAccessToken(User user, Boolean rememberMe) {
        Map<String, Object> claims = generateClaims(user);
        return generateAccessToken(user.getAccount(), claims, rememberMe);
    }

    private Map<String, Object> generateClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ID, user.getId());
        claims.put(CLAIM_KEY_ACCOUNT_NON_LOCKED, false);
        claims.put(CLAIM_KEY_ACCOUNT_NON_EXPIRED, false);
        return claims;
    }

    private String generateRefreshToken(String subject, Map<String, Object> claims, Boolean rememberMe) {
        Date expirationDate;
        if (rememberMe) {
            expirationDate = generateRememberMeExpirationDate(jwtProperties.getRememberMeRefreshExpiration());
        } else {
            expirationDate = generateExpirationDate(jwtProperties.getRememberMeExpiration());
        }
        return generateToken(subject, claims, expirationDate);
    }

    private String generateToken(String subject, Map<String, Object> claims, Date expirationDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(UuidUtil.getUUID())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SIGNATURE_ALGORITHM, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 检验token是否正确
     *
     * @param token token
     * @param user  用户
     * @return boolean
     */
    public Boolean validateToken(String token, User user) {
        final Long userId = getUserIdFromToken(token);
        final String userName = getUsernameFromToken(token);
        return (Objects.equals(userId, user.getId()))
                && (Objects.equals(userName, user.getAccount()))
                && !isTokenExpired(token);
    }
}
