package com.example.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.Category;
import com.example.model.Customer;
import com.example.model.Order;
import com.example.model.OrderDetails;
import com.example.model.Vegetable;
import com.google.gson.Gson;

@RestController
@EnableJpaRepositories(basePackages="com.example.controllers")
@ComponentScan(basePackages = "com.example.model")
@EntityScan(basePackages = "com.example.model")
public class WebController {
	
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private VegetableRepository vegetableRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderDetailsRepository orderDetailsRepository;
	
	List<String> cartItemList = new ArrayList<String>();

	public WebController() {
		this.categoryRepository = null;
		this.vegetableRepository = null;
		this.customerRepository = null;
		this.orderRepository = null;
		this.orderDetailsRepository = null;
	}

	public WebController(CategoryRepository categoryRepository, VegetableRepository vegetableRepository,
			CustomerRepository customerRepository, OrderRepository orderRepository,
			OrderDetailsRepository orderDetailsRepository) {
		this.categoryRepository = categoryRepository;
		this.vegetableRepository = vegetableRepository;
		this.customerRepository = customerRepository;
		this.orderRepository = orderRepository;
		this.orderDetailsRepository = orderDetailsRepository;
	}

	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView Index(@RequestParam(value="pageID", defaultValue = "0") int pageID,
			@RequestParam(value="sell", defaultValue = "0") int sold, @RequestParam(value="search", defaultValue = "") String search,
			@RequestParam(value="categoryID", defaultValue = "") List<Integer> categoryIDList) {
		
		ModelAndView mav = new ModelAndView();
		int sizePerPage = 3;
		String searchItem = "%"+search+"%";
		
		if(categoryIDList.isEmpty()) {
			for(Category category: categoryRepository.findAll()) {
				categoryIDList.add(category.getCatagoryID());
			}
		}
		
		categoryIDList = new ArrayList<Integer>(categoryIDList);
		
		Pageable limitVegetable = (Pageable) PageRequest.of(pageID, sizePerPage,  Sort.by("Vegetable.vegetableID").ascending());
		Page<Vegetable> vegetableList = vegetableRepository.findAllByCategoryIDAndNameAndQuantity(categoryIDList, searchItem, sold, limitVegetable);
		Page<Vegetable> totalVegetableItems = vegetableRepository.findAllByCategoryIDAndNameAndQuantity(categoryIDList, searchItem, sold, null);
		long numberOfPageVegetable = Math.round(totalVegetableItems.getSize() / sizePerPage);
		
		mav.addObject("categoryIDList", categoryIDList);
		mav.addObject("categoryList", categoryRepository.findAll());
		mav.addObject("vegetableList", vegetableList);
		mav.addObject("totalPageOfVegetable", numberOfPageVegetable);
		
		mav.setViewName("index");
		return mav;
	}
	
	@RequestMapping(value="/pages/product", method = RequestMethod.GET)
	public ModelAndView Products(@RequestParam(value="vegetableID", defaultValue = "1") int vegetableID) {
		ModelAndView mav = new ModelAndView();
		
		Vegetable vegetableItem = vegetableRepository.findById(vegetableID);
		
		mav.addObject("vegetableItem", vegetableItem);
		
		mav.setViewName("pages/product");
		return mav;
	}
	
	@RequestMapping(value="/pages/product/add", method = RequestMethod.POST)
	public ModelAndView AddProduct(@RequestParam(value="vegetableID", defaultValue = "1") int vegetableID,
			@RequestParam(value="quantity", defaultValue = "1") int quantity,
			@CookieValue(name = "cart", defaultValue = "[]") String cartCookie,
			HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView();
		Vegetable vegetable  = vegetableRepository.findById(vegetableID);
		
		if(quantity <= 0) {
			quantity = 1;
		}
		
		Cookie cookie;
			try {
				String cart = URLDecoder.decode(cartCookie, StandardCharsets.UTF_8.toString());
				boolean isTrue = false;
				JSONArray jsonArray = new JSONArray(cart);
				JSONObject jsonObject = new JSONObject();	
				
				
				for(int i=0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					String[] splitStr = object.getString("Vegetable").split(",");
					if(splitStr[0].contains(String.valueOf(vegetable.getVegetableID()))) {	
						isTrue = true;
					}
					if(isTrue == true) {
						object.put("Vegetable", vegetable);
						object.put("Quantity", quantity);
						jsonArray.put(i, object);
						break;
					}
				}
				
				if(isTrue == false) {
					jsonObject.put("Vegetable",  vegetable);
					jsonObject.put("Quantity", quantity);
					jsonArray.put(jsonObject);
				}
				
				cookie = new Cookie("cart", URLEncoder.encode(jsonArray.toString(), StandardCharsets.UTF_8.toString()));
				cookie.setMaxAge(24*60*60 + (7*24*60*60+1000));
				cookie.setPath("/");
				
				response.addCookie(cookie);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		mav.setViewName("redirect:./../../");
		
		return mav;
	}
	
	@RequestMapping(value="/pages/checkout/delete", method = RequestMethod.GET)
	public ModelAndView AddProduct(@RequestParam(value="index", defaultValue = "0") int index,
			@CookieValue(name = "cart", defaultValue = "[]") String cartCookie,
			HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView();
		
		Cookie cookie;
		try {
			String cart = URLDecoder.decode(cartCookie, StandardCharsets.UTF_8.toString());
			JSONArray jsonArray = new JSONArray(cart);
			
			jsonArray.remove(index);
			
			cookie = new Cookie("cart", URLEncoder.encode(jsonArray.toString(), StandardCharsets.UTF_8.toString()));
			cookie.setMaxAge(24*60*60 + (7*24*60*60+1000));
			cookie.setPath("/");
			
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.setViewName("redirect:./../checkout");
		return mav;
	}
	
	@RequestMapping(value="/pages/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView mav = new ModelAndView();
		Customer customer = new Customer();
		
		mav.addObject("Customer", customer); 
		mav.setViewName("pages/register");
		return mav;
	}
	
	@RequestMapping(value="/pages/register/add", method = RequestMethod.POST)
	public ModelAndView register(@ModelAttribute("Customer") Customer customer) {
		ModelAndView mav = new ModelAndView();
		String password = customer.getPassword();
		String fullname = customer.getFullname();
		String address = customer.getAddress();
		String city = customer.getCity();
		Set<Order> orderList = new HashSet<Order>();
		
		customerRepository.save(customer);
		mav.setViewName("redirect: ./../../login");
		return mav;
	}
	
	@RequestMapping(value="/pages/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		Customer customer = new Customer();
		mav.addObject("Customer", customer);
		mav.setViewName("pages/login");
		return mav;
	}
	
	@RequestMapping(value="/pages/login/success", method = RequestMethod.GET)
	public ModelAndView LoginSuccess(@RequestParam(value="customerID", defaultValue = "0") int customerID,
			@RequestParam(value="password", defaultValue = "") String password,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		JSONObject jsonObject = new JSONObject();
		
		Cookie cookie;
		try {
			if(customerID > 0 && !password.isEmpty()) {
				Customer customer1 = customerRepository.findById(customerID, password);
				jsonObject.put("customerID", customer1.getCustomerID());
				jsonObject.put("username", customer1.getFullname());
				jsonObject.put("password", customer1.getPassword());
				jsonObject.put("address", customer1.getAddress());
				jsonObject.put("city", customer1.getCity());
				
				cookie = new Cookie("User", URLEncoder.encode(jsonObject.toString(), StandardCharsets.UTF_8.toString()));
				cookie.setMaxAge(24*60*60*60 + (7*86400*1000));
				cookie.setPath("/");
				
				response.addCookie(cookie);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.setViewName("redirect:./../../");
		return mav;
	}
	
	@RequestMapping(value="/pages/logout", method = RequestMethod.GET)
	public ModelAndView LogoutSuccess(@CookieValue(value="User", defaultValue = "[]") String userCookie,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		
		Cookie cookie = new Cookie("User", "[]");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		mav.setViewName("redirect:./../");
		return mav;
	}
	
	@RequestMapping(value="/pages/checkout", method = RequestMethod.GET)
	public ModelAndView Cart() {
		ModelAndView mav = new ModelAndView();

		mav.setViewName("pages/checkout");
		return mav;
	}
	
	@RequestMapping(value="/pages/checkout/add", method = RequestMethod.POST)
	public ModelAndView SaveCart(@RequestParam(value="customerID") int customerID,
			@RequestParam(value="total") float Total,
			@RequestParam(value="VegetableID") List<Integer> vegetableIdList,
			@RequestParam(value="Quantity") List<Integer> quantityList,
			@RequestParam(value="Price") List<Float> priceList,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		
		Customer customer = customerRepository.findById(customerID);
		Date date = new Date();
		float totalOrder = Total;
		String Note = "";
		Set<Vegetable> vegetableOrderList = new HashSet<Vegetable>();
		
		Order order = new Order(customerID, date, totalOrder, Note, customer, vegetableOrderList);
		orderRepository.save(order);
		
		List<Order> orderList = orderRepository.findAll();
		int orderID = orderList.get(orderList.size()-1).getOrderID();
		for(int i=0; i < vegetableIdList.size(); i++) {
			OrderDetails orderDetails = new OrderDetails(orderID, vegetableIdList.get(i), quantityList.get(i), priceList.get(i));
			orderDetailsRepository.save(orderDetails);
		}
		
		Cookie cookie = new Cookie("cart", "[]");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		mav.addObject("messegePurchase", "Your have ordered, delivery is coming soon");
		mav.setViewName("redirect:./../checkout");
		return mav;
	}
}
