package com.abdellah.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abdellah.demo.entity.Internship;

public interface InternshipRepository extends JpaRepository<Internship, Long>
{
    List<Internship> findAllByUser_IdOrderByIdDesc(Long userId);

    Optional<Internship> findByIdAndUser_Id(Long internshipId, Long userId);

    long deleteByIdAndUser_Id(Long internshipId, Long userId);
}
