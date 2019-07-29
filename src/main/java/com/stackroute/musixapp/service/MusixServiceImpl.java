package com.stackroute.musixapp.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stackroute.musixapp.domain.Musix;
import com.stackroute.musixapp.exceptions.TrackAlreadyExistsException;
import com.stackroute.musixapp.exceptions.TrackNotFoundException;
import com.stackroute.musixapp.repository.MusixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MusixServiceImpl implements MusixService, ApplicationListener<ContextRefreshedEvent>, CommandLineRunner {

    @Value("${musix.1.name:default}")
    String name1;
    @Value("${musix.1.rating:default}")
    int rating1;
    @Value("${musix.1.comments:default}")
    String comments1;
    @Value("${musix.2.name:default}")
    String name2;
    @Value("${musix.2.rating:default}")
    int rating2;
    @Value("${musix.2.comments:default}")
    String comments2;

    MusixRepository musixRepository;

    @Autowired
    public MusixServiceImpl(MusixRepository musixRepository) {
       this.musixRepository = musixRepository;
    }

    @Override
    public Musix saveNewMusix(Musix musix) throws TrackAlreadyExistsException {
        if(musixRepository.existsById(musix.getId())){
            throw new TrackAlreadyExistsException("User already exists!");
        }
        Musix savedTrack = musixRepository.save(musix);
        if(savedTrack == null) {
            throw new TrackAlreadyExistsException("User already exists!");
        }
        return savedTrack;
    }

    @Override
    public List<Musix> getMusix() {
        return musixRepository.findAll();
    }

    @Override
    public Musix getById(int id) throws TrackNotFoundException{
        Optional<Musix> userId = musixRepository.findById(id);
        if(userId.isEmpty()){
            throw new TrackNotFoundException("Track not found!");
        }
        return userId.get();
    }

    @Override
    public void deleteById(int id) throws TrackNotFoundException {
        Optional<Musix> userId = musixRepository.findById(id);
        if(userId.isEmpty()){
            throw new TrackNotFoundException("Track not found!");
        }
        musixRepository.deleteById(id);
    }

    @Override
    public boolean updateById(Musix musix, int id) throws TrackNotFoundException {
        Optional<Musix> userOptional = musixRepository.findById(id);

        if(userOptional.isEmpty()){
            throw new TrackNotFoundException("Track not found!");
        }

        musix.setId(id);

        musixRepository.save(musix);
        return true;
    }

    @Override
    public List<Musix> getByName(String name) {
        List<Musix> userId = musixRepository.findTitleByName(name);
        return userId;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        musixRepository.save(new Musix(1, name1, rating1, comments1));
        musixRepository.save(new Musix(2, name2, rating2, comments2));
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
