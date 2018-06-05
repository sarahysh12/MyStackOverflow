package me.arminb.sara.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class SecurityConfigurationBeans {

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService)
    {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        dao.setUserDetailsService(userDetailsService);
        return dao;
    }

    @Bean
    TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    AuthorizationServerTokenServices tokenServices()
    {
        DefaultTokenServices ret = new DefaultTokenServices();
        ret.setTokenStore(tokenStore());
        ret.setAccessTokenValiditySeconds(120);
        ret.setRefreshTokenValiditySeconds(1000);
        ret.setSupportRefreshToken(true);
        return ret;
    }

}