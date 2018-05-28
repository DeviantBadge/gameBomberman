package ru.atom.game.databases.player;

import javax.persistence.*;

@Entity
@Table(name = "player", schema = "game")
public class PlayerData {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "games_played")
    private Integer gamesPlayed;

    @Column(name = "games_won")
    private Integer gamesWon;

    @Column(name = "rating")
    private Integer rating;

    protected PlayerData() {}

    public PlayerData(String name, String password) {
        this.name = name;
        this.password = password;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.rating = 1000;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, name='%s', rating='%s', gamesPlayed='%s']",
                id, name, rating.toString(), gamesPlayed.toString());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public Integer getRating() {
        return rating;
    }

    public Integer addToRating(int ratingPoints) {
        return rating += ratingPoints;
    }
}
