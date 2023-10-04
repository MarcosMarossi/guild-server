package br.com.feira.guild.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UpdateForm {
	
	private String whatsapp;
	private String email;
	private String name;
	private String password;
	private String customerNewPassword;
	
}
