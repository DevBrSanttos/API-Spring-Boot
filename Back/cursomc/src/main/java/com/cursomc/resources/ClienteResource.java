package com.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cursomc.domain.Cliente;
import com.cursomc.dto.ClienteDto;
import com.cursomc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id){
		
		Cliente cliente = service.findById(id);
		return ResponseEntity.ok().body(cliente);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDto>> find() {
		List<Cliente> clientes = service.findAll();
		List<ClienteDto> categoriaDto = clientes.stream().map(categoria -> new ClienteDto(categoria))
															 .collect(Collectors.toList());
		
		return ResponseEntity.ok().body(categoriaDto);
	}
	
	@RequestMapping(value = "/{id}" , method = RequestMethod.PUT)
	public ResponseEntity<Cliente> update(@Valid @RequestBody ClienteDto categoriaDto, @PathVariable Integer id){
		Cliente categoria = service.fromDto(categoriaDto);
		
		categoria.setId(id);
		categoria = service.update(categoria);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ClienteDto>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction){
		
		
		Page<Cliente> clientes = service.findPage(page, linesPerPage, orderBy, direction);
		
		Page<ClienteDto> clientesDto = clientes.map(categoria -> new ClienteDto(categoria));
		
		return ResponseEntity.ok().body(clientesDto);
		
	}

	
}
