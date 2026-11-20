package com.example.demo.infra.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.out.OutboundApiRecordLoggerPort;
import com.example.demo.application.shared.command.RecordOutboundApiRequestCommand;
import com.example.demo.infra.persistence.entity.OutboundApiRecord;
import com.example.demo.infra.persistence.repository.OutboundApiRecordRepository;

import lombok.AllArgsConstructor;

/**
 * OutboundApiRecord Logger Outbound Adapter
 */
@Component
@AllArgsConstructor
public class OutboundApiRecordLoggerAdapter implements OutboundApiRecordLoggerPort {

	private final OutboundApiRecordRepository repository;

	@Override
	public Long save(RecordOutboundApiRequestCommand command) {
		OutboundApiRecord apiRecord = OutboundApiRecord.create(command);
		apiRecord = repository.save(apiRecord);
		return apiRecord.getId();
	}

}
