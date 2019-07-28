package com.example.school.shiro.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:47
 * description:
 */
public class AModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {

        assertRealmsConfigured();
        List<Realm> realms = this.getRealms()
                .stream()
                .filter(realm -> realm.supports(authenticationToken))
                .collect(toList());
        return realms.size() == 1 ? this.doSingleRealmAuthentication(realms.iterator().next(), authenticationToken) : this.doMultiRealmAuthentication(realms, authenticationToken);

    }
}
