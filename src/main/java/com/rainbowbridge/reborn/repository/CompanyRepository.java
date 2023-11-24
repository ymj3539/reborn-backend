package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, String> {
    @Query("SELECT c FROM Company c WHERE c.id NOT IN (SELECT t.company.id FROM TimeOff t WHERE t.date = :date AND t.time = :time)")
    List<Company> findAvailableCompanieList(@Param("date") LocalDate date, @Param("time") int time);

    // 지역권 별로 업체 리스트 조회
    List<Company> findAllByRegion(String displayName);
}
