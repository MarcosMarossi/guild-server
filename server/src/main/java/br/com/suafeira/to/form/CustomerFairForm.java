package br.com.suafeira.to.form;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CustomerFairForm {

	private Integer customerId;
	private Integer fairId;
	private List<FairForm> idsFair;
	
}
