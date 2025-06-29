package org.utp.pydwi.access.infrastructure.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.utp.pydwi.access.domain.model.valueobjects.Email;
import org.utp.pydwi.access.domain.services.UserDomainService;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserDomainService userDomainService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDomainService.buscarPorEmail(new Email(username));
    }
}
