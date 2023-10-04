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
public class Fair {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fair_id")
	private Integer id;
	@Column(unique = true)
	private String siteName;
	private String description;
	private String address;
	private String city;
	private String uf;
	private String dayWeek;
	private Double latitude;
	private Double longitude;
	
	@JsonBackReference(value = "customers")
	@ManyToMany
	@JoinTable(
		name = "customer_fair", 
		joinColumns = @JoinColumn(name = "fair_id", referencedColumnName = "fair_id"), 
		inverseJoinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
	)
	private Set<Customer> customers = new HashSet<Customer>();
	
	public Fair(String siteName, String description, String address, String city, String uf, String dayWeek,
			Double latitude, Double longitude) {
		this.siteName = siteName;
		this.description = description;
		this.address = address;
		this.city = city;
		this.uf = uf;
		this.dayWeek = dayWeek;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
