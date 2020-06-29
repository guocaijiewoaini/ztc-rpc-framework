package github.ztc.transport;

import github.ztc.dto.RpcRequest;

/**
 * @title: ClientTransport
 * @description: 发送消息到服务端
 * @author: ztc
 * @date: 2020/06/29 09:33 21:33
 * @version: V1.0
*/
public interface ClientTransport {


    void sendRpcRequest(RpcRequest rpcRequest);
}
