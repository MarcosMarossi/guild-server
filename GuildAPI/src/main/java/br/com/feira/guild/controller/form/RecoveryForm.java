package br.com.feira.guild.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class RecoveryForm {

	private String password;
	private Integer code;
	private String whatsapp;
	
}
