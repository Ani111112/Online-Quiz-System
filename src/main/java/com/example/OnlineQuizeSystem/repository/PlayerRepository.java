package com.example.OnlineQuizeSystem.repository;

import com.example.OnlineQuizeSystem.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player>findByEmail(String email);
}
