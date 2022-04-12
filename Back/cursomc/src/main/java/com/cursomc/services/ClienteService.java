package com.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cursomc.domain.Cidade;
import com.cursomc.domain.Cliente;
import com.cursomc.domain.Endereco;
import com.cursomc.domain.enums.TipoCliente;
import com.cursomc.dto.ClienteDto;
import com.cursomc.dto.ClienteNewDto;
import com.cursomc.repositories.ClienteRepository;
import com.cursomc.repositories.EnderecoRepository;
import com.cursomc.services.exceptions.DataIntegrityException;
import com.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				 "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getSimpleName()));
	}
	
	@Transactional
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		cliente = repo.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
	}

	public Cliente update(Cliente obj) {
		Cliente newCliente = findById(obj.getId());
		updateData(newCliente, obj);
		return repo.save(newCliente);
	}
	
	public void delete(Integer id) {
		findById(id);
		try {
			repo.deleteById(id);
			
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que possui pedidos vinculados a ele");
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequeste = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequeste);
		
	}
	
	private void updateData(Cliente newCliente, Cliente obj) {
		newCliente.setNome(obj.getNome());
		newCliente.setEmail(obj.getEmail()); 
	}
	
	public Cliente fromDto(ClienteDto clienteDto) {
		return new Cliente(clienteDto.getId(), clienteDto.getNome(), clienteDto.getEmail(), null, null);
	}
	
	public Cliente fromDto(ClienteNewDto clienteDto) {
		Cliente cliente = new Cliente(null, clienteDto.getNome(), clienteDto.getEmail(), clienteDto.getCpfOuCnpj(), TipoCliente.toEnum(clienteDto.getTipo()));
		
		Cidade cidade = new Cidade(clienteDto.getCidadeId(), null, null);
		
		Endereco endereco = new Endereco(null, clienteDto.getLogradouro(),  clienteDto.getNumero(),
										clienteDto.getComplemento(), clienteDto.getBairro(), clienteDto.getCep(),
										cliente, cidade);
		
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteDto.getTelefone1());
		
		if(clienteDto.getTelefone2() != null)
			cliente.getTelefones().add(clienteDto.getTelefone2());
		
		if(clienteDto.getTelefone3() != null)
			cliente.getTelefones().add(clienteDto.getTelefone3());
		
		
		return cliente;
	}
	
	
}
