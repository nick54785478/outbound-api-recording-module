package com.example.demo.infra.adapter;

import org.springframework.stereotype.Component;

import com.example.demo.application.shared.command.log.RecordFailedOutboundApiCommand;
import com.example.demo.application.shared.command.log.RecordSuccessOutboundApiCommand;
import com.example.demo.application.port.OutboundApiResponseHandlerPort;
import com.example.demo.infra.persistence.OutboundApiRecordRepository;
import lombok.AllArgsConstructor;

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
		outboundApiRecordRepository.findById(command.getSavedId()).ifPresent(outboundApiRecord -> {
			outboundApiRecord.markSuccess(command);
			outboundApiRecordRepository.save(outboundApiRecord);
		});
	}

	@Override
	public void handleFailure(RecordFailedOutboundApiCommand command) {
		outboundApiRecordRepository.findById(command.getSavedId()).ifPresent(outboundApiRecord -> {
			outboundApiRecord.markFailed(command);
			outboundApiRecordRepository.save(outboundApiRecord);
		});

	}

}
