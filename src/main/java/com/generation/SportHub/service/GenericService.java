package com.generation.SportHub.service;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.SportHub.converters.GenericConverter;
import com.generation.SportHub.dto.GenericDTO;
import com.generation.SportHub.entity.GenericEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class GenericService
                                    <ID,
                                    E extends GenericEntity,
                                    D extends GenericDTO,
                                    C extends GenericConverter<D, E>,
                                    R extends JpaRepository<E, ID>> {

    private final R repository;
    private final C converter;
    private final ApplicationContext context;

    public abstract E construct(Map<String,String> params);


    public List<D> getAll(){
        List<E> lista = repository.findAll();
        List<D> listaDTO = lista.stream().map(
            e ->  converter.fromEntityToDto(e)
        ).toList();
        return listaDTO;
    }

    public D getById(ID id){
        E e= repository.getReferenceById(id);
        D d= converter.fromEntityToDto(e);
        return d;

    }

    public boolean save(Map<String,String> params){
        try {
            E e = construct(params);
            repository.save(e);
            return true;
        } catch (Exception excep) {
            excep.printStackTrace();
            return false;
        }
    }

    public void delete(ID id){
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

     

}
