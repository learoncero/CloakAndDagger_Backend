package at.fhv.backend.service;

import at.fhv.backend.model.Sabotage;
import at.fhv.backend.repository.SabotageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SabotageService {
    private final SabotageRepository sabotageRepository;

    @Autowired
    public SabotageService(SabotageRepository sabotageRepository) {
        this.sabotageRepository = sabotageRepository;
    }

    public List<Sabotage> getAllSabotages() {
        return sabotageRepository.getAllSabotages();
    }
}
