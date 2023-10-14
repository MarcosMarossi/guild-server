package br.com.feira.guild.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import br.com.feira.guild.controller.dto.CodeDTO;
import br.com.feira.guild.controller.dto.CustomerDTO;
import br.com.feira.guild.controller.form.AssociateForm;
import br.com.feira.guild.controller.form.CodeForm;
import br.com.feira.guild.controller.form.CustomerForm;
import br.com.feira.guild.controller.form.RecoveryForm;
import br.com.feira.guild.controller.form.UpdateForm;
import br.com.feira.guild.exceptions.CustomerException;
import br.com.feira.guild.exceptions.EntityNotFoundException;
import br.com.feira.guild.exceptions.GenerateCodeException;
import br.com.feira.guild.repository.CustomerRepository;
import br.com.feira.guild.to.Customer;
import br.com.feira.guild.to.Fair;
import br.com.feira.guild.to.Product;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private FairService fairService;

	@Autowired
	private ProductService productService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Customer findByEmail(String email) {
		Optional<Customer> customer = customerRepository.findByEmail(email);

		if (!customer.isPresent()) {
			throw new EntityNotFoundException("Customer not found");
		}

		return customer.get();
	}

	public void save(CustomerForm form) {
		Set<Fair> fairs = new HashSet<Fair>();

		List<Fair> fairList = fairService.findAll();

		for (Fair fair : fairList) {
			if (form.getIdsFair().stream().anyMatch(item -> item.getIdFair().equals(fair.getId()))) {
				fairs.add(fair);
			}
		}

		Set<Product> products = new HashSet<Product>();

		List<Product> productList = productService.findAll();

		for (Product product : productList) {
			if (form.getIdsProduct().stream().anyMatch(item -> item.getIdProduct().equals(product.getId()))) {
				products.add(product);
			}
		}

		customerRepository.save(new Customer(form.getName(), form.getWhatsapp(), form.getEmail(),
				new BCryptPasswordEncoder().encode(form.getCustomerPassword()), products, fairs));
	}

	public CustomerDTO findByIdWithFairAndProducts(Integer id) {
		Optional<Customer> customer = customerRepository.findById(id);

		if (customer.isPresent()) {
			Set<Product> products = customer.get().getProducts();

			List<Product> ordenedList = products.stream().collect(Collectors.toList());

			ordenedList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));

			return new CustomerDTO(customer.get().getName(), customer.get().getWhatsapp(), customer.get().getEmail(),
					customer.get().getFairs(), ordenedList);
		}

		throw new EntityNotFoundException("Customer with id " + id + " not found.");
	}

	public void association(AssociateForm associateForm) {
		Optional<Customer> auxCustomer = customerRepository.findById(associateForm.getCustomerId());

		if (!auxCustomer.isPresent()) {
			throw new EntityNotFoundException("Customer with id " + associateForm.getCustomerId() + " not found.");
		}

		Customer customer = auxCustomer.get();

		if (associateForm.getIdsFair() != null && associateForm.getIdsFair().size() != 0) {
			List<Fair> fairs = new ArrayList<Fair>();
			List<Fair> fairList = fairService.findAll();

			for (Fair fair : fairList) {
				if (associateForm.getIdsFair().stream().anyMatch(item -> item.equals(fair.getId()))) {
					fairs.add(fair);
				}
			}

			customer.setFairs(new HashSet<>(fairs));
		}

		if (associateForm.getIdsProduct() != null && associateForm.getIdsProduct().size() != 0) {
			List<Product> aux = new ArrayList<Product>();
			List<Product> products = productService.findAll();

			for (Product product : products) {
				if (associateForm.getIdsProduct().stream().anyMatch(item -> item.equals(product.getId()))) {
					aux.add(product);
				}
			}

			customer.setProducts(new HashSet<Product>(aux));
		}

		customerRepository.save(customer);
	}

	public void deleteById(Integer customerId, Integer fairId) {
		Optional<Customer> auxCustomer = customerRepository.findById(customerId);

		if (auxCustomer.isPresent()) {
			Customer customer = auxCustomer.get();

			Set<Fair> fairs = new TreeSet<Fair>();
			fairs = customer.getFairs();

			Fair fair = fairService.findById(fairId);
			fairs.remove(fair);

			customer.setFairs(fairs);
			customerRepository.save(customer);
		} else {
			throw new EntityNotFoundException("Customer with id " + customerId + " not found.");
		}
	}

	public void update(UpdateForm form) {
		Optional<Customer> auxCustomer = customerRepository.findById(form.getId());

		if (auxCustomer.isPresent()) {
			Customer customer = auxCustomer.get();

			if (form.getCustomerNewPassword() != null && !form.getCustomerNewPassword().isEmpty()) {
				if (passwordEncoder.matches(form.getPassword(), customer.getPassword())) {
					customer.setCustomerPassword(passwordEncoder.encode(customer.getCustomerPassword()));
				} else {
					throw new CustomerException("Password does not match.");
				}
			}
			
			customer.setEmail(form.getEmail() != null && !form.getEmail().isBlank() ? form.getEmail() : customer.getEmail());
			customer.setName(form.getName() != null && !form.getName().isBlank() ? form.getName() : customer.getName());
			customer.setWhatsapp(form.getWhatsapp() != null && !form.getWhatsapp().isBlank() ? form.getWhatsapp() : customer.getWhatsapp());
		
			customerRepository.save(customer);
		} else {
			throw new EntityNotFoundException("Customer with e-mail " + form.getEmail() + " not found.");
		}
	}

	public Optional<Customer> findById(Integer customerId) {
		return customerRepository.findById(customerId);
	}

	public void save(Customer customer) {
		customerRepository.save(customer);
	}

	public CodeDTO send(CodeForm form) {
		Optional<Customer> optCustomer = customerRepository.findByWhatsapp(form.getRecipient());

		if (optCustomer.isPresent()) {
			Customer customer = optCustomer.get();

			if (customer.getFinalDateCode() == null || Calendar.getInstance().after(customer.getFinalDateCode())) {
				Integer min = 100000;
				Integer max = 999999;

				Integer code = (int) Math.floor(Math.random() * (max - min + 1) + min);

				Twilio.init(form.getAccountSID(), form.getAuthToken());

				PhoneNumber phoneRecipient = new PhoneNumber("+55" + form.getRecipient());
				PhoneNumber phoneSender = new PhoneNumber(form.getSender());

				Message message = Message.creator(phoneRecipient, phoneSender,
						"Informe este código de verificação no seu aplicativo: " + code).create();

				customer.setPhoneCode(code);

				Calendar finalDateCode = Calendar.getInstance();
				finalDateCode.add(Calendar.MINUTE, 30);
				customer.setFinalDateCode(finalDateCode);

				customerRepository.save(customer);

				return new CodeDTO(message.getSid(), finalDateCode.getTime());
			}

			throw new GenerateCodeException(
					"Error generating a new authentication code. A code has already been sent to your mobile phone.");
		} else {
			throw new EntityNotFoundException("Customer with whatsapp " + form.getRecipient() + " not found.");
		}
	}

	public void changePassword(RecoveryForm form) {
		Optional<Customer> optCustomer = customerRepository.findByWhatsappAndPhoneCode(form.getWhatsapp(),
				form.getCode());

		if (optCustomer.isPresent()) {
			Customer customer = optCustomer.get();

			customer.setPhoneCode(null);
			customer.setFinalDateCode(null);
			customer.setCustomerPassword(passwordEncoder.encode(form.getPassword()));

			customerRepository.save(customer);
		} else {
			throw new EntityNotFoundException("Customer with whatsapp " + form.getWhatsapp() + " not found.");
		}
	}

}
