package br.com.feira.guild.service;

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
import br.com.feira.guild.to.form.CustomerFairForm;
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
				} else if (field == null) {
					throw new ValidateException("The field can't be null or empty.");
				}
			}
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

	public void addFairs(CustomerFairForm cfForm) {
		Optional<Customer> auxCustomer = customerRepository.findById(cfForm.getCustomerId());
		
		if (auxCustomer.isPresent()) {
			Customer customer = auxCustomer.get();
			
			Set<Fair> fairs = new TreeSet<Fair>();
			fairs = customer.getFairs();		
			
			List<Fair> fairList = fairService.findAll();
			
			for (Fair fair : fairList) {
				if (cfForm.getIdsFair().stream().anyMatch(item -> item.getIdFair().equals(fair.getId()))) {
					fairs.add(fair);
				}
			}
						
			customer.setFairs(fairs);
		} else {
			throw new EntityNotFoundException("Customer with id " + cfForm.getCustomerId() + " not found.");
		}	
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

			if (!form.getWhatsapp().isEmpty() || form.getWhatsapp().length() >= 11) {
				customer.setWhatsapp(form.getWhatsapp());
			}

			if (!form.getCustomerNewPassword().isEmpty() || form.getCustomerNewPassword().length() >= 8) {
				String registerPassword = new BCryptPasswordEncoder().encode(form.getCustomerNewPassword());
				customer.setCustomerPassword(registerPassword);
			}

			customerRepository.save(customer);
		} else {
			throw new EntityNotFoundException("Customer with e-mail " + form.getEmail() + " not found.");
		}
	}

}
