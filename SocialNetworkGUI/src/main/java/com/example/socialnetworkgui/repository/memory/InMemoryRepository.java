package com.example.socialnetworkgui.repository.memory;

import com.example.socialnetworkgui.domain.Entity;
import com.example.socialnetworkgui.domain.validators.ValidationException;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.exceptions.RepositoryException;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private Validator<E> validator;

    protected Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    public Map<ID, E> getEntities() {
        return entities;
    }

    @Override
    public E save(E entity) {
        if (entity == null)
            throw new RepositoryException("Entitatea nu poate fi null! ");
        if (entities.get(entity.getId()) != null)
            throw new RepositoryException("Utilizator deja existent! ");
        try {
            validator.validate(entity);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return null;
        }
        entities.put(entity.getId(), entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if (id == null)
            throw new RepositoryException("ID-ul nu poate fi null! ");
        E entity = findOne(id);
        if (entity == null)
            throw new RepositoryException("Utilizatorul pe care doriti sa il stergeti nu exista! ");
        entities.remove(id);
        return entity;
    }

    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new RepositoryException("ID-ul nu poate fi null! ");
        if (entities.get(id) == null) {
            throw new RepositoryException("Nu exista entitatea cu ID-ul dat! ");
        } else
            return entities.get(id);
    }

    @Override
    public E update(E entity) {
        if (entity == null)
            throw new RepositoryException("entity must be not null!");
        if(entities.get(entity.getId())==null)
            throw new RepositoryException("Nu exista entitatea cu ID-ul dat! ");
        try {
            validator.validate(entity);
        }catch(ValidationException e){
            System.out.println(e.getMessage());
            return null;
        }
        entities.put(entity.getId(),entity);
        return entity;
    }

    public Integer size() {
        return entities.size();
    }

}
