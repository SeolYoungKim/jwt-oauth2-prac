package com.example.jwt_oauth_prac.repository;

import com.example.jwt_oauth_prac.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByAuthId(String authId);

    Boolean existsByEmail(String email);
    Boolean existsByAuthId(String authId);
}
