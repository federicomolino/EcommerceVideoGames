package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName (String name);
}
