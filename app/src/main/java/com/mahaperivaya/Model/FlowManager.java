package com.mahaperivaya.Model;

import android.content.Context;
import android.os.Handler;

/**
 * Created by m84098 on 10/4/15.
 */
public class FlowManager {

  private static FlowManager flowManager;
  private static Handler flowHandler;

  public static FlowManager getInstance(Handler hdlr) {
    if (flowManager == null) {
      flowManager = new FlowManager();
      flowHandler = hdlr;
    }
    return flowManager;
  }

  public Handler getFlowManagerHandler(){
    return flowHandler;
  }

}
