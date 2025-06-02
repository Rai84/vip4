package com.staxrt.tutorial;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import com.pi.vip4.Application;
import com.pi.vip4.model.Usuario;
import com.pi.vip4.model.Usuario.Tipo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetAllUsers() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/users",
				HttpMethod.GET, entity, String.class);

		Assert.assertNotNull(response.getBody());
	}

	@Test
	public void testGetUserById() {
		Usuario user = restTemplate.getForObject(getRootUrl() + "/users/1", Usuario.class);
		System.out.println(user.getNome());
		Assert.assertNotNull(user);
	}

	@Test
	public void testCreateUser() {
    // Criação de um novo usuário
    Usuario user = new Usuario();
    user.setEmail("admin@gmail.com");
    user.setNome("admin");
    user.setCpf("11111111111"); // CPF com 11 dígitos
    user.setTipo(Usuario.Tipo.ADMIN); // Usando o enum diretamente
    user.setStatus(true); // Status ativo

    // Envia a requisição POST para o endpoint '/users/save'
    ResponseEntity<Usuario> postResponse = restTemplate.postForEntity(getRootUrl() + "/users/save", user, Usuario.class);

    // Verifica se a resposta não é nula
    Assert.assertNotNull(postResponse);
    Assert.assertNotNull(postResponse.getBody());

    // Verifica se o código de status da resposta é 201 Created
    Assert.assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
}


	@Test
	public void testUpdateUser() {
		int id = 1;
		Usuario user = restTemplate.getForObject(getRootUrl() + "/users/" + id, Usuario.class);
		user.setNome("admin1");
		user.setTipo(Tipo.ESTOQUISTA); // Atualizando tipo com o enum Tipo.ESTOQUISTA
		user.setStatus(false); // Atualizando status para inativo

		restTemplate.put(getRootUrl() + "/users/" + id, user);

		Usuario updatedUser = restTemplate.getForObject(getRootUrl() + "/users/" + id, Usuario.class);
		Assert.assertNotNull(updatedUser);
		Assert.assertEquals("admin1", updatedUser.getNome());
		Assert.assertEquals(Tipo.ESTOQUISTA, updatedUser.getTipo()); // Verificando tipo com enum
		Assert.assertFalse(updatedUser.isStatus()); // Verificando status
	}

	@Test
	public void testDeleteUser() {
		int id = 2;
		Usuario user = restTemplate.getForObject(getRootUrl() + "/users/" + id, Usuario.class);
		Assert.assertNotNull(user);

		restTemplate.delete(getRootUrl() + "/users/" + id);

		try {
			user = restTemplate.getForObject(getRootUrl() + "/users/" + id, Usuario.class);
		} catch (final HttpClientErrorException e) {
			Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}
