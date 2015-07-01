package sample.data.elasticsearch;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	CustomerRepository customerRepository;

	@RequestMapping(path="/findAll", method=RequestMethod.GET)
    public @ResponseBody List<Customer> findAll() {
		List<Customer> customers = new ArrayList<Customer>();
		for (Customer item : customerRepository.findAll()) {
			customers.add(item);
		}
		
        return customers;
    }
	
	@RequestMapping(path="/save", method=RequestMethod.POST)
    public @ResponseBody Customer save(@RequestBody Customer customer) {
		Customer newCustomer = customerRepository.save(customer);
        return newCustomer;
    }
	

	@RequestMapping(path="/delete", method=RequestMethod.POST)
    public @ResponseBody void delete(@RequestBody Customer customer) {
		customerRepository.delete(customer);
    }
}
