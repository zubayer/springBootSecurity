package com.example.service;

import java.util.Arrays;
import java.util.HashSet;

import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.Role;
import com.example.model.User;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService{

	@Autowired
	@Qualifier("userRepository")
	private UserRepository userRepository;
	@Autowired
	@Qualifier("roleRepository")
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {
//		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setPassword(user.getPassword());
        user.setActive(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findUserByEmail(username);
		if(user==null){
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(user);
	}

}
