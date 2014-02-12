package uk.co.b2esoftware.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

/**
 * This AuthenticationUserDetailsService just maps the granted authorities in the pre-authenticated token into the
 * returned UserDetails object.
 * User: art
 * Date: 02/10/2013
 * Time: 12:16
 */
public class PreAuthUserDetailsService implements AuthenticationUserDetailsService
{
    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException
    {
        PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails tokenDetails =
            (PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails) token.getDetails();

        return new User(
            (String) token.getPrincipal(),
            "unused", true, true, true, true,
            tokenDetails.getGrantedAuthorities());
    }
}
