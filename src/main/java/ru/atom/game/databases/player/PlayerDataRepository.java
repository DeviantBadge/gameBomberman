package ru.atom.game.databases.player;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayerDataRepository extends CrudRepository<PlayerData, Long> {
    List<PlayerData> findByName(String name);
}
