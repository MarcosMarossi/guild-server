package br.com.feira.guild.to.form;

import java.util.List;

public class AssociateForm {

	private Integer customerId;
	private Integer fairId;
	private List<Integer> idsFair;
	private List<Integer> idsProduct;
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer id) {
		this.customerId = id;
	}
	public List<Integer> getIdsFair() {
		return idsFair;
	}
	public void setIdsFair(List<Integer> idsFair) {
		this.idsFair = idsFair;
	}
	public Integer getFairId() {
		return fairId;
	}
	public void setFairId(Integer fairId) {
		this.fairId = fairId;
	}
	public List<Integer> getIdsProduct() {
		return idsProduct;
	}
	public void setIdsProduct(List<Integer> idsProduct) {
		this.idsProduct = idsProduct;
	}
}
