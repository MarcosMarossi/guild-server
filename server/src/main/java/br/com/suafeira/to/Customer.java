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

@Entity
public class Customer implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue( strategy = GenerationType.IDENTITY )
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

	public Customer() {
		
	}
	
	public Customer(String name, String whatsapp, String email, String customerPassword) {
		this.name = name;
		this.whatsapp = whatsapp;
		this.email = email;
		this.customerPassword = customerPassword;
	}
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return name;
	}

	public void setNome(String nome) {
		this.name = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCustomerPassword() {
		return customerPassword;
	}
	
	public void setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;
	}
	
	public String getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Product> getProducts() {
		return products;
	}
	
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Set<Fair> getFairs() {
		return fairs;
	}
	public void setFairs(Set<Fair> fairs) {
		this.fairs = fairs;
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
