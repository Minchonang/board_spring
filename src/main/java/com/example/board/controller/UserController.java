package com.example.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

import com.example.board.model.User;
import com.example.board.repository.UserRepository;

@Controller
public class UserController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}

	@PostMapping("/signin")
	public String signinPost(@ModelAttribute User user) {
		User dbUser = userRepository.findByEmail(user.getEmail());
		String encodedPwd = dbUser.getPwd();
		String userPwd = user.getPwd();
		boolean isMatch = passwordEncoder.matches(userPwd, encodedPwd);
		if (isMatch) {
			session.setAttribute("user_info", dbUser);
		}
		// User dbUser = userRepository.findByEmailAndPwd(
		// user.getEmail(), user.getPwd());
		// if (dbUser != null) {
		// session.setAttribute("user_info", dbUser);
		// }
		return "redirect:/";
	}

	@GetMapping("/signout")
	public String signout() {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}

	@PostMapping("/signup")
	public String signupPost(@ModelAttribute User user) {
		String userPwd = user.getPwd();
		String encodedPwd = passwordEncoder.encode(userPwd);
		user.setPwd(encodedPwd);

		userRepository.save(user);
		return "redirect:/";
	}
}