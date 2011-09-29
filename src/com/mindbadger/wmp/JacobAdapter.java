package com.mindbadger.wmp;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobAdapter {
  private ActiveXComponent wmp;
  
  public JacobAdapter () {
    wmp = new ActiveXComponent("WMPlayer.OCX");
  }
  
  public Dispatch getDispatch (String property) {
    return wmp.getProperty("mediaCollection").toDispatch();
  }
  
  public Variant getVariant (Dispatch dispatch, String property) {
    return Dispatch.get(dispatch, property); 
  }

  public Dispatch call (Dispatch itemToExecuteOn, String method) {
    return Dispatch.call(itemToExecuteOn, method).toDispatch();
  }

  public Dispatch call(Dispatch itemToExecuteOn, String method, Object arg) {
    return Dispatch.call(itemToExecuteOn, method, arg).toDispatch();
  }
  
  public Variant getVariant(Dispatch itemToExecuteOn, String method, Object arg) {
    return Dispatch.call(itemToExecuteOn, method, arg);
  }
}
