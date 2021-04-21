package br.com.suafeira.to;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "name", "whatsapp", "email", "customerPassword" })
public class CustomerTO implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Integer id;
	private String name;
	private String whatsapp;
	@Column(unique = true)
	private String email;
	private String customerPassword;

	@JsonBackReference(value = "products")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "customer_product", 
				joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"), 
				inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "product_id")
	)
	private Set<ProductTO> products = new HashSet<ProductTO>();

	@JsonBackReference(value = "fairs")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "customer_fair", 
				joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"), 
				inverseJoinColumns = @JoinColumn(name = "fair_id", referencedColumnName = "fair_id")
	)
	private Set<FairTO> fairs = new HashSet<FairTO>();

	@ManyToMany(fetch = FetchType.EAGER)
	private List<ProfileTO> profile = new ArrayList<>();

	public CustomerTO(String name, String whatsapp, String email, String customerPassword) {
		this.name = name;
		this.whatsapp = whatsapp;
		this.email = email;
		this.customerPassword = customerPassword;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.profile;
	}

	@Override
	public String getPassword() {
		return this.customerPassword;
	}

	@Override
	public String getUsername() {
		return this.email;
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
