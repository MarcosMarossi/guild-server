package br.com.feira.guild.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateForm {
	
	private Integer id;
	private String whatsapp;
	private String email;
	private String name;
	private String password;
	private String customerNewPassword;
	
}
