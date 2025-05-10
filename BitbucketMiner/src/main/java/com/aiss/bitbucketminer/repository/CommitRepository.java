package com.aiss.bitbucketminer.repository;

import com.aiss.bitbucketminer.entity.CommitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitRepository extends JpaRepository<CommitEntity, String> {}

