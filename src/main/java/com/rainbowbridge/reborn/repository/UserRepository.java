package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
