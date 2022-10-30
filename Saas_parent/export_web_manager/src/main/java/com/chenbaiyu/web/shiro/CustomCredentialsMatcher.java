package com.chenbaiyu.web.shiro;



import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info){
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        String password = new String(userToken.getPassword());
        String salt = userToken.getUsername();

        Md5Hash md5Hash = new Md5Hash(password, salt);
        String encodePwd = md5Hash.toString();

        SimpleAuthenticationInfo authenticationInfo = (SimpleAuthenticationInfo) info;
        String credentials = (String) authenticationInfo.getCredentials();

        return credentials.equals(encodePwd);
    }
}
