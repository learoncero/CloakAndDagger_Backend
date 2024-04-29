package at.fhv.tasks.service;

import at.fhv.tasks.model.MiniGame;
import at.fhv.tasks.repository.MiniGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiniGameService {
    private final MiniGameRepository miniGameRepository;

    @Autowired
    public MiniGameService(MiniGameRepository miniGameRepository) {
        this.miniGameRepository = miniGameRepository;
    }

    public List<MiniGame> getAllMiniGames() {
        return miniGameRepository.getAllMiniGames();
    }

    public MiniGame getMiniGameById(int id) {
        return miniGameRepository.getMiniGameById(id);
    }
}
