package com.example.school.shiro.shiro.factory;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:49
 * description: 对于无状态的TOKEN不创建session 这里都不使用session
 */
public class StatelessWebSubjectFactory  extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        // 这里都不创建session
        AuthenticationToken token = context.getAuthenticationToken();
        context.setSessionCreationEnabled(Boolean.FALSE);
        return super.createSubject(context);
    }

    public StatelessWebSubjectFactory() {}

}
