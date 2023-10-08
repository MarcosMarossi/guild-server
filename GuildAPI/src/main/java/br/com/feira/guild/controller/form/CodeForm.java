package br.com.feira.guild.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CodeForm {
	
	private String recipient;
	private String sender;
	private String accountSID;
	private String authToken;
	
}
