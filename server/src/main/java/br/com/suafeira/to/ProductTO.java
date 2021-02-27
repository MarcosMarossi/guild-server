package br.com.suafeira.to;

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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "name" })
public class ProductTO {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer id;
	@Column(unique = true)
	private String name;
	
	@JsonBackReference(value = "customers")
	@ManyToMany
	@JoinTable(name = "customer_product", 
			joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "product_id"), 
			inverseJoinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
	)
	private Set<CustomerTO> customers = new HashSet<CustomerTO>();
}
