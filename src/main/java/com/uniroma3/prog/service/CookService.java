package com.uniroma3.prog.service;

import com.uniroma3.prog.model.Cook;
import com.uniroma3.prog.repository.CookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CookService {

    @Autowired
    protected CookRepository cuocoRepository;

    public Cook findById(long id) {
        return cuocoRepository.findById(id).get();
    }
    public Iterable<Cook> findAll(){
        return cuocoRepository.findAll();
    }

    @Transactional
    public Cook getCuoco(Long id) {
        Optional<Cook> result = this.cuocoRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Cook saveCuoco(Cook cuoco) {
        return this.cuocoRepository.save(cuoco);
    }

    @Transactional
    public List<Cook> getAllCuochi() {
        List<Cook> result = new ArrayList<>();
        Iterable<Cook> iterable = this.cuocoRepository.findAll();
        for(Cook cuoco : iterable)
            result.add(cuoco);
        return result;
    }


}
