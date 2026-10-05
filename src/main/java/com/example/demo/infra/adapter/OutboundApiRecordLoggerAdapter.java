package com.example.demo.infra.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.OutboundApiRecordLoggerPort;
import com.example.demo.application.shared.command.RecordOutboundApiRequestCommand;
import com.example.demo.infra.persistence.repository.OutboundApiRecordRepository;
import com.example.demo.infra.persistence.entity.OutboundApiRecord;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OutboundApiRecordLoggerAdapter implements OutboundApiRecordLoggerPort {

	private final OutboundApiRecordRepository repository;

	@Override
	public Long save(RecordOutboundApiRequestCommand command) {
		OutboundApiRecord record = new OutboundApiRecord();
		record.create(command);
		record = repository.save(record);
		return record.getId();
	}

}
