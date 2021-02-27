package br.com.suafeira.to.form;

import java.util.List;

import br.com.suafeira.to.dto.handler.FairForm;

public class CustomerFairForm {

	private Integer customerId;
	private Integer fairId;
	private List<FairForm> idsFair;
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer id) {
		this.customerId = id;
	}
	public List<FairForm> getIdsFair() {
		return idsFair;
	}
	public void setIdsFair(List<FairForm> idsFair) {
		this.idsFair = idsFair;
	}
	public Integer getFairId() {
		return fairId;
	}
	public void setFairId(Integer fairId) {
		this.fairId = fairId;
	}
}
