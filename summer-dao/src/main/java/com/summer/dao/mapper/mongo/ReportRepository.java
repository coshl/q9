package com.summer.dao.mapper.mongo;

import com.summer.dao.entity.mongo.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report,Integer> {
}
