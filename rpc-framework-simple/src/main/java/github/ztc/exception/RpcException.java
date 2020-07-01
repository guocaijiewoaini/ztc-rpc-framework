package github.ztc.exception;

import github.ztc.rpcEnum.RpcErrorMessageEnum;

/**
 * @title: RpcException
 * @description: rpc异常类
 * @author: ztc
 * @date: 2020/07/01 03:00 15:00
 * @version: V1.0
*/
public class RpcException extends RuntimeException {

    public RpcException(RpcErrorMessageEnum errorMsg, String detail){
        super(errorMsg.getMessage()+":"+detail);
    }

    public RpcException(RpcErrorMessageEnum errorMsg){
        super(errorMsg.getMessage());
    }

    public RpcException(String msg,Throwable cause){
        super(msg,cause);
    }
}
