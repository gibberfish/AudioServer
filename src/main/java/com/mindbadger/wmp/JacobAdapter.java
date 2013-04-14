package com.mindbadger.wmp;

import org.apache.log4j.Logger;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobAdapter {
	Logger logger = Logger.getLogger(JacobAdapter.class);

	private ActiveXComponent wmp;

	public JacobAdapter() {
		// Need to ensure the DLL is installed on the target PC
		wmp = new ActiveXComponent("WMPlayer.OCX");
		
		logger.debug("WMPlayer.OCX: " + wmp);
	}

	public Dispatch getDispatch(String property) {
		return wmp.getProperty("mediaCollection").toDispatch();
	}

	public Variant getVariant(Dispatch dispatch, String property) {
		return Dispatch.get(dispatch, property);
	}

	public Dispatch call(Dispatch itemToExecuteOn, String method) {
		return Dispatch.call(itemToExecuteOn, method).toDispatch();
	}

	public Dispatch call(Dispatch itemToExecuteOn, String method, Object arg) {
		return Dispatch.call(itemToExecuteOn, method, arg).toDispatch();
	}

	public Variant getVariant(Dispatch itemToExecuteOn, String method, Object arg) {
		return Dispatch.call(itemToExecuteOn, method, arg);
	}
}
