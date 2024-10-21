package com.enablefn.sert.service;

import com.enablefn.sert.dto.UserDTO;
import com.enablefn.sert.mapper.UserMapper;
import com.enablefn.sert.model.User;
import com.enablefn.sert.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Value("${keycloak.auth.server.url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.admin.username}")
    private String keycloakAdminUsername;

    @Value("${keycloak.admin.password}")
    private String keycloakAdminPassword;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.userToUserDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.userToUserDTO(savedUser);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        User updatedUser = userRepository.save(user);
        return userMapper.userToUserDTO(updatedUser);
    }

    public String registerUser(UserDTO userDTO) {
        String token = getAdminToken(); // Получаем токен администратора
        String url = keycloakAuthServerUrl + "/admin/realms/" + keycloakRealm + "/users";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token); // Добавляем токен в заголовок

        // Создаем тело запроса
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", userDTO.getEmail()); // Используем email как логин
        userMap.put("email", userDTO.getEmail());
        userMap.put("enabled", true);
        userMap.put("firstName", userDTO.getFullName());
        userMap.put("lastName", userDTO.getFullName());
        userMap.put("credentials", List.of(Map.of("type", "password", "value", userDTO.getPassword(), "temporary", false))); // Указываем пароль из DTO

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userMap, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);

        return response.getStatusCode() == HttpStatus.CREATED ? "User created successfully" : "Error creating user";
    }

    public String authenticateUser(String username, String password) {
        String url = keycloakAuthServerUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=password"
                + "&username=" + username
                + "&password=" + password
                + "&client_id=user"
                + "&client_secret=qohI3y0W23FxyQkbmuC8Du7CNNae72bl";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        // Проверка ответа
        if (response.getStatusCode() != HttpStatus.OK) {
            System.out.println("Authentication failed: " + response.getBody());
            throw new RuntimeException("Authentication failed");
        }

        return (String) response.getBody().get("access_token");
    }


    private String getAdminToken() {
        String url = keycloakAuthServerUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=password&username=" + keycloakAdminUsername + "&password=" + keycloakAdminPassword + "&client_id=admin-cli";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        return (String) response.getBody().get("access_token");
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
