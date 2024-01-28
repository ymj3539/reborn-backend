package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository  extends JpaRepository<File, Long> {
}
