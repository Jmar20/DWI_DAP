package org.utp.pydwi.access.domain.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.utp.pydwi.access.domain.model.valueobjects.Email;
import org.utp.pydwi.access.domain.model.valueobjects.Nombre;
import org.utp.pydwi.access.domain.model.valueobjects.Rol;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
@Getter
@NoArgsConstructor
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "nombre"))
    private Nombre nombre;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private Email email;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    @Column(name = "contraseña", nullable = false)
    private String password;

    public Usuario(Nombre nombre, Email email, String password, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public void actualizarPassword(String nuevoPassword) {
        this.password = nuevoPassword;
    }

    public void actualizarRol(Rol nuevoRol) {
        this.rol = nuevoRol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email.getValue();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
