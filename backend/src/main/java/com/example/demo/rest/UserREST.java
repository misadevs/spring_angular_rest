package com.example.demo.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/v1/usuario/")
public class UserREST {
	@Autowired
	private UserService userService;
	
	@GetMapping
	private ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.findAll());
	}
	
	@GetMapping("{id}")
	private ResponseEntity<?> getUser(@PathVariable("id") Long userId) {
		Optional<User> user = userService.findById(userId);
		
		if (!user.isPresent()) {
			Map<String, String> resError = new HashMap<>();
			
			resError.put("error", "Usuario no encontrado");
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resError);
		} else {
			return ResponseEntity.ok(user.get());
		}
	}
	
	@PostMapping
	private ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			Map<String, Object> response = new HashMap<>();
			Map<String, Object> data = new HashMap<>();
			List<String> errors = new ArrayList<>();
			
			
			if (user.getAnios_cursados() <= 0) {
				errors.add("El campo anios cursados es requerido");
			} if (user.getApellido() == null || user.getApellido().isEmpty()) {
				errors.add("El campo apellido es requerido");
			} if (user.getCarrera() == null || user.getCarrera().isEmpty()) {
				errors.add("El campo carrera es requerido");
			} if (user.getEscuela() == null || user.getEscuela().isEmpty()) {
				errors.add("El campo escuela es requerido");
			} if (user.getNombre() == null || user.getNombre().isEmpty()) {
				errors.add("El campo nombre es requerido");
			} if (user.getEdad() <= 0) {
				errors.add("El campo edad es requerido");
			}
			
			
			if (!errors.isEmpty()) {
				data.put("errors", errors);
				response.put("success", false);
				response.put("msg", "error");
				response.put("data", data);
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			
			User userSaved = userService.save(user);
			return ResponseEntity.created(new URI("/api/v1/usuario/"+userSaved.getId())).body(userSaved);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@PutMapping("{id}")
	private ResponseEntity<?> updateUser(@PathVariable("id") Long userId, @RequestBody User user) {
		try {
			Map<String, Object> response = new HashMap<>();
			Map<String, Object> data = new HashMap<>();
			List<String> errors = new ArrayList<>();
			
			Optional<User> userDb = userService.findById(userId);
			
			if (!userDb.isPresent()) {
				errors.add("El usuario no existe");
				data.put("errors", errors);
				response.put("success", false);
				response.put("msg", "error");
				response.put("data", data);
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			
			if (user.getAnios_cursados() <= 0) {
				errors.add("El campo anios cursados es requerido");
			} if (user.getApellido() == null || user.getApellido().isEmpty()) {
				errors.add("El campo apellido es requerido");
			} if (user.getCarrera() == null || user.getCarrera().isEmpty()) {
				errors.add("El campo carrera es requerido");
			} if (user.getEscuela() == null || user.getEscuela().isEmpty()) {
				errors.add("El campo escuela es requerido");
			} if (user.getNombre() == null || user.getNombre().isEmpty()) {
				errors.add("El campo nombre es requerido");
			} if (user.getEdad() <= 0) {
				errors.add("El campo edad es requerido");
			}
			
			if (!errors.isEmpty()) {
				data.put("errors", errors);
				response.put("success", false);
				response.put("msg", "error");
				response.put("data", data);
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			
			User userUpdated = userDb.get();
			userUpdated.setAnios_cursados(user.getAnios_cursados());
			userUpdated.setApellido(user.getApellido());
			userUpdated.setCarrera(user.getCarrera());
			userUpdated.setEdad(user.getEdad());
			userUpdated.setEscuela(user.getEscuela());
			userUpdated.setNombre(user.getNombre());
			userUpdated.setId(userId);
			
			userService.save(userUpdated);
			
			return ResponseEntity.created(new URI("/api/v1/usuario/"+userUpdated.getId())).body(userUpdated);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@DeleteMapping("{id}")
	private ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {
		try {
			Map<String, Object> response = new HashMap<>();
			Map<String, Object> data = new HashMap<>();
			List<String> errors = new ArrayList<>();
			
			Optional<User> userDb = userService.findById(userId);
			
			if (!userDb.isPresent()) {
				errors.add("El usuario no existe");
				data.put("errors", errors);
				response.put("success", false);
				response.put("msg", "error");
				response.put("data", data);
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			
			userService.deleteById(userId);
			response.put("success", true);
			response.put("msg", "Usuario eliminado");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

}
