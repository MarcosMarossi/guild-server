package br.com.feira.guild.to;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
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
	
	public Product() {
		
	}
	
	public Product(String name) {
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Customer> getCustomers() {
		return customers;
	}
	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}
}
