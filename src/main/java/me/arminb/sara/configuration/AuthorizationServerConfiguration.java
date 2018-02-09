package me.arminb.sara.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import java.util.ArrayList;
import java.util.List;


public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;
    List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>();

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.checkTokenAccess("hasRole('CLIENT')");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .authorizedGrantTypes("password", "refresh_token")
                .authorities("ROLE_USER")
                .scopes("read")
                .secret("secret").accessTokenValiditySeconds(3600);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        providers.add(daoAuthenticationProvider);
        endpoints
                .tokenServices(myTokenService())
                .authenticationManager(new ProviderManager(providers));
    }

    @Bean
    @Qualifier("myTokenService")
    AuthorizationServerTokenServices myTokenService()
    {
        DefaultTokenServices ret = new DefaultTokenServices();
        ret.setTokenStore(new InMemoryTokenStore());
        ret.setAccessTokenValiditySeconds(3000);
        ret.setRefreshTokenValiditySeconds(3000);
        ret.setSupportRefreshToken(true);
        return ret;
    }


}
