package br.com.feira.guild.to;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Customer implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column(name = "customer_id")
	private Integer id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String whatsapp;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String customerPassword;
	@Column(nullable = true)
	private Integer phoneCode;
	@Column(nullable = true)
	private Calendar finalDateCode;
	
	@JsonBackReference(value = "products")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "customer_product",
		joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
		inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "product_id"))	
	private Set<Product> products = new HashSet<Product>();
	
	@JsonBackReference(value = "fairs")
	@ManyToMany(cascade = CascadeType.ALL)	
	@JoinTable(name = "customer_fair",
		joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"),
		inverseJoinColumns = @JoinColumn(name = "fair_id", referencedColumnName = "fair_id"))
	private Set<Fair> fairs = new HashSet<Fair>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Profile> profile = new ArrayList<>();
	
	public Customer(String name, String whatsapp, String email, String customerPassword,
			Set<Product> products, Set<Fair> fairs) {
		super();
		this.name = name;
		this.whatsapp = whatsapp;
		this.email = email;
		this.customerPassword = customerPassword;
		this.products = products;
		this.fairs = fairs;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
