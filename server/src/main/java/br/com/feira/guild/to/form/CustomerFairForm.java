package br.com.feira.guild.to.form;

import java.util.List;

import br.com.feira.guild.to.dto.handler.FairHandler;

public class CustomerFairForm {

	private Integer customerId;
	private Integer fairId;
	private List<FairHandler> idsFair;
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer id) {
		this.customerId = id;
	}
	public List<FairHandler> getIdsFair() {
		return idsFair;
	}
	public void setIdsFair(List<FairHandler> idsFair) {
		this.idsFair = idsFair;
	}
	public Integer getFairId() {
		return fairId;
	}
	public void setFairId(Integer fairId) {
		this.fairId = fairId;
	}
}
