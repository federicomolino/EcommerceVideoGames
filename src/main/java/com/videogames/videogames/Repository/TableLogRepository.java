package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.TableLogAPI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableLogRepository extends JpaRepository<TableLogAPI, Integer> {
}
