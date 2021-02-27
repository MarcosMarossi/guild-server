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
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "siteName", "description", "address", "city", "uf", "dayWeek", "latitude", "longitude" })
public class FairTO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@JoinTable(name = "customer_fair", 
			joinColumns = @JoinColumn(name = "fair_id", referencedColumnName = "fair_id"), 
			inverseJoinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
	)
	private Set<CustomerTO> customers = new HashSet<CustomerTO>();

	public FairTO(String siteName, String description, String address, String city, String uf, String dayWeek,
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
