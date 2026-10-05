package com.example.demo.infra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.infra.persistence.entity.OutboundApiRecord;

public interface OutboundApiRecordRepository extends JpaRepository<OutboundApiRecord, Long> {

}
