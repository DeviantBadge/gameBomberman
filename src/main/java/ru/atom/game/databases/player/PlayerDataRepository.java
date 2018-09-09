package ru.atom.game.databases.player;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayerDataRepository extends CrudRepository<PlayerData, Long> {
    PlayerData findByName(String name);

    default boolean savePlayer(@NotNull PlayerData player) {
        try {
            save(player);
        } catch (Throwable e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
