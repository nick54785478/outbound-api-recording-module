package com.example.demo.application.domain.log.aggregate.vo;

/**
 * Outbound API 呼叫狀態。
 *
 * <p>
 * 描述 OutboundApiRecord 在其生命週期中的狀態轉移， 狀態僅能由 Aggregate Root 內部行為進行改變。
 */
public enum OutboundApiStatus {

	/**
	 * 已建立請求紀錄，尚未完成外部呼叫。
	 */
	PENDING,

	/**
	 * 外部 API 呼叫成功完成。
	 */
	SUCCESS,

	/**
	 * 外部 API 呼叫失敗完成。
	 */
	FAILED
}
