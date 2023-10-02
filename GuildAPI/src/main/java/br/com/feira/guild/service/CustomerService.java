package br.com.feira.guild.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.feira.guild.exceptions.EntityNotFoundException;
import br.com.feira.guild.exceptions.ValidateException;
import br.com.feira.guild.repository.CustomerRepository;
import br.com.feira.guild.to.Customer;
import br.com.feira.guild.to.Fair;
import br.com.feira.guild.to.Product;
import br.com.feira.guild.to.dto.CustomerDTO;
import br.com.feira.guild.to.form.AssociateForm;
import br.com.feira.guild.to.form.CustomerForm;
import br.com.feira.guild.to.form.UpdateForm;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private FairService fairService;

	@Autowired
	private ProductService productService;

	public Customer findByEmail(String email) {
		Optional<Customer> customer = customerRepository.findByEmail(email);

		if (!customer.isPresent()) {
			throw new EntityNotFoundException("Customer not found");
		}

		return customer.get();
	}

	public void save(CustomerForm form) {
		validadeFields(form.getIdsFair(), form.getIdsProduct(), form.getCustomerPassword());

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

	public void validadeFields(Object... fields) {
		if (fields != null) {
			for (Object field : fields) {
				if (field instanceof List && ((List<?>) field).isEmpty()) {
					throw new ValidateException("The field can't be null or empty.");
				}
				if (field instanceof String && field.toString().isEmpty()) {
					throw new ValidateException("The field can't be null or empty.");
				}
				if (field == null) {
					throw new ValidateException("The field can't be null or empty.");
				}
			}

			return;
		}

		throw new ValidateException("The field can't be null or empty.");
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
		Optional<Customer> auxCustomer = customerRepository.findByEmail(form.getEmail());

		if (auxCustomer.isPresent()) {
			Customer customer = auxCustomer.get();

			if (form.getName() != null && !form.getName().isEmpty()) {
				customer.setName(form.getName());
			}

			if (form.getEmail() != null && !form.getEmail().isEmpty()) {
				customer.setEmail(form.getEmail());
			}

			if (form.getWhatsapp() != null && !form.getWhatsapp().isEmpty()) {
				customer.setWhatsapp(form.getWhatsapp());
			}

			if (form.getCustomerNewPassword() != null && !form.getCustomerNewPassword().isEmpty()) {
				String dbPassoword = customer.getCustomerPassword();
				String formPassword = new BCryptPasswordEncoder().encode(form.getPassword());

				if (dbPassoword.equals(formPassword)) {
					String registerPassword = new BCryptPasswordEncoder().encode(form.getCustomerNewPassword());
					customer.setCustomerPassword(registerPassword);
				} else {
					throw new ValidateException("The old password does not match");
				}
			}

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

}
