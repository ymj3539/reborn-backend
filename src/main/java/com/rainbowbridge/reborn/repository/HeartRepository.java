package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.Company;
import com.rainbowbridge.reborn.domain.Heart;
import com.rainbowbridge.reborn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    List<Heart> findAllByUserAndCompany(User user, Company company);
    List<Heart> findAllByUser(User user);

}
