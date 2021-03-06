package com.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cursomc.domain.Categoria;
import com.cursomc.dto.CategoriaDto;
import com.cursomc.repositories.CategoriaRepository;
import com.cursomc.services.exceptions.DataIntegrityException;
import com.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
		
	public List<Categoria> findAll() {
		return repo.findAll();
	}

	public Categoria findById(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				 "Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getSimpleName()));
	}


	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}


	public Categoria update(Categoria obj) {
		Categoria newCategoria = findById(obj.getId());
		updateData(newCategoria, obj);
		return repo.save(newCategoria);
	}
	
	public void delete(Integer id) {
		findById(id);
		try {
			repo.deleteById(id);
			
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos vinculados a ela");
		}
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequeste = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequeste);
		
	}
	
	public Categoria fromDto(CategoriaDto objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
	private void updateData(Categoria newCategoria, Categoria obj) {
		newCategoria.setNome(obj.getNome());
	}
}
