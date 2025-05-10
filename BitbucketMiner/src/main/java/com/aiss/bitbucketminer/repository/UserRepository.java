package com.aiss.bitbucketminer.repository;

import com.aiss.bitbucketminer.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {}

