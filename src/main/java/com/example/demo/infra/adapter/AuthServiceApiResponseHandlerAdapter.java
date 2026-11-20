package com.example.demo.infra.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.out.OutboundApiResponseHandlerPort;
import com.example.demo.application.shared.command.RecordFailedOutboundApiCommand;
import com.example.demo.application.shared.command.RecordSuccessOutboundApiCommand;
import com.example.demo.infra.persistence.repository.OutboundApiRecordRepository;

/**
 * AuthService Response Handler Outbound Adapter
 */
@Component
class AuthServiceApiResponseHandlerAdapter implements OutboundApiResponseHandlerPort {

	private final OutboundApiRecordRepository outboundApiRecordRepository;

	public AuthServiceApiResponseHandlerAdapter(OutboundApiRecordRepository outboundApiRecordRepository) {
		this.outboundApiRecordRepository = outboundApiRecordRepository;
	}

	@Override
	public String supportSystem() {
		return "AuthService";
	}

	@Override
	public void handleSuccess(RecordSuccessOutboundApiCommand command) {
		outboundApiRecordRepository.findById(command.savedId()).ifPresent(outboundApiRecord -> {
			outboundApiRecord.markSuccess(command);
			outboundApiRecordRepository.save(outboundApiRecord);
		});
	}

	@Override
	public void handleFailure(RecordFailedOutboundApiCommand command) {
		outboundApiRecordRepository.findById(command.savedId()).ifPresent(outboundApiRecord -> {
			outboundApiRecord.markFailed(command);
			outboundApiRecordRepository.save(outboundApiRecord);
		});
	}

}
