package com.example.demo.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.application.domain.log.aggregate.OutboundApiRecord;

public interface OutboundApiRecordRepository extends JpaRepository<OutboundApiRecord, Long> {

}
