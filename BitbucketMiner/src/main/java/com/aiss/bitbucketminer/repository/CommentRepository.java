package com.aiss.bitbucketminer.repository;

import com.aiss.bitbucketminer.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, String> {}

