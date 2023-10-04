package br.com.feira.guild.to;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Product {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer id;
	@Column(unique = true)
	private String name;
	
	@JsonBackReference(value = "customers")
	@ManyToMany
	@JoinTable(name = "customer_product", 
		joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "product_id"), 
		inverseJoinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id"))
	private Set<Customer> customers = new HashSet<Customer>();
	
	public Product(String name) {
		this.name = name;
	}
	
}
