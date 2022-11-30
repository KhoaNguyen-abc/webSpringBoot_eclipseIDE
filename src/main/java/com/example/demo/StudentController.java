package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.DateFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value="/student") // This means URL's start with /demo (after Application path)
public class StudentController {
  // This means to get the bean called userRepository
  // Which is auto-generated by Spring, we will use it to handle the data
	@Autowired
  private JdbcTemplate jdbcTemplate;
	
  @PostMapping(value="/add") // Map ONLY POST Requests
  public ModelAndView addNewUser (@RequestParam(value="name") String nameStudent,
		  @RequestParam(value="phone") String phoneNumber, @RequestParam(value="id") String id) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request
	  ModelAndView mav = new ModelAndView();
	LocalDateTime myDateObj = LocalDateTime.now();
	int idInt = Integer.parseInt(id);
	String sql_insert = "INSERT INTO tblstudent(nameStudent, phoneNumber, created_at, updated_at) VALUES(?, ?, ?, ?)";
	String sql_update = "UPDATE tblstudent SET tblstudent.nameStudent=?, tblstudent.phoneNumber=?, tblstudent.updated_at=? WHERE tblstudent.id=?";
	int result = 0;
	if(!nameStudent.toLowerCase().equalsIgnoreCase("")&&!phoneNumber.toLowerCase().equalsIgnoreCase("")){
		result = jdbcTemplate.update(sql_update, nameStudent, phoneNumber, myDateObj, idInt);
	}
	result = jdbcTemplate.update(sql_insert, nameStudent, phoneNumber, myDateObj, myDateObj);
	if(result > 0) {
		System.out.println("Insert OR Update successful");
	}
	System.out.println("Insert OR Update failed");
	mav.setViewName("redirect:./all");
	return mav;
  }

  @GetMapping(value="/{id}/delete") // Map ONLY GET Requests
  public ModelAndView deleteUser (@PathVariable("id") String id) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request
	  ModelAndView mav = new ModelAndView();
	int idInt = Integer.parseInt(id);
	String sql = "DELETE FROM tblstudent WHERE tblstudent.id=?";
	int result = jdbcTemplate.update(sql, idInt);
	if(result > 0) {
		System.out.println("Insert successful");
	}
	System.out.println(idInt);
	mav.setViewName("redirect:./../all");
	return mav;
  }
  
  @GetMapping(value="/all", params="id") // Map ONLY GET Requests
  public ModelAndView editUser (@RequestParam(value="id", defaultValue="0") String id) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request
	  ModelAndView mav = new ModelAndView();
	int idInt = Integer.parseInt(id);
	String sql = "SELECT * FROM tblstudent WHERE tblstudent.id=?";
	Student student = jdbcTemplate.queryForObject(sql,
						(rs, numRow)->
						new Student(
								rs.getInt("id"),
								rs.getString("nameStudent"),
								rs.getString("phoneNumber"),
								rs.getString("created_at"),
								rs.getString("updated_at")
						),
						new Object[] {idInt});
	System.out.println(student);
	mav.addObject("stdent", student);
	mav.setViewName("student/index");
	return mav;
  }
  
  @GetMapping(value="/all")
  public ModelAndView getAllStudents() {
    // This returns a JSON or XML with the users;
	  ModelAndView mav = new ModelAndView();
	  String sql = "SELECT * FROM tblstudent";
	  List<Student> studentList = jdbcTemplate.query(sql,
			  (rs, rowNum) ->
			  new Student(
					  rs.getInt("id"),
					  rs.getString("nameStudent"),
					  rs.getString("phoneNumber"),
					  rs.getString("created_at"),
					  rs.getString("updated_at")
					  ));
	  Student student = new Student(0, "", "", "", "");
	  mav.addObject("stdList", studentList);
	  mav.addObject("stdent", student);
	  mav.setViewName("student/index");
	  return mav;
  }
}