package com.aiss.bitbucketminer.repository;

import com.aiss.bitbucketminer.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<IssueEntity, String> {}

